package io.github.smiley4.ktorswaggerui.dsl

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.util.*
import io.ktor.util.pipeline.*

class DocumentedRouteSelector(val documentation: OpenApiRoute) : RouteSelector() {

    companion object {
        private var includeDocumentedRouteInRouteToString = false
        fun setIncludeDocumentedRouteInRouteToString(include: Boolean) {
            includeDocumentedRouteInRouteToString = include
        }
    }

    override fun evaluate(context: RoutingResolveContext, segmentIndex: Int) = RouteSelectorEvaluation.Transparent

    override fun toString() = if (includeDocumentedRouteInRouteToString) super.toString() else ""
}

@KtorDsl
fun Route.documentation(
    documentation: OpenApiRoute.() -> Unit = { },
    build: Route.() -> Unit
): Route {
    val documentedRoute = createChild(DocumentedRouteSelector(OpenApiRoute().apply(documentation)))
    documentedRoute.build()
    return documentedRoute
}

//============================//
//           ROUTING          //
//============================//

@KtorDsl
fun Route.route(
    path: String,
    builder: OpenApiRoute.() -> Unit = { },
    build: Route.() -> Unit
): Route {
    return documentation(builder) { route(path, build) }
}

@KtorDsl
fun Route.route(
    path: String,
    method: HttpMethod,
    builder: OpenApiRoute.() -> Unit = { },
    build: Route.() -> Unit
): Route {
    return documentation(builder) { route(path, method, build) }
}

@KtorDsl
fun Route.method(
    method: HttpMethod,
    builder: OpenApiRoute.() -> Unit = { },
    body: Route.() -> Unit
): Route {
    return documentation(builder) { method(method, body) }
}

//============================//
//             GET            //
//============================//

@KtorDsl
fun Route.get(
    path: String,
    builder: OpenApiRoute.() -> Unit = { },
    body: PipelineInterceptor<Unit, ApplicationCall>
): Route {
    return documentation(builder) { get(path, body) }
}

@KtorDsl
fun Route.get(
    builder: OpenApiRoute.() -> Unit = { },
    body: PipelineInterceptor<Unit, ApplicationCall>
): Route {
    return documentation(builder) { get(body) }
}

//============================//
//            POST            //
//============================//

@KtorDsl
fun Route.post(
    path: String,
    builder: OpenApiRoute.() -> Unit = { },
    body: PipelineInterceptor<Unit, ApplicationCall>
): Route {
    return documentation(builder) { post(path, body) }
}

@KtorDsl
@JvmName("postTyped")
inline fun <reified R : Any> Route.post(
    noinline builder: OpenApiRoute.() -> Unit = { },
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.(R) -> Unit
): Route {
    return documentation(builder) { post(body) }
}

@KtorDsl
@JvmName("postTypedPath")
inline fun <reified R : Any> Route.post(
    path: String,
    noinline builder: OpenApiRoute.() -> Unit = { },
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.(R) -> Unit
): Route {
    return documentation(builder) { post(path, body) }
}


@KtorDsl
fun Route.post(
    builder: OpenApiRoute.() -> Unit = { },
    body: PipelineInterceptor<Unit, ApplicationCall>
): Route {
    return documentation(builder) { post(body) }
}

//============================//
//             PUT            //
//============================//

@KtorDsl
fun Route.put(
    path: String,
    builder: OpenApiRoute.() -> Unit = { },
    body: PipelineInterceptor<Unit, ApplicationCall>
): Route {
    return documentation(builder) { put(path, body) }
}

@KtorDsl
fun Route.put(
    builder: OpenApiRoute.() -> Unit = { },
    body: PipelineInterceptor<Unit, ApplicationCall>
): Route {
    return documentation(builder) { put(body) }
}

@KtorDsl
@JvmName("putTyped")
inline fun <reified R : Any> Route.put(
    noinline builder: OpenApiRoute.() -> Unit = { },
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.(R) -> Unit
): Route {
    return documentation(builder) { put(body) }
}

@KtorDsl
@JvmName("putTypedPath")
inline fun <reified R : Any> Route.put(
    path: String,
    noinline builder: OpenApiRoute.() -> Unit = { },
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.(R) -> Unit
): Route {
    return documentation(builder) { put(path, body) }
}

//============================//
//           DELETE           //
//============================//

@KtorDsl
fun Route.delete(
    path: String,
    builder: OpenApiRoute.() -> Unit = { },
    body: PipelineInterceptor<Unit, ApplicationCall>
): Route {
    return documentation(builder) { delete(path, body) }
}

@KtorDsl
fun Route.delete(
    builder: OpenApiRoute.() -> Unit = { },
    body: PipelineInterceptor<Unit, ApplicationCall>
): Route {
    return documentation(builder) { delete(body) }
}

//============================//
//            PATCH           //
//============================//

@KtorDsl
fun Route.patch(
    path: String,
    builder: OpenApiRoute.() -> Unit = { },
    body: PipelineInterceptor<Unit, ApplicationCall>
): Route {
    return documentation(builder) { patch(path, body) }
}

@KtorDsl
fun Route.patch(
    builder: OpenApiRoute.() -> Unit = { },
    body: PipelineInterceptor<Unit, ApplicationCall>
): Route {
    return documentation(builder) { patch(body) }
}

@JvmName("patchTyped")
inline fun <reified R : Any> Route.patch(
    noinline builder: OpenApiRoute.() -> Unit = { },
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.(R) -> Unit
): Route {
    return documentation(builder) { patch(body) }

}

@JvmName("patchTypedPath")
@KtorDsl
inline fun <reified R : Any> Route.patch(
    path: String,
    noinline builder: OpenApiRoute.() -> Unit = { },
    crossinline body: suspend PipelineContext<Unit, ApplicationCall>.(R) -> Unit
): Route {
    return documentation(builder) { patch(path, body) }
}

//============================//
//           OPTIONS          //
//============================//

@KtorDsl
fun Route.options(
    path: String,
    builder: OpenApiRoute.() -> Unit = { },
    body: PipelineInterceptor<Unit, ApplicationCall>
): Route {
    return documentation(builder) { options(path, body) }
}

@KtorDsl
fun Route.options(
    builder: OpenApiRoute.() -> Unit = { },
    body: PipelineInterceptor<Unit, ApplicationCall>
): Route {
    return documentation(builder) { options(body) }
}

//============================//
//            HEAD            //
//============================//

@KtorDsl
fun Route.head(
    path: String,
    builder: OpenApiRoute.() -> Unit = { },
    body: PipelineInterceptor<Unit, ApplicationCall>
): Route {
    return documentation(builder) { head(path, body) }
}

@KtorDsl
fun Route.head(
    builder: OpenApiRoute.() -> Unit = { },
    body: PipelineInterceptor<Unit, ApplicationCall>
): Route {
    return documentation(builder) { head(body) }
}
