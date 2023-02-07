package io.github.smiley4.ktorswaggerui.tests

import io.github.smiley4.ktorswaggerui.SwaggerUIPluginConfig
import io.github.smiley4.ktorswaggerui.captureType
import io.github.smiley4.ktorswaggerui.specbuilder.ComponentsContext
import io.kotest.core.spec.style.StringSpec
import io.swagger.v3.oas.models.media.Schema
import java.math.BigDecimal

class PrimitiveArraysSchemaGenerationTests : StringSpec({

    "generate schema for byte-array" {
        getOApiSchemaBuilder().build(captureType<Array<Byte>>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "array"
            items = Schema<String>().apply {
                type = "integer"
                minimum = BigDecimal.valueOf(-128)
                maximum = BigDecimal.valueOf(127)
            }
        }
        getOApiSchemaBuilder().build(captureType<ByteArray>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "array"
            items = Schema<String>().apply {
                type = "integer"
                minimum = BigDecimal.valueOf(-128)
                maximum = BigDecimal.valueOf(127)
            }
        }
    }

    "generate schema for unsigned byte" {
        getOApiSchemaBuilder().build(captureType<Array<UByte>>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "array"
            items = Schema<String>().apply {
                type = "integer"
                minimum = BigDecimal.valueOf(0)
                maximum = BigDecimal.valueOf(255)
            }
        }
    }

    "generate schema for short-array" {
        getOApiSchemaBuilder().build(captureType<Array<Short>>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "array"
            items = Schema<String>().apply {
                type = "integer"
                minimum = BigDecimal.valueOf(-32768)
                maximum = BigDecimal.valueOf(32767)
            }
        }
        getOApiSchemaBuilder().build(captureType<ShortArray>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "array"
            items = Schema<String>().apply {
                type = "integer"
                minimum = BigDecimal.valueOf(-32768)
                maximum = BigDecimal.valueOf(32767)
            }
        }
    }

    "generate schema for unsigned short" {
        getOApiSchemaBuilder().build(captureType<Array<UShort>>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "array"
            items = Schema<String>().apply {
                type = "integer"
                minimum = BigDecimal.valueOf(0)
                maximum = BigDecimal.valueOf(65535)
            }
        }
    }

    "generate schema for integer-array" {
        getOApiSchemaBuilder().build(captureType<Array<Int>>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "array"
            items = Schema<String>().apply {
                type = "integer"
                format = "int32"
            }
        }
        getOApiSchemaBuilder().build(captureType<IntArray>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "array"
            items = Schema<String>().apply {
                type = "integer"
                format = "int32"
            }
        }
    }

    "generate schema for unsigned integer" {
        getOApiSchemaBuilder().build(captureType<Array<UInt>>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "array"
            items = Schema<String>().apply {
                type = "integer"
                minimum = BigDecimal.valueOf(0)
                maximum = BigDecimal.valueOf(4294967295)
            }
        }
    }

    "generate schema for long-array" {
        getOApiSchemaBuilder().build(captureType<Array<Long>>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "array"
            items = Schema<String>().apply {
                type = "integer"
                format = "int64"
            }
        }
        getOApiSchemaBuilder().build(captureType<LongArray>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "array"
            items = Schema<String>().apply {
                type = "integer"
                format = "int64"
            }
        }
    }

    "generate schema for unsigned long" {
        getOApiSchemaBuilder().build(captureType<Array<ULong>>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "array"
            items = Schema<String>().apply {
                type = "integer"
                minimum = BigDecimal.valueOf(0)
            }
        }
    }

    "generate schema for float-array" {
        getOApiSchemaBuilder().build(captureType<Array<Float>>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "array"
            items = Schema<String>().apply {
                type = "number"
                format = "float"
            }
        }
        getOApiSchemaBuilder().build(captureType<FloatArray>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "array"
            items = Schema<String>().apply {
                type = "number"
                format = "float"
            }
        }
    }

    "generate schema for double-array" {
        getOApiSchemaBuilder().build(captureType<Array<Double>>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "array"
            items = Schema<String>().apply {
                type = "number"
                format = "double"
            }
        }
        getOApiSchemaBuilder().build(captureType<DoubleArray>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "array"
            items = Schema<String>().apply {
                type = "number"
                format = "double"
            }
        }
    }

    "generate schema for character-array" {
        getOApiSchemaBuilder().build(captureType<Array<Char>>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "array"
            items = Schema<String>().apply {
                type = "string"
                minLength = 1
                maxLength = 1
            }
        }
    }

    "generate schema for string-array" {
        getOApiSchemaBuilder().build(captureType<Array<String>>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "array"
            items = Schema<String>().apply {
                type = "string"
            }
        }
    }

    "generate schema for boolean-array" {
        getOApiSchemaBuilder().build(captureType<Array<Boolean>>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "array"
            items = Schema<String>().apply {
                type = "boolean"
            }
        }
    }

})
