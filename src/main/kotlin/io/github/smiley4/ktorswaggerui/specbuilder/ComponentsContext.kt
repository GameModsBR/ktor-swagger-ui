package io.github.smiley4.ktorswaggerui.specbuilder

import io.github.smiley4.ktorswaggerui.CapturedType
import io.github.smiley4.ktorswaggerui.dsl.OpenApiExample
import io.swagger.v3.oas.models.media.Schema
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType
import kotlin.reflect.jvm.javaType

/**
 * Container holding and collecting information about the OpenApi "Components"-Object
 */
data class ComponentsContext(
    val schemasInComponents: Boolean,
    val schemas: MutableMap<String, Schema<*>>,
    val examplesInComponents: Boolean,
    val examples: MutableMap<String, OpenApiExample>,
    val canonicalNameObjectRefs: Boolean
) {

    companion object {
        val NOOP = ComponentsContext(false, mutableMapOf(), false, mutableMapOf(), false)
    }


    /**
     * Add the given schema for the given type to the components-section.
     * The schema is an array, only the element type is added to the components-section
     * @return a schema referencing the complete schema (or the original schema if 'schemasInComponents' = false)
     */
    fun addArraySchema(type: CapturedType, schema: Schema<*>): Schema<Any> {
        if (this.schemasInComponents) {
            val innerSchema: Schema<Any> = addSchema(type.parameter(0), schema.items)
            return Schema<Any>().apply {
                this.type = "array"
                this.items = Schema<Any>().apply {
                    `$ref` = innerSchema.`$ref`
                }
            }
        } else {
            @Suppress("UNCHECKED_CAST")
            return schema as Schema<Any>
        }
    }


    /**
     * Add the given schema for the given type to the components-section
     * @return a schema referencing the complete schema (or the original schema if 'schemasInComponents' = false)
     */
    fun addSchema(type: CapturedType, schema: Schema<*>): Schema<Any> {
        return addSchema(getIdentifyingName(type.kType!!.javaType), schema)
    }


    /**
     * Add the given schema for the given type to the components-section
     * @return a schema referencing the complete schema (or the original schema if 'schemasInComponents' = false)
     */
    fun addSchema(id: String, schema: Schema<*>): Schema<Any> {
        if (schemasInComponents) {
            if (!schemas.containsKey(id)) {
                schemas[id] = schema
            }
            return Schema<Any>().apply {
                `$ref` = asSchemaRef(id)
            }
        } else {
            @Suppress("UNCHECKED_CAST")
            return schema as Schema<Any>
        }
    }


    private fun getIdentifyingName(type: Type): String {
        return when (type) {
            is Class<*> -> if (canonicalNameObjectRefs) type.canonicalName else type.simpleName
            is ParameterizedType -> getIdentifyingName(type.rawType) + "<" + getIdentifyingName(type.actualTypeArguments.first()) + ">"
            is WildcardType -> getIdentifyingName(type.upperBounds.first())
            else -> throw Exception("Could not get identifying name from $type")
        }
    }


    /**
     * Add the given example with the given name to the components-section
     * @return the ref-string for the example
     */
    fun addExample(name: String, example: OpenApiExample): String {
        if (!examples.containsKey(name)) {
            examples[name] = example
            return asExampleRef(name)
        } else {
            if (isSameExample(examples[name]!!, example)) {
                return asExampleRef(name)
            } else {
                val key = asUniqueName(name, example)
                examples[key] = example
                return asExampleRef(key)
            }
        }
    }


    private fun isSameExample(a: OpenApiExample, b: OpenApiExample): Boolean {
        return a.value == b.value
                && a.description == b.description
                && a.summary == b.summary
    }


    private fun asUniqueName(name: String, example: OpenApiExample): String {
        return name + "#" + example.hashCode().toString(16)
    }


    private fun asSchemaRef(key: String) = "#/components/schemas/$key"


    private fun asExampleRef(key: String) = "#/components/examples/$key"

}
