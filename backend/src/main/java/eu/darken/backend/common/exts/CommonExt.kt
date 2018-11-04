package eu.darken.backend.common.exts

import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import kotlin.reflect.KClass

fun logger(clz: KClass<*>): Logger {
    return LoggerFactory.getLogger(clz.qualifiedName)
}