package io.github.smiley4.ktorswaggerui.dsl

import io.github.smiley4.ktorswaggerui.CapturedType
import io.github.smiley4.ktorswaggerui.captureType

/**
 * Describes a single request/response body with multipart content.
 * See https://swagger.io/docs/specification/describing-request-body/multipart-requests/ for more info
 */
@OpenApiDslMarker
class OpenApiMultipartBody : OpenApiBaseBody() {

    private val parts = mutableListOf<OpenapiMultipartPart>()

    /**
     * One part of a multipart-body
     */
    fun part(name: String, type: CapturedType, block: OpenapiMultipartPart.() -> Unit) {
        parts.add(OpenapiMultipartPart(name, type).apply(block))
    }

    /**
     * One part of a multipart-body
     */
    fun part(name: String, type: CapturedType) = part(name, type) {}

    /**
     * One part of a multipart-body
     */
    inline fun <reified TYPE> part(name: String) = part(name, captureType<TYPE>())

    /**
     * One part of a multipart-body
     */
    inline fun <reified TYPE> part(name: String, noinline block: OpenapiMultipartPart.() -> Unit) =
        part(name, captureType<TYPE>(), block)

    /**
     * One part of a multipart-body
     */
    fun part(name: String, customSchemaId: String, block: OpenapiMultipartPart.() -> Unit) {
        parts.add(OpenapiMultipartPart(name, null).apply(block).apply {
            this.customSchemaId = customSchemaId
        })
    }

    /**
     * One part of a multipart-body
     */
    fun part(name: String, customSchemaId: String) = part(name, customSchemaId) {}

    fun getParts(): List<OpenapiMultipartPart> = parts

}
