package stack.saas.backend.webserver.graphql.components

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import graphql.schema.DataFetcher
import org.dataloader.BatchLoader
import stack.saas.backend.common.graphql.FieldKey
import stack.saas.backend.common.graphql.GraphQLMutation
import stack.saas.backend.common.graphql.GraphQLQuery
import stack.saas.backend.webserver.graphql.components.hello.HelloDetailsBatchLoader
import stack.saas.backend.webserver.graphql.components.hello.HelloDetailsDataFetcher
import stack.saas.backend.webserver.graphql.components.hello.schema.HelloDetails
import stack.saas.backend.webserver.graphql.components.hello.schema.HelloMutation
import stack.saas.backend.webserver.graphql.components.hello.schema.HelloQuery


@Module
abstract class ComponentModule {

    @Binds @IntoSet abstract fun helloQ(q: HelloQuery): GraphQLQuery
    @Binds @IntoSet abstract fun helloM(m: HelloMutation): GraphQLMutation

    @Binds @IntoMap @FieldKey(HelloDetails::class)
    abstract fun provideHelloDetailsFetcher(fetcher: HelloDetailsDataFetcher): DataFetcher<Any>

    @Binds @IntoMap @FieldKey(HelloDetails::class)
    abstract fun provideHelloDetailsBatchLoader(loader: HelloDetailsBatchLoader): BatchLoader<*, *>

}