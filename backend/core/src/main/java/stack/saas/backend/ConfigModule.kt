package stack.saas.backend

import dagger.Module
import dagger.Provides
import io.vertx.config.ConfigStoreOptions
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.config.ConfigRetrieverOptions
import io.vertx.reactivex.config.ConfigRetriever
import io.vertx.reactivex.core.Vertx
import stack.saas.backend.common.logger
import javax.inject.Named
import javax.inject.Singleton


@Module
class ConfigModule {
    private val log = logger(ConfigModule::class)

    @Provides
    @Singleton
    @Named("config")
    fun environmentConfig(vertx: Vertx): JsonObject {
        val envStore = ConfigStoreOptions().setType("env")
        val options = ConfigRetrieverOptions().addStore(envStore)
        val retriever = ConfigRetriever.create(vertx, options)
        return retriever.rxGetConfig()
                .doOnSuccess { log.info("Config: $it") }
                .blockingGet()
    }
}