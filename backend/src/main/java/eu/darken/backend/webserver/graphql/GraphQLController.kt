package eu.darken.backend.webserver.graphql

import com.expedia.graphql.schema.SchemaGeneratorConfig
import com.expedia.graphql.toSchema
import eu.darken.backend.common.Controller
import eu.darken.backend.common.exts.json
import eu.darken.backend.common.exts.logger
import eu.darken.backend.common.exts.toTopLevelObjectDefs
import eu.darken.backend.webserver.graphql.schemas.HelloMutation
import eu.darken.backend.webserver.graphql.schemas.HelloQuery
import graphql.ExecutionInput.newExecutionInput
import graphql.GraphQL.newGraphQL
import graphql.schema.idl.SchemaPrinter
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.web.Router
import io.vertx.reactivex.ext.web.handler.BodyHandler
import javax.inject.Inject


class GraphQLController @Inject constructor(override val router: Router, val vertx: Vertx
) : Controller({
    val log = logger(this::class)

    router.route().handler(BodyHandler.create())
    router.route().handler { event ->
        val jsonBody = event.bodyAsJson
        log.debug("RequestBody: $jsonBody")
        val query = jsonBody.getString("query")
        log.debug("Query: $query")

        val schemaConfig = SchemaGeneratorConfig(
                supportedPackages = listOf("eu.darken.backend.webserver.graphql")
        )

        val schema = toSchema(
                listOf(HelloQuery()).toTopLevelObjectDefs(),
                listOf(HelloMutation()).toTopLevelObjectDefs(),
                schemaConfig)

        log.debug(SchemaPrinter().print(schema))

        val graphQL = newGraphQL(schema).build()

        val executionInput = newExecutionInput().query(query)

        val specification = graphQL.execute(executionInput.build()).toSpecification()
        event.json(specification)
    }
})