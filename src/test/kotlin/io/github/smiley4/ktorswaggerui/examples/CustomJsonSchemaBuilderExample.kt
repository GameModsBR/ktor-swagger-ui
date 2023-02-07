package io.github.smiley4.ktorswaggerui.examples

import com.github.victools.jsonschema.generator.*
import com.github.victools.jsonschema.module.jackson.JacksonModule
import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.github.smiley4.ktorswaggerui.dsl.get
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.lang.reflect.Type
import kotlin.reflect.javaType

/**
 * An example for building custom json-schemas
 */
@OptIn(ExperimentalStdlibApi::class)
fun main() {

    data class MyRequestData(
        val someText: String,
        val someBoolean: Boolean
    )


    data class MyResponseData(
        val someText: String,
        val someNumber: Long
    )


    embeddedServer(Netty, port = 8080, host = "localhost") {

        install(SwaggerUI) {
            schemas {
                jsonSchemaBuilder { type ->
                    // custom converter from the given 'type' to a json-schema
                    typeToJsonSchema(type.kType!!.javaType)
                }
            }
        }

        routing {
            get("something", {
                request {
                    body<MyResponseData>()
                }
                response {
                    HttpStatusCode.OK to {
                        body<MyRequestData>()
                    }
                }
            }) {
                val text = call.receive<MyRequestData>().someText
                call.respond(HttpStatusCode.OK, MyResponseData(text, 42))
            }
        }

    }.start(wait = true)
}


fun typeToJsonSchema(type: Type): String {
    return SchemaGenerator(
        SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2019_09, OptionPreset.PLAIN_JSON)
            .with(JacksonModule())
            .without(Option.DEFINITIONS_FOR_ALL_OBJECTS)
            .with(Option.INLINE_ALL_SCHEMAS)
            .with(Option.EXTRA_OPEN_API_FORMAT_VALUES)
            .with(Option.ALLOF_CLEANUP_AT_THE_END)
            .build()
    )
        .generateSchema(type)
        .toString()
}
