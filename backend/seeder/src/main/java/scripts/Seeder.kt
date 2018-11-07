package scripts

import eu.darken.backend.common.exts.logger
import eu.darken.backend.webserver.graphql.schemas.hello.Hello
import eu.darken.backend.webserver.graphql.schemas.hello.HelloDetails
import eu.darken.backend.webserver.graphql.schemas.hello.HelloRepo
import io.reactivex.Single
import io.vertx.reactivex.core.Vertx
import java.util.*
import javax.inject.Inject

class Seeder @Inject constructor(private val helloRepo: HelloRepo) {
    private val log = logger(this::class)

    fun launch() {
        log.info("Seeding data...")
        Single
                .create<Hello> {
                    val hello = Hello("Name-${UUID.randomUUID().toString().substring(0, 10)}", UUID.randomUUID())
                    hello.helloDetails = HelloDetails(hello.helloId)
                    it.onSuccess(hello)
                }
                .flatMapCompletable { helloRepo.save(it) }
                .repeat(1000)
                .doFinally { log.info("...seeding done") }
                .blockingAwait()

        System.exit(0)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            DaggerSeederComponent.builder().vertx(Vertx.vertx()).build().seeder().launch()
        }
    }

}