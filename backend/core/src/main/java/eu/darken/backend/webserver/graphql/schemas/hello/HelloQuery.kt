package eu.darken.backend.webserver.graphql.schemas.hello

import com.expedia.graphql.annotations.GraphQLDescription
import eu.darken.backend.common.exts.logger
import eu.darken.backend.common.graphql.GraphQLQuery
import javax.inject.Inject

@Suppress("unused")
class HelloQuery @Inject constructor(private val helloRepo: HelloRepo) : GraphQLQuery {
    private val log = logger(this::class)

    @GraphQLDescription("A friendly hello")
    fun hello(): String {
        return "helloooo"
    }

    @GraphQLDescription("A query that returns all hellos")
    fun allHellos(): List<Hello> {
        val start = System.currentTimeMillis()
        return helloRepo.getAllHellos()
                .doOnSuccess { log.info(("##### allHellos(MongoDB) in ${System.currentTimeMillis() - start}ms (${it.size} items)")) }
                .blockingGet()
    }
}