package stack.saas.backend.webserver.graphql.components.hello.schema

import com.expedia.graphql.annotations.GraphQLDescription
import stack.saas.backend.common.graphql.GraphQLQuery
import stack.saas.backend.common.logger
import stack.saas.backend.webserver.graphql.components.hello.HelloRepo
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