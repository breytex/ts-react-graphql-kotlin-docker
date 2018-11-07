package eu.darken.backend

import eu.darken.backend.common.exts.logger
import eu.darken.backend.webserver.HttpVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.core.logging.SLF4JLogDelegateFactory
import io.vertx.reactivex.core.Vertx
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.config.Configurator
import javax.inject.Inject
import javax.inject.Named


class Application @Inject constructor(private val vertx: Vertx, @Named("config") private val config: JsonObject) {
    val log = logger(Application::class)
    fun launch() {
        Application.isDebug = config.getString("VERTX_DEBUG", "true")!!.toBoolean()

        log.warn("Debug: ${Application.isDebug}")

        if (Application.isDebug) {
            Configurator.setRootLevel(Level.DEBUG)
            Configurator.setLevel("eu.darken.backend", Level.DEBUG)
        } else {
            Configurator.setRootLevel(Level.WARN)
            Configurator.setLevel("eu.darken.backend", Level.WARN)
        }

        vertx.deployVerticle("dagger:${HttpVerticle::class.qualifiedName}", DeploymentOptions().apply {
            this.config = this@Application.config
        })
    }

    companion object {
        var isDebug = false

        @JvmStatic
        fun main(args: Array<String>) {
            System.setProperty(LoggerFactory.LOGGER_DELEGATE_FACTORY_CLASS_NAME, SLF4JLogDelegateFactory::class.java.name)
            DaggerAppComponent.builder().build().application().launch()
        }
    }
}