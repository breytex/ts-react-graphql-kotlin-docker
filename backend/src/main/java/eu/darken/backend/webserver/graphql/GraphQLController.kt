package eu.darken.backend.webserver.graphql

import eu.darken.backend.common.Controller
import eu.darken.backend.common.exts.json
import graphql.ExecutionInput.newExecutionInput
import graphql.ExecutionResult
import graphql.GraphQL
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.web.Router
import io.vertx.reactivex.ext.web.handler.BodyHandler
import javax.inject.Inject


class GraphQLController @Inject constructor(
        override val router: Router,
        val vertx: Vertx,
        graphQL: GraphQL
) : Controller({

    router.route().handler(BodyHandler.create())
    router.route().handler { event ->
        vertx
                .rxExecuteBlocking<ExecutionResult>({
                    val cmd = newExecutionInput().query(event.bodyAsJson.map["query"] as String)
                    val result = graphQL.execute(cmd)
                    it.complete(result)
                }, false)
                .map { it.toSpecification() }
                .subscribe({ event.json(it) }, { event.fail(500) })
    }
})