package eu.darken.backend

import dagger.Module
import dagger.Provides
import eu.darken.backend.common.exts.logger
import io.vertx.config.ConfigStoreOptions
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.config.ConfigRetrieverOptions
import io.vertx.reactivex.config.ConfigRetriever
import io.vertx.reactivex.core.Vertx
import javax.inject.Named
import javax.inject.Singleton


@Module
class ConfigModule {
    private val log = logger(ConfigModule::class)

    @Provides
    @Singleton
    @Named("config")
    fun environmentConfig(vertx: Vertx): JsonObject {
        val envStore = ConfigStoreOptions().setType("env").setConfig(JsonObject().put("raw-data", true))
        val options = ConfigRetrieverOptions().addStore(envStore)
        val retriever = ConfigRetriever.create(vertx, options)
        return retriever.rxGetConfig()
                .doOnSuccess { log.info("Config: $it") }
                .blockingGet()
    }
}