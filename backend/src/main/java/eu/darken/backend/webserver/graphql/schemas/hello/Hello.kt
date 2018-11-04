package eu.darken.backend.webserver.graphql.schemas.hello

import com.expedia.graphql.annotations.GraphQLDescription
import eu.darken.backend.AppModule
import eu.darken.backend.common.exts.logger
import eu.darken.backend.webserver.graphql.schemas.GraphQLMutation
import eu.darken.backend.webserver.graphql.schemas.GraphQLQuery
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import java.time.Instant
import java.util.*
import javax.inject.Inject

data class Hello(val name: String) {
    val id = UUID.randomUUID()
    lateinit var helloDetails: HelloDetails
}

data class HelloDetails(val id: UUID) {
    val createdAt = Instant.now()
}

class HelloQuery @Inject constructor() : GraphQLQuery {
    companion object {
        val hellos = mutableListOf<Hello>()
        val helloDetails = mutableListOf<HelloDetails>()
    }

    @GraphQLDescription("A friendly hello")
    fun hello(): String {
        return "helloooo"
    }

    @GraphQLDescription("A query that returns all hellos")
    fun allHellos(): List<Hello> {
        return hellos
    }
}

class HelloMutation @Inject constructor() : GraphQLMutation {
    @GraphQLDescription("A mutation that saves a hello")
    fun saveHello(value: String): Hello {
        val hello = Hello(value)
        HelloQuery.hellos.add(hello)
        HelloQuery.helloDetails.add(HelloDetails(hello.id))
        return hello
    }
}

class HelloDetailsDataFetcher @Inject constructor() : DataFetcher<Any> {

    private val log = logger(AppModule::class)
    override fun get(environment: DataFetchingEnvironment?): Any {
        log.debug("Environment: $environment")
        val sourceHello = environment?.getSource<Hello>()
        HelloQuery.helloDetails.forEach {
            if (it.id == sourceHello?.id) return HelloDetails(UUID.randomUUID())
        }
        throw Exception()
    }
}