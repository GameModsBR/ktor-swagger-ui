package io.github.smiley4.ktorswaggerui.specbuilder

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.github.victools.jsonschema.generator.SchemaGenerator
import io.github.smiley4.ktorswaggerui.CapturedType
import io.github.smiley4.ktorswaggerui.SwaggerUIPluginConfig
import io.swagger.v3.oas.models.media.Schema
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.serializer
import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.javaType

/**
 * Builder for an OpenAPI Schema Object that describes a json-object (or array)
 */
class OApiJsonSchemaBuilder {

    private val jsonToSchemaConverter = JsonToOpenApiSchemaConverter()

    fun build(type: CapturedType, components: ComponentsContext, config: SwaggerUIPluginConfig): Schema<Any> {
        return if (components.schemasInComponents) {
            val schema = createSchema(type, config)
            if (schema.type == "array") {
                components.addArraySchema(type, schema)
            } else {
                components.addSchema(type, schema)
            }
        } else {
            createSchema(type, config)
        }
    }


    private fun createSchema(descriptor: SerialDescriptor, config: SwaggerUIPluginConfig): Schema<Any> {

    }

    private fun createSchema(type: CapturedType, config: SwaggerUIPluginConfig): Schema<Any> {
        type.serializer?.descriptor?.let {
            return createSchema(it, config)
        }
        val kType = type.kType
        val classifier = kType.classifier
        return if (classifier is KClass<*> && classifier.isSubclassOf(Array::class)) {
            Schema<Any>().apply {
                this.type = "array"
                val component = classifier.typeParameters.single().upperBounds.single()
                val componentSerializer = try {
                    serializer(component)
                } catch (_: Exception) { null }

                this.items = createObjectSchema(CapturedType(component, componentSerializer), config)
            }
        } else {
            val enum = (classifier as? KClass<*>)?.java?.takeIf { it.isEnum }
            if (enum != null) {
                Schema<Any>().apply {
                    this.type = "string"
                    this.enum = enum.enumConstants.map { it.toString() }
                }
            } else {
                return createObjectSchema(type, config)
            }
        }
    }


    private fun createObjectSchema(type: CapturedType, config: SwaggerUIPluginConfig): Schema<Any> {
        return if (type is Class<*> && type.isEnum) {
            Schema<Any>().apply {
                this.type = "string"
                this.enum = type.enumConstants.map { it.toString() }
            }
        } else {
            val jsonSchema = createObjectJsonSchema(type, config)
            return jsonToSchemaConverter.toSchema(jsonSchema)
        }
    }

    private fun createObjectJsonSchema(type: CapturedType, config: SwaggerUIPluginConfig): ObjectNode {
        if (config.getCustomSchemas().getJsonSchemaBuilder() != null) {
            val jsonSchema = config.getCustomSchemas().getJsonSchemaBuilder()?.let { it(type) }
            if (jsonSchema != null) {
                return ObjectMapper().readTree(jsonSchema) as ObjectNode
            }
        }
        return generateJsonSchema(type, config)
    }

    private fun generateJsonSchema(type: CapturedType, config: SwaggerUIPluginConfig): ObjectNode {
        val generatorConfig = config.schemaGeneratorConfigBuilder.build()
        return SchemaGenerator(generatorConfig).generateSchema(type.kType.javaType)
    }

}
