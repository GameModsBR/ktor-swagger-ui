package io.github.smiley4.ktorswaggerui.tests

import io.github.smiley4.ktorswaggerui.SwaggerUIPluginConfig
import io.github.smiley4.ktorswaggerui.captureType
import io.github.smiley4.ktorswaggerui.specbuilder.ComponentsContext
import io.kotest.core.spec.style.StringSpec
import java.math.BigDecimal

class PrimitiveSchemaGenerationTests : StringSpec({

    "generate schema for byte" {
        getOApiSchemaBuilder().build(captureType<Byte>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "integer"
            minimum = BigDecimal.valueOf(-128)
            maximum = BigDecimal.valueOf(127)
        }
    }

    "generate schema for unsigned byte" {
        getOApiSchemaBuilder().build(captureType<UByte>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "integer"
            minimum = BigDecimal.valueOf(0)
            maximum = BigDecimal.valueOf(255)
        }
    }

    "generate schema for short" {
        getOApiSchemaBuilder().build(captureType<Short>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "integer"
            minimum = BigDecimal.valueOf(-32768)
            maximum = BigDecimal.valueOf(32767)
        }
    }

    "generate schema for unsigned short" {
        getOApiSchemaBuilder().build(captureType<UShort>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "integer"
            minimum = BigDecimal.valueOf(0)
            maximum = BigDecimal.valueOf(65535)
        }
    }

    "generate schema for integer" {
        getOApiSchemaBuilder().build(captureType<Int>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "integer"
            format = "int32"
        }
    }

    "generate schema for unsigned integer" {
        getOApiSchemaBuilder().build(captureType<UInt>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "integer"
            minimum = BigDecimal.valueOf(0)
            maximum = BigDecimal.valueOf(4294967295)
        }
    }

    "generate schema for long" {
        getOApiSchemaBuilder().build(captureType<Long>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "integer"
            format = "int64"
        }
    }

    "generate schema for unsigned long" {
        getOApiSchemaBuilder().build(captureType<ULong>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "integer"
            minimum = BigDecimal.valueOf(0)
        }
    }

    "generate schema for float" {
        getOApiSchemaBuilder().build(captureType<Float>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "number"
            format = "float"
        }
    }

    "generate schema for double" {
        getOApiSchemaBuilder().build(captureType<Double>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "number"
            format = "double"
        }
    }

    "generate schema for character" {
        getOApiSchemaBuilder().build(captureType<Char>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "string"
            minLength = 1
            maxLength = 1
        }
    }

    "generate schema for string" {
        getOApiSchemaBuilder().build(captureType<String>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "string"
        }
    }

    "generate schema for boolean" {
        getOApiSchemaBuilder().build(captureType<Boolean>(), ComponentsContext.NOOP, SwaggerUIPluginConfig()) shouldBeSchema {
            type = "boolean"
        }
    }

})
