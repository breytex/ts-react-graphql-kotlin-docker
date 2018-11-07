package eu.darken.backend

import dagger.Component
import eu.darken.backend.webserver.HttpVerticleModule
import eu.darken.backend.webserver.graphql.GraphQLModule
import eu.darken.backend.webserver.graphql.schemas.SchemaModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    VertxModule::class,
    MongoModule::class,
    DaggerVerticleFactoryModule::class,
    MoshiModule::class,
    HttpVerticleModule::class,
    GraphQLModule::class,
    SchemaModule::class,
    ConfigModule::class
])
interface AppComponent {
    fun application(): Application

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
    }
}