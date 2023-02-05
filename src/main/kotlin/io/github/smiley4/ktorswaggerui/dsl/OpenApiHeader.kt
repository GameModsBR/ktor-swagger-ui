package io.github.smiley4.ktorswaggerui.dsl

import io.github.smiley4.ktorswaggerui.CapturedType


@OpenApiDslMarker
class OpenApiHeader {

    /**
     * A description of the header
     */
    var description: String? = null


    /**
     * The schema of the header
     */
    var type: CapturedType? = null


    /**
     * Determines whether this header is mandatory
     */
    var required: Boolean? = null


    /**
     * Specifies that a header is deprecated and SHOULD be transitioned out of usage
     */
    var deprecated: Boolean? = null

}
