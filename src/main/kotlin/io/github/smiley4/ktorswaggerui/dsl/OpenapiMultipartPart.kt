package io.github.smiley4.ktorswaggerui.dsl

import io.github.smiley4.ktorswaggerui.CapturedType
import io.github.smiley4.ktorswaggerui.captureType
import io.ktor.http.*
import kotlin.collections.set

/**
 * Describes one section of a multipart-body.
 * See https://swagger.io/docs/specification/describing-request-body/multipart-requests/ for more info
 */
@OpenApiDslMarker
class OpenapiMultipartPart(
    /**
     * The name of this part
     */
    val name: String,

    val type: CapturedType?
) {

    /**
     * id of a custom schema (alternative to 'type')
     */
    var customSchemaId: String? = null

    /**
     * Set a specific content type for this part
     */
    var mediaTypes: Collection<ContentType> = setOf()

    private val headers = mutableMapOf<String, OpenApiHeader>()

    /**
     * Possible headers for this part
     */
    fun header(name: String, type: CapturedType, block: OpenApiHeader.() -> Unit) {
        headers[name] = OpenApiHeader().apply(block).apply {
            this.type = type
        }
    }

    /**
     * Possible headers for this part
     */
    inline fun <reified TYPE> header(name: String) = header(name, captureType<TYPE>()) {}

    /**
     * Possible headers for this part
     */
    inline fun <reified TYPE> header(name: String, noinline block: OpenApiHeader.() -> Unit) =
        header(name, captureType<TYPE>(), block)

    fun getHeaders(): Map<String, OpenApiHeader> = headers

}
