package io.github.smiley4.ktorswaggerui.dsl

import io.github.smiley4.ktorswaggerui.CapturedType
import io.github.smiley4.ktorswaggerui.captureType

/**
 * A container for the expected responses of an operation. The container maps a HTTP response code to the expected response.
 * A response code can only have one response object.
 */
@OpenApiDslMarker
class OpenApiResponse(val statusCode: String) {

    /**
     * A short description of the response
     */
    var description: String? = null

    private val headers = mutableMapOf<String, OpenApiHeader>()

    /**
     * Possible headers returned with this response
     */
    fun header(name: String, type: CapturedType, block: OpenApiHeader.() -> Unit) {
        headers[name] = OpenApiHeader().apply(block).apply {
            this.type = type
        }
    }

    /**
     * Possible headers returned with this response
     */
    inline fun <reified TYPE> header(name: String) = header(name, captureType<TYPE>()) {}

    /**
     * Possible headers returned with this response
     */
    inline fun <reified TYPE> header(name: String, noinline block: OpenApiHeader.() -> Unit) =
        header(name, captureType<TYPE>(), block)

    fun getHeaders(): Map<String, OpenApiHeader> = headers


    private var body: OpenApiBaseBody? = null

    /**
     * The body returned with this response
     */
    fun body(type: CapturedType, block: OpenApiSimpleBody.() -> Unit) {
        body = OpenApiSimpleBody(type).apply(block)
    }

    /**
     * The body returned with this response
     */
    @JvmName("bodyGenericType")
    inline fun <reified TYPE> body(noinline block: OpenApiSimpleBody.() -> Unit) =
        body(captureType<TYPE>(), block)

    /**
     * The body returned with this response
     */
    inline fun <reified TYPE> body() = body(captureType<TYPE>()) {}

    /**
     * The body returned with this response
     */
    fun body(block: OpenApiSimpleBody.() -> Unit) {
        body = OpenApiSimpleBody(null).apply(block)
    }

    /**
     * The body returned with this response
     */
    fun body(schemaUrl: String, block: OpenApiSimpleBody.() -> Unit) {
        body = OpenApiSimpleBody(null).apply(block).apply {
            customSchemaId = schemaUrl
        }
    }

    /**
     * The body returned with this response
     */
    fun body(schemaUrl: String) = body(schemaUrl) {}


    fun getBody() = body

}
