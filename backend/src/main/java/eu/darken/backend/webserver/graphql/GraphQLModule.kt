package eu.darken.backend.webserver.graphql

import com.expedia.graphql.schema.SchemaGeneratorConfig
import com.expedia.graphql.toSchema
import dagger.Module
import dagger.Provides
import eu.darken.backend.AppModule
import eu.darken.backend.common.exts.logger
import eu.darken.backend.common.exts.toTopLevelObjectDefs
import eu.darken.backend.webserver.graphql.extensions.CustomSchemaGeneratorHooks
import eu.darken.backend.webserver.graphql.schemas.GraphQLMutation
import eu.darken.backend.webserver.graphql.schemas.GraphQLQuery
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import graphql.schema.idl.SchemaPrinter
import javax.inject.Singleton


@Module
class GraphQLModule {
    private val log = logger(AppModule::class)

    @Provides
    @Singleton
    fun provideSchema(customGens: CustomSchemaGeneratorHooks,
                      queries: Set<@JvmSuppressWildcards GraphQLQuery>,
                      mutations: Set<@JvmSuppressWildcards GraphQLMutation>): GraphQLSchema {
        val schemaConfig = SchemaGeneratorConfig(
                supportedPackages = listOf("eu.darken.backend.webserver.graphql"),
                hooks = customGens
        )

        val schema = toSchema(
                queries.toTopLevelObjectDefs(),
                mutations.toTopLevelObjectDefs(),
                schemaConfig)

        log.debug("Loaded GraphQL Schema:\n${SchemaPrinter().print(schema)}")
        return schema
    }

    @Provides
    @Singleton
    fun provideGraphQL(schema: GraphQLSchema): GraphQL {
        return GraphQL.newGraphQL(schema).build()
    }
}