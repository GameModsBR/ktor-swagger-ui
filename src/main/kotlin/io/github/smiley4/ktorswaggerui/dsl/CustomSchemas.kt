package io.github.smiley4.ktorswaggerui.dsl

import io.github.smiley4.ktorswaggerui.CapturedType
import io.swagger.v3.oas.models.media.Schema

@OpenApiDslMarker
class CustomSchemas {

    private var jsonSchemaBuilder: ((type: CapturedType) -> String?)? = null


    /**
     * Custom builder for building json-schemas from a given type. Return null to not use this builder for the given type.
     */
    fun jsonSchemaBuilder(builder: (type: CapturedType) -> String?) {
        jsonSchemaBuilder = builder
    }

    fun getJsonSchemaBuilder() = jsonSchemaBuilder


    private val customSchemas = mutableMapOf<String, BaseCustomSchema>()


    /**
     * Define the json-schema for an object/body with the given id
     */
    fun json(id: String, provider: () -> String) {
        customSchemas[id] = CustomJsonSchema(provider)
    }


    /**
     * Define the [Schema] for an object/body with the given id
     */
    fun openApi(id: String, provider: () -> Schema<Any>) {
        customSchemas[id] = CustomOpenApiSchema(provider)
    }


    /**
     * Define the external url for an object/body with the given id
     */
    fun remote(id: String, url: String) {
        customSchemas[id] = RemoteSchema(url)
    }

    fun getSchema(id: String): BaseCustomSchema? = customSchemas[id]


}


sealed class BaseCustomSchema

class CustomJsonSchema(val provider: () -> String) : BaseCustomSchema()

class CustomOpenApiSchema(val provider: () -> Schema<Any>) : BaseCustomSchema()

class RemoteSchema(val url: String) : BaseCustomSchema()
