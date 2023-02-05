package io.github.smiley4.ktorswaggerui

import io.github.smiley4.ktorswaggerui.specbuilder.ApiSpecBuilder
import io.ktor.server.application.*
import io.ktor.server.application.hooks.*
import io.ktor.server.webjars.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlin.reflect.KType
import kotlin.reflect.typeOf

/**
 * This version must match the version of the gradle dependency
 */
internal const val SWAGGER_UI_WEBJARS_VERSION = "4.15.0"

val SwaggerUI = createApplicationPlugin(name = "SwaggerUI", createConfiguration = ::SwaggerUIPluginConfig) {

    var apiSpecJson = "{}"

    on(MonitoringEvent(ApplicationStarted)) { application ->
        if (application.pluginOrNull(Webjars) == null) {
            application.install(Webjars)
        }
        apiSpecJson = ApiSpecBuilder().build(application, pluginConfig)
    }

    SwaggerRouting(
        pluginConfig.getSwaggerUI(),
        application.environment.config,
        SWAGGER_UI_WEBJARS_VERSION,
    ) { apiSpecJson }.setup(application)

}

data class CapturedType(val kType: KType?, val descriptor: SerialDescriptor?) {
    fun parameter(index: Int): CapturedType {
        val paramKType = kType?.arguments?.get(index)?.type
        val paramDescriptor = kType?.let(::serialDescriptor)
        return CapturedType(paramKType, paramDescriptor)
    }
}

@PublishedApi
internal inline fun <reified TYPE> captureType(): CapturedType {
    val descriptor = try {
        serialDescriptor<TYPE>()
    } catch (_: Exception) { null }
    val kClass = typeOf<TYPE>()
    return CapturedType(kClass, descriptor)
    /*return if (kClass.isValue) {
        kClass.declaredMembers.first { it is KProperty<*> }.returnType.javaType
    } else {
        kClass.java
    }*/
}
