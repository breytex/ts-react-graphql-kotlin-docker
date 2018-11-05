package eu.darken.backend.webserver.graphql.schemas

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import eu.darken.backend.common.FetcherKey
import eu.darken.backend.webserver.graphql.schemas.hello.HelloDetails
import eu.darken.backend.webserver.graphql.schemas.hello.HelloDetailsDataFetcher
import eu.darken.backend.webserver.graphql.schemas.hello.HelloMutation
import eu.darken.backend.webserver.graphql.schemas.hello.HelloQuery
import graphql.schema.DataFetcher


@Module
abstract class SchemaModule {

    @Binds @IntoSet abstract fun helloQ(q: HelloQuery): GraphQLQuery
    @Binds @IntoSet abstract fun helloM(m: HelloMutation): GraphQLMutation

    @Binds @IntoMap @FetcherKey(HelloDetails::class)
    abstract fun provideHelloDetailsFetcher(fetcher: HelloDetailsDataFetcher): DataFetcher<Any>


}