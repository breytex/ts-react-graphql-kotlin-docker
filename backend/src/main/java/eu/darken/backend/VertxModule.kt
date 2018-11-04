package eu.darken.backend

import dagger.Module
import dagger.Provides
import eu.darken.backend.common.exts.logger
import io.reactivex.Single
import io.vertx.config.ConfigStoreOptions
import io.vertx.core.json.JsonObject
import io.vertx.core.spi.VerticleFactory
import io.vertx.kotlin.config.ConfigRetrieverOptions
import io.vertx.reactivex.config.ConfigRetriever
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.web.Router
import javax.inject.Named
import javax.inject.Singleton


@Module
object VertxModule {
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

    @Provides
    @Singleton
    @Named("ENV")
    fun environmentConfig(vertx: Vertx): JsonObject {
        val envStore = ConfigStoreOptions().setType("env").setConfig(JsonObject().put("raw-data", true))
        val options = ConfigRetrieverOptions().addStore(envStore)
        val retriever = ConfigRetriever.create(vertx, options)
        return Single
                .create<JsonObject> {
                    retriever.getConfig { event ->
                        when {
                            event.failed() -> it.onError(event.cause())
                            else -> it.onSuccess(event.result())
                        }
                    }
                }
                .doOnSuccess { log.info("Config: $it") }
                .blockingGet()
    }
}