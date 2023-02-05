package io.github.smiley4.ktorswaggerui.specbuilder

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.github.victools.jsonschema.generator.SchemaGenerator
import io.github.smiley4.ktorswaggerui.CapturedType
import io.github.smiley4.ktorswaggerui.SwaggerUIPluginConfig
import io.swagger.v3.oas.models.media.Schema
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.json.JsonObject
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.javaType

/**
 * Builder for an OpenAPI Schema Object that describes a json-object (or array)
 */
class OApiJsonSchemaBuilder {

    private val jsonToSchemaConverter = JsonToOpenApiSchemaConverter()

    fun build(type: CapturedType, components: ComponentsContext, config: SwaggerUIPluginConfig): Schema<Any> {
        return try {
            if (components.schemasInComponents) {
                val schema = createSchema(type, components, config)
                if (schema.type == "array") {
                    components.addArraySchema(type, schema)
                } else {
                    components.addSchema(type, schema)
                }
            } else {
                createSchema(type, components, config)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            Schema<Any>().apply { this.type = "object" }
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun createSchema(descriptor: SerialDescriptor, capturedType: CapturedType, config: SwaggerUIPluginConfig, components: ComponentsContext): Schema<Any> {
        if (descriptor.isInline) {
            val elementDescriptor = descriptor.getElementDescriptor(0)
            return build(CapturedType(elementDescriptor.capturedKClass?.starProjectedType, elementDescriptor), components, config)
        }
        return Schema<Any>().apply {
            nullable = descriptor.isNullable
            description = descriptor.serialName
            when (descriptor.kind) {
                PrimitiveKind.BOOLEAN -> type = "boolean"
                PrimitiveKind.STRING -> {
                    type = "string"
                    when (descriptor.serialName) {
                        "Instant" -> format = "date-time"
                    }
                }
                PrimitiveKind.CHAR -> {
                    type = "string"
                    minLength = 1
                    maxLength = 1
                }
                PrimitiveKind.BYTE -> {
                    type = "integer"
                    minimum = Byte.MIN_VALUE.toLong().toBigDecimal()
                    maximum = Byte.MAX_VALUE.toLong().toBigDecimal()
                }
                PrimitiveKind.SHORT -> {
                    type = "integer"
                    minimum = Short.MIN_VALUE.toLong().toBigDecimal()
                    maximum = Short.MAX_VALUE.toLong().toBigDecimal()
                }
                PrimitiveKind.INT -> {
                    type = "integer"
                    minimum = Int.MIN_VALUE.toBigDecimal()
                    maximum = Int.MAX_VALUE.toBigDecimal()
                }
                PrimitiveKind.LONG -> {
                    type = "integer"
                    minimum = Long.MIN_VALUE.toBigDecimal()
                    maximum = Long.MAX_VALUE.toBigDecimal()
                }
                PrimitiveKind.FLOAT -> {
                    type = "number"
                    format = "float"
                }
                PrimitiveKind.DOUBLE -> {
                    type = "double"
                    format = "float"
                }
                SerialKind.ENUM -> {
                    type = "string"
                    enum = descriptor.elementNames.toList()
                }
                StructureKind.LIST -> {
                    type = "array"
                    if (descriptor.elementsCount == 1) {
                        val itemDescriptor = descriptor.getElementDescriptor(0)
                        val itemKType = itemDescriptor.capturedKClass?.starProjectedType
                        items = build(CapturedType(itemKType, itemDescriptor), components, config)
                    }
                }
                StructureKind.MAP -> {
                    type = "object"
                    if (descriptor == serialDescriptor<JsonObject>() || descriptor == serialDescriptor<JsonObject?>()) {
                        additionalProperties = true
                    } else if (descriptor.elementsCount == 2) {
                        val keyDescriptor = descriptor.getElementDescriptor(0)
                        val valueDescriptor = descriptor.getElementDescriptor(1)
                        val keyType = CapturedType(keyDescriptor.capturedKClass?.starProjectedType, keyDescriptor)
                        val valueType = CapturedType(valueDescriptor.capturedKClass?.starProjectedType, valueDescriptor)
                        properties = mapOf(
                            "key" to build(keyType, components, config),
                        )
                        additionalProperties = build(valueType, components, config)
                    }
                    descriptor.getElementDescriptor(1)
                }
                StructureKind.OBJECT -> {
                    type = "object"
                }
                StructureKind.CLASS, PolymorphicKind.SEALED-> {
                    type = "object"
                    properties = descriptor.elementNames
                        .map { name ->
                            val index = descriptor.getElementIndex(name)
                            name to descriptor.getElementDescriptor(index)
                        }.filter { (_, elementDescriptor) -> elementDescriptor.kind != SerialKind.CONTEXTUAL }
                        .associate { (name, elementDescriptor) ->
                            val elementKType = elementDescriptor.capturedKClass?.starProjectedType
                            name to build(CapturedType(elementKType, elementDescriptor), components, config)
                        }
                }
                PolymorphicKind.OPEN, SerialKind.CONTEXTUAL -> {
                    createObjectJsonSchema(capturedType, config)
                }
            }
        }
    }

    private fun createSchema(type: CapturedType, components: ComponentsContext, config: SwaggerUIPluginConfig): Schema<Any> {
        type.descriptor?.let {
            return createSchema(it, type, config, components)
        }
        val kType = type.kType
        val classifier = kType?.classifier
        return if (classifier is KClass<*> && classifier.isSubclassOf(Array::class)) {
            Schema<Any>().apply {
                this.type = "array"
                description = classifier.qualifiedName
                val component = classifier.typeParameters.single().upperBounds.single()
                val componentDescriptor = try {
                    serialDescriptor(component)
                } catch (_: Exception) { null }

                this.items = createObjectSchema(CapturedType(component, componentDescriptor), config)
            }
        } else {
            val enum = (classifier as? KClass<*>)?.java?.takeIf { it.isEnum }
            if (enum != null) {
                Schema<Any>().apply {
                    description = classifier.qualifiedName
                    this.type = "string"
                    this.enum = enum.enumConstants.map { (it as Enum<*>).name }
                }
            } else {
                return createObjectSchema(type, config)
            }
        }
    }


    private fun createObjectSchema(type: CapturedType, config: SwaggerUIPluginConfig): Schema<Any> {
        val classifier = type.kType?.classifier
        val enum = (classifier as? KClass<*>)?.java?.takeIf { it.isEnum }
        return if (enum != null) {
            Schema<Any>().apply {
                this.type = "string"
                description = classifier.qualifiedName
                this.enum = enum.enumConstants.map { (it as Enum<*>).name }
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
        return type.kType?.javaType?.let {
            SchemaGenerator(generatorConfig).generateSchema(it)
        } ?: generatorConfig.createObjectNode()
    }

}
