package io.github.smiley4.ktorswaggerui.dsl

import io.github.smiley4.ktorswaggerui.CapturedType
import kotlinx.serialization.StringFormat
import kotlinx.serialization.encodeToString

/**
 * Describes the base of a single request/response body.
 */
@OpenApiDslMarker
class OpenApiSimpleBody(
    /**
     * The type defining the schema used for the body.
     */
    val type: CapturedType?,
) : OpenApiBaseBody() {

    /**
     * id of a custom schema (alternative to 'type')
     */
    var customSchemaId: String? = null

    /**
     * Examples for this body
     */
    private val examples = mutableMapOf<String, OpenApiExample>()

    @PublishedApi
    internal fun addExample(name: String, example: OpenApiExample) {
        examples[name] = example
    }

    fun example(name: String, value: Any, block: OpenApiExample.() -> Unit) {
        addExample(name, OpenApiExample(value).apply(block))
    }

    inline fun <reified T: Any> serializedExample(name: String, value: T, json: StringFormat, block: OpenApiExample.() -> Unit = {}) {
        addExample(name, OpenApiExample(json.encodeToString(value)).apply(block))
    }

    fun example(name: String, value: Any) = example(name, value) {}

    fun getExamples(): Map<String, OpenApiExample> = examples

}
