package eu.darken.backend.webserver.graphql

import eu.darken.backend.common.Controller
import eu.darken.backend.common.exts.json
import eu.darken.backend.common.exts.logger
import graphql.ExecutionInput.newExecutionInput
import graphql.ExecutionResult
import graphql.GraphQL
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.web.Router
import io.vertx.reactivex.ext.web.handler.BodyHandler
import org.dataloader.DataLoaderRegistry
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
                    val cmd = newExecutionInput()
                            .query(event.bodyAsJson.map["query"] as String)
                            .dataLoaderRegistry(dataLoaderRegistry.get())
                    val result = graphQL.execute(cmd)
                    it.complete(result)
                }, false)
                .map { it.toSpecification() }
                .doOnSuccess { log.info("##### GraphQuery finished in  ${System.currentTimeMillis() - start}ms") }
                .subscribe({ event.json(it) }, { event.fail(500) })
    }
})