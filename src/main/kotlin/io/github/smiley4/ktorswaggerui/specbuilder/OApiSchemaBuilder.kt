package io.github.smiley4.ktorswaggerui.specbuilder

import io.github.smiley4.ktorswaggerui.CapturedType
import io.github.smiley4.ktorswaggerui.SwaggerUIPluginConfig
import io.swagger.v3.oas.models.media.Schema

/**
 * Builder for an OpenAPI Schema Object
 */
class OApiSchemaBuilder {

    private val jsonSchemaBuilder = OApiJsonSchemaBuilder()


    fun build(type: CapturedType, components: ComponentsContext, config: SwaggerUIPluginConfig): Schema<Any> {
        return jsonSchemaBuilder.build(type, components, config)
    }

}
