package stack.saas.backend.webserver.graphql

import graphql.ExecutionInput.newExecutionInput
import graphql.ExecutionResult
import graphql.GraphQL
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.web.Router
import io.vertx.reactivex.ext.web.handler.BodyHandler
import org.dataloader.DataLoaderRegistry
import stack.saas.backend.common.logger
import stack.saas.backend.common.vertx.Controller
import stack.saas.backend.common.vertx.json
import javax.inject.Inject
import javax.inject.Provider


class GraphQLController @Inject constructor(
        override val router: Router,
        val vertx: Vertx,
        graphQL: GraphQL,
        dataLoaderRegistry: Provider<DataLoaderRegistry>
) : Controller({

    val log = logger(this::class)

    router.route().handler(BodyHandler.create())
    router.route().handler { event ->
        val start = System.currentTimeMillis()
        vertx
                .rxExecuteBlocking<ExecutionResult>({
                    val query = event.bodyAsJson
                    log.debug("Processing query: $query")

                    val cmd = newExecutionInput()
                            .query(query.map["query"] as? String)
                            .operationName(query.map["operationName"] as? String)
                            .variables(query.map["variables"] as? Map<String, Any>)
                            .dataLoaderRegistry(dataLoaderRegistry.get())

                    val result = graphQL.execute(cmd)
                    it.complete(result)
                }, false)
                .map { it.toSpecification() }
                .doOnSuccess { log.info("##### GraphQuery finished in  ${System.currentTimeMillis() - start}ms") }
                .subscribe({
                    event.json(it)
                }, {
                    log.warn("GraphQL query failed.", it)
                    event.fail(500)
                })
    }
})