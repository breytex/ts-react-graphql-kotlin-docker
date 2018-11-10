package stack.saas.backend.webserver

import io.vertx.ext.web.handler.LoggerFormat
import io.vertx.reactivex.ext.web.Router
import io.vertx.reactivex.ext.web.handler.ErrorHandler
import io.vertx.reactivex.ext.web.handler.LoggerHandler
import stack.saas.backend.Application
import stack.saas.backend.common.logger
import stack.saas.backend.common.vertx.Controller
import stack.saas.backend.common.vertx.OK
import stack.saas.backend.webserver.graphql.GraphQLController
import javax.inject.Inject

class HttpController @Inject constructor(override val router: Router,
                                         private val graphQlController: GraphQLController
) : Controller({
    val log = logger(HttpController::class)

    router.route().failureHandler(ErrorHandler.create(Application.isDebug))
    if (Application.isDebug) router.route().handler(LoggerHandler.create(LoggerFormat.DEFAULT))

    router.route("/").handler { it.OK("Hello") }

    mountSubRouter("/api/graphql", graphQlController.create())

    log.info("Api setup done.")
})