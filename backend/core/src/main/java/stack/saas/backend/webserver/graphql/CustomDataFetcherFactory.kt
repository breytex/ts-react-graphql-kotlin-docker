package stack.saas.backend.webserver.graphql

import com.expedia.graphql.schema.extensions.deepName
import graphql.schema.DataFetcher
import graphql.schema.DataFetcherFactory
import graphql.schema.DataFetcherFactoryEnvironment
import stack.saas.backend.common.logger
import javax.inject.Inject

class CustomDataFetcherFactory @Inject constructor(fetchers: Map<Class<*>, @JvmSuppressWildcards DataFetcher<Any>>) : DataFetcherFactory<Any> {
    private val fetchersByName = mutableMapOf<String, DataFetcher<Any>>()

    init {
        fetchers.forEach { fetchersByName[it.key.simpleName] = it.value }
    }

    val log = logger(this::class)

    override fun get(environment: DataFetcherFactoryEnvironment?): DataFetcher<Any> {
        //Strip out possible `Input` and `!` suffixes added to by the SchemaGenerator
        val targetedTypeName = environment?.fieldDefinition?.type?.deepName?.removeSuffix("!")?.removeSuffix("Input")
        return fetchersByName[targetedTypeName]!!
    }

}