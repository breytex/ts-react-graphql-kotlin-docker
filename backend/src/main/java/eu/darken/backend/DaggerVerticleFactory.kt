package eu.darken.backend

import eu.darken.backend.common.exts.logger
import io.vertx.core.Verticle
import io.vertx.core.spi.VerticleFactory
import javax.inject.Provider

/**
 * Verticle factory that delegates the instantiation of the verticle to the appropriate, given [Provider].
 * The "dagger:" prefix advises Vertx to call this factory to get [Verticle] instances.
 *
 * @param verticleMap Map of [Provider]'s they are provided itself by several Dagger modules.
 */
class DaggerVerticleFactory(private val verticleMap: Map<String, Provider<Verticle>>) : VerticleFactory {
    private var log = logger(DaggerVerticleFactory::class)

    override fun createVerticle(verticleName: String, classLoader: ClassLoader): Verticle {
        val verticle = verticleMap
                .getOrElse(sanitizeClassName(verticleName)) { throw IllegalStateException("No provider for verticle type $verticleName found") }
                .get()
        log.info("Verticle for type: $verticleName created")
        return verticle
    }

    private fun sanitizeClassName(verticleName: String): String = verticleName.substring(verticleName.lastIndexOf(":") + 1)

    override fun prefix(): String = "dagger"
}
