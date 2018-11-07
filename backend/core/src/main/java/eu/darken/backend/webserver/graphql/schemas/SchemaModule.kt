package eu.darken.backend.webserver.graphql.schemas

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import eu.darken.backend.common.graphql.FieldKey
import eu.darken.backend.webserver.graphql.schemas.hello.*
import graphql.schema.DataFetcher
import org.dataloader.BatchLoader


@Module
abstract class SchemaModule {

    @Binds @IntoSet abstract fun helloQ(q: HelloQuery): GraphQLQuery
    @Binds @IntoSet abstract fun helloM(m: HelloMutation): GraphQLMutation

    @Binds @IntoMap @FieldKey(HelloDetails::class)
    abstract fun provideHelloDetailsFetcher(fetcher: HelloDetailsDataFetcher): DataFetcher<Any>

    @Binds @IntoMap @FieldKey(HelloDetails::class)
    abstract fun provideHelloDetailsBatchLoader(loader: HelloDetailsBatchLoader): BatchLoader<*, *>

}