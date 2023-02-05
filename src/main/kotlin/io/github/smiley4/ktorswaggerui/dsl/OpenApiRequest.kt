package io.github.smiley4.ktorswaggerui.dsl

import io.github.smiley4.ktorswaggerui.CapturedType
import io.github.smiley4.ktorswaggerui.captureType


@OpenApiDslMarker
class OpenApiRequest {

    /**
     * A list of parameters that are applicable for this operation
     */
    private val parameters = mutableListOf<OpenApiRequestParameter>()

    /**
     * A path parameters that is applicable for this operation
     */
    fun pathParameter(name: String, type: CapturedType, block: OpenApiRequestParameter.() -> Unit) {
        parameters.add(OpenApiRequestParameter(name, type, OpenApiRequestParameter.Location.PATH)
            .apply { required = true }
            .apply(block))
    }

    /**
     * A path parameters that is applicable for this operation
     */
    inline fun <reified TYPE> pathParameter(name: String) = pathParameter(name, captureType<TYPE>()) {}

    /**
     * A path parameters that is applicable for this operation
     */
    inline fun <reified TYPE> pathParameter(name: String, noinline block: OpenApiRequestParameter.() -> Unit) =
        pathParameter(name, captureType<TYPE>(), block)

    /**
     * A query parameters that is applicable for this operation
     */
    fun queryParameter(name: String, type: CapturedType, block: OpenApiRequestParameter.() -> Unit) {
        parameters.add(OpenApiRequestParameter(name, type, OpenApiRequestParameter.Location.QUERY).apply(block))
    }

    /**
     * A query parameters that is applicable for this operation
     */
    inline fun <reified TYPE> queryParameter(name: String) = queryParameter(name, captureType<TYPE>()) {}

    /**
     * A query parameters that is applicable for this operation
     */
    inline fun <reified TYPE> queryParameter(name: String, noinline block: OpenApiRequestParameter.() -> Unit) =
        queryParameter(name, captureType<TYPE>(), block)

    /**
     * A header parameters that is applicable for this operation
     */
    fun headerParameter(name: String, type: CapturedType, block: OpenApiRequestParameter.() -> Unit) {
        parameters.add(OpenApiRequestParameter(name, type, OpenApiRequestParameter.Location.HEADER).apply(block))
    }

    /**
     * A header parameters that is applicable for this operation
     */
    inline fun <reified TYPE> headerParameter(name: String) = headerParameter(name, captureType<TYPE>()) {}

    /**
     * A header parameters that is applicable for this operation
     */
    inline fun <reified TYPE> headerParameter(name: String, noinline block: OpenApiRequestParameter.() -> Unit) =
        headerParameter(name, captureType<TYPE>(), block)


    fun getParameters(): List<OpenApiRequestParameter> = parameters


    private var body: OpenApiBaseBody? = null

    /**
     * The request body applicable for this operation
     */
    fun body(type: CapturedType, block: OpenApiSimpleBody.() -> Unit) {
        body = OpenApiSimpleBody(type).apply { required = true }.apply(block)
    }

    /**
     * The request body applicable for this operation
     */
    @JvmName("bodyGenericType")
    inline fun <reified TYPE> body(noinline block: OpenApiSimpleBody.() -> Unit) = body(captureType<TYPE>(), block)

    /**
     * The request body applicable for this operation
     */
    inline fun <reified TYPE> body() = body(captureType<TYPE>()) {}

    /**
     * The request body applicable for this operation
     */
    fun body(block: OpenApiSimpleBody.() -> Unit) {
        body = OpenApiSimpleBody(null).apply(block)
    }

    /**
     * The body returned with this response
     */
    fun body(customSchemaId: String, block: OpenApiSimpleBody.() -> Unit) {
        body = OpenApiSimpleBody(null).apply(block).apply {
            this.customSchemaId = customSchemaId
        }
    }

    /**
     * The body returned with this response
     */
    fun body(customSchemaId: String) = body(customSchemaId) {}

    /**
     * The multipart-body returned with this response
     */
    fun multipartBody(block: OpenApiMultipartBody.() -> Unit) {
        body = OpenApiMultipartBody().apply(block)
    }

    /**
     * Set the body of this request. Intended for internal use.
     */
    fun setBody(body: OpenApiBaseBody?) {
        this.body = body
    }

    fun getBody() = body

}
