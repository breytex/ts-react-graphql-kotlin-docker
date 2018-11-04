package eu.darken.backend

import dagger.Component
import eu.darken.backend.webserver.HttpVerticleModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    (VertxModule::class), (MongoModule::class), (DaggerVerticleFactoryModule::class), (AppModule::class), (HttpVerticleModule::class)
])
interface AppComponent {
    fun application(): Application
}