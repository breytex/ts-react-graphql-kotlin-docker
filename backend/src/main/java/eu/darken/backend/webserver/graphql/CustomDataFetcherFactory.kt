package eu.darken.backend.webserver.graphql

import com.expedia.graphql.schema.extensions.deepName
import eu.darken.backend.common.exts.logger
import eu.darken.backend.webserver.graphql.schemas.hello.HelloDetailsDataFetcher
import graphql.schema.DataFetcher
import graphql.schema.DataFetcherFactory
import graphql.schema.DataFetcherFactoryEnvironment
import javax.inject.Inject

class CustomDataFetcherFactory @Inject constructor() : DataFetcherFactory<Any> {
    val log = logger(this::class)

    override fun get(environment: DataFetcherFactoryEnvironment?): DataFetcher<Any> {
        //Strip out possible `Input` and `!` suffixes added to by the SchemaGenerator
        val targetedTypeName = environment?.fieldDefinition?.type?.deepName?.removeSuffix("!")?.removeSuffix("Input")
        log.info("TargetedType: $targetedTypeName")
        return HelloDetailsDataFetcher()
    }

}