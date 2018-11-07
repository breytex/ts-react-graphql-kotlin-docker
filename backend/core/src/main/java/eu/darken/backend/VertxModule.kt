package eu.darken.backend

import dagger.Module
import dagger.Provides
import eu.darken.backend.common.exts.logger
import io.vertx.core.spi.VerticleFactory
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.web.Router
import javax.inject.Singleton


@Module
class VertxModule {
    private val log = logger(VertxModule::class)

    @Provides
    @Singleton
    fun provideVertx(verticleFactory: VerticleFactory): Vertx {
        val vertx = Vertx.vertx()
        vertx.delegate.registerVerticleFactory(verticleFactory)
        vertx.exceptionHandler { log.error(it) }
        return vertx
    }

    @Provides
    @Singleton
    fun provideRxVertx(vertx: Vertx): io.vertx.core.Vertx {
        return vertx.delegate
    }

    @Provides
    fun provideRxRouter(vertx: Vertx): Router {
        return Router.router(vertx)
    }

    @Provides
    fun provideRouter(router: Router): io.vertx.ext.web.Router {
        return router.delegate
    }
}