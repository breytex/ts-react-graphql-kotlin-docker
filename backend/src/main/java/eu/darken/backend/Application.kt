package eu.darken.backend

import eu.darken.backend.common.exts.logger
import eu.darken.backend.webserver.HttpVerticle
import eu.darken.backend.webserver.HttpVerticleModule
import io.vertx.core.DeploymentOptions
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.core.logging.SLF4JLogDelegateFactory
import io.vertx.reactivex.core.Vertx
import javax.inject.Inject
import javax.inject.Named


class Application @Inject constructor(private val vertx: Vertx, @Named("ENV") private val config: JsonObject) {
    val log = logger(Application::class)
    fun launch() {
        Application.isDebug = config.getString("VERTX_DEBUG", "true")!!.toBoolean()
        log.info("Debug: ${Application.isDebug}")
        vertx.deployVerticle("dagger:${HttpVerticle::class.java.name}", DeploymentOptions().apply {
            this.config = config
        })
    }

    companion object {
        var isDebug = false

        @JvmStatic
        fun main(args: Array<String>) {
            System.setProperty(LoggerFactory.LOGGER_DELEGATE_FACTORY_CLASS_NAME, SLF4JLogDelegateFactory::class.java.name)
            DaggerAppComponent.builder()
                    .vertxModule(VertxModule)
                    .mongoModule(MongoModule)
                    .appModule(AppModule)
                    .httpVerticleModule(HttpVerticleModule)
                    .daggerVerticleFactoryModule(DaggerVerticleFactoryModule)
                    .build()
                    .application()
                    .launch()
        }
    }
}