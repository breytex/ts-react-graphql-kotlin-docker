package eu.darken.backend.webserver

import eu.darken.backend.Application
import eu.darken.backend.common.Controller
import eu.darken.backend.common.exts.OK
import eu.darken.backend.common.exts.logger
import eu.darken.backend.webserver.graphql.GraphQLController
import io.vertx.ext.web.handler.LoggerFormat
import io.vertx.reactivex.ext.web.Router
import io.vertx.reactivex.ext.web.handler.ErrorHandler
import io.vertx.reactivex.ext.web.handler.LoggerHandler
import javax.inject.Inject

class HttpController @Inject constructor(override val router: Router,
                                         private val graphQlController: GraphQLController
) : Controller({
    val log = logger(HttpController::class)

    router.route().failureHandler(ErrorHandler.create(Application.isDebug))
    if (Application.isDebug) router.route().handler(LoggerHandler.create(LoggerFormat.DEFAULT))

    router.route("/").handler { it.OK("test2331") }

    mountSubRouter("/api/graphql", graphQlController.create())

    log.info("Api setup done.")
})