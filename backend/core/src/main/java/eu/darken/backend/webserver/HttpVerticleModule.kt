package eu.darken.backend.webserver

import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import io.vertx.core.Verticle

@Module
class HttpVerticleModule {

    @Provides
    @IntoMap
    @ClassKey(HttpVerticle::class)
    fun provideHttpVerticle(httpController: HttpController): Verticle = HttpVerticle(httpController)
}