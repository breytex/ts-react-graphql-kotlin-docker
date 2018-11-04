package eu.darken.backend.webserver.graphql.schemas

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet
import eu.darken.backend.webserver.graphql.schemas.hello.HelloMutation
import eu.darken.backend.webserver.graphql.schemas.hello.HelloQuery


@Module
abstract class SchemaModule {

    @Binds @IntoSet abstract fun helloQ(q: HelloQuery): GraphQLQuery
    @Binds @IntoSet abstract fun helloM(m: HelloMutation): GraphQLMutation
}