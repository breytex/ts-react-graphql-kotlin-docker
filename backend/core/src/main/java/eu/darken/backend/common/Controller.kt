package eu.darken.backend.common

import io.vertx.reactivex.ext.web.Router


abstract class Controller(val handlers: Router.() -> Unit) {
    abstract val router: Router
    fun create(): Router {
        return router.apply {
            handlers()
        }
    }
}