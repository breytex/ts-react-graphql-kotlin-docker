package stack.saas.backend.webserver.graphql

import com.expedia.graphql.schema.SchemaGeneratorConfig
import com.expedia.graphql.toSchema
import dagger.Module
import dagger.Provides
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import graphql.schema.idl.SchemaPrinter
import org.dataloader.BatchLoader
import org.dataloader.DataLoader
import org.dataloader.DataLoaderRegistry
import stack.saas.backend.MoshiModule
import stack.saas.backend.common.graphql.GraphQLMutation
import stack.saas.backend.common.graphql.GraphQLQuery
import stack.saas.backend.common.graphql.toTopLevelObjectDefs
import stack.saas.backend.common.logger
import stack.saas.backend.webserver.graphql.extensions.CustomSchemaGeneratorHooks
import javax.inject.Singleton


@Module
class GraphQLModule {
    private val log = logger(MoshiModule::class)

    @Provides
    @Singleton
    fun provideSchema(customGens: CustomSchemaGeneratorHooks,
                      queries: Set<@JvmSuppressWildcards GraphQLQuery>,
                      mutations: Set<@JvmSuppressWildcards GraphQLMutation>,
                      dataFetcherFactory: CustomDataFetcherFactory): GraphQLSchema {
        val schemaConfig = SchemaGeneratorConfig(
                supportedPackages = listOf("stack.saas.backend.webserver.graphql"),
                hooks = customGens,
                dataFetcherFactory = dataFetcherFactory
        )

        val schema = toSchema(queries.toTopLevelObjectDefs(), mutations.toTopLevelObjectDefs(), schemaConfig)

        log.debug("Loaded GraphQL Schema:\n${SchemaPrinter().print(schema)}")
        return schema
    }

    @Provides
    @Singleton
    fun provideGraphQL(schema: GraphQLSchema): GraphQL {
        return GraphQL
                .newGraphQL(schema)
                .build()
    }

    @Provides
    fun provideDataloaderRegistry(loaders: @JvmSuppressWildcards Map<Class<*>, BatchLoader<*, *>>): DataLoaderRegistry {
        val registry = DataLoaderRegistry()
        loaders.forEach { registry.register(it.key.simpleName, DataLoader.newDataLoader(it.value)) }
        return registry
    }
}