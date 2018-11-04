package eu.darken.backend.webserver

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import io.vertx.core.Verticle

@Module
class HttpVerticleModule {

    @Provides
    @IntoMap
    @StringKey("eu.darken.backend.webserver.HttpVerticle")
    fun provideHttpVerticle(httpController: HttpController): Verticle = HttpVerticle(httpController)
}