package stack.saas.backend

import dagger.Component
import stack.saas.backend.webserver.HttpVerticleModule
import stack.saas.backend.webserver.graphql.GraphQLModule
import stack.saas.backend.webserver.graphql.components.ComponentModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    VertxModule::class,
    MongoModule::class,
    DaggerVerticleFactoryModule::class,
    HttpVerticleModule::class,
    GraphQLModule::class,
    ComponentModule::class,
    ConfigModule::class
])
interface AppComponent {
    fun application(): Application

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
    }
}