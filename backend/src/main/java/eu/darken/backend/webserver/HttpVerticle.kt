package eu.darken.backend.webserver

import io.vertx.core.http.HttpServerOptions
import io.vertx.core.net.PemKeyCertOptions
import io.vertx.reactivex.core.AbstractVerticle
import io.vertx.reactivex.ext.web.Router
import org.apache.logging.log4j.LogManager
import java.io.File
import java.net.URI

class HttpVerticle constructor(val httpController: HttpController) : AbstractVerticle() {
    var logger = LogManager.getLogger(HttpVerticle::class.simpleName)

    override fun start() {
        val config = config()
        val nonSSLPort = config.getInteger("http.port", 4000)
        val portSSL = config.getInteger("https.port", nonSSLPort + 22)
        val privKey = File(config.getString("https.key", "~"))
        val pubKey = File(config.getString("https.cert", "~"))

        val router = httpController.create()

        val redirectRouter = Router.router(vertx)
        redirectRouter.route().handler { context ->
            try {
                val myPublicUri = URI(context.request().absoluteURI())
                logger.info("URI: {}", myPublicUri.toURL())
                if (myPublicUri.scheme == "http") {
                    val myHttpsPublicUri = URI("https",
                            myPublicUri.userInfo,
                            myPublicUri.host,
                            portSSL,
                            myPublicUri.rawPath,
                            myPublicUri.rawQuery,
                            myPublicUri.rawFragment)
                    context.response().putHeader("Location", myHttpsPublicUri.toString()).setStatusCode(301).end()
                } else {
                    context.next()
                }
            } catch (ex: Throwable) {
                context.fail(ex)
            }
        }

        val baseOptions = HttpServerOptions()
                .setIdleTimeout(15)

        vertx.createHttpServer(baseOptions)
                .requestHandler(redirectRouter::accept)
                .requestHandler(router::accept)
                .rxListen(nonSSLPort)
                .doOnSuccess { logger.info("Api (HTTP) listening on port: {}", nonSSLPort) }
                .subscribe()

        val httpsOptions = if (privKey.exists() && pubKey.exists()) {
            logger.info("Loaded private key from {}", privKey.path)
            logger.info("Loaded public key from {}", pubKey.path)
            HttpServerOptions(baseOptions).setSsl(true)
                    .setPemKeyCertOptions(PemKeyCertOptions()
                            .setKeyPath(privKey.path)
                            .setCertPath(pubKey.path))
        } else {
            HttpServerOptions(baseOptions)
        }

        vertx.createHttpServer(httpsOptions)
                .requestHandler(router::accept)
                .rxListen(portSSL)
                .doOnSuccess { logger.info("Api (HTTPS) listening on port: {}", portSSL) }
                .subscribe()

        super.start()
    }
}