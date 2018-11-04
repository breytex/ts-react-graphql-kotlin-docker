package eu.darken.backend.webserver.graphql

import eu.darken.backend.common.Controller
import eu.darken.backend.common.exts.json
import eu.darken.backend.common.exts.logger
import graphql.ExecutionInput.newExecutionInput
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

    val log = logger(this::class)

    router.route().handler(BodyHandler.create())
    router.route().handler { event ->
        val jsonBody = event.bodyAsJson
        val query = jsonBody.getString("query")
        log.debug("Query: $query")

        val executionInput = newExecutionInput().query(query)

        val specification = graphQL.execute(executionInput.build()).toSpecification()
        event.json(specification)
    }
})