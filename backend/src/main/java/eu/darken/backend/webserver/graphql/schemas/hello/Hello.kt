package eu.darken.backend.webserver.graphql.schemas.hello

import com.expedia.graphql.annotations.GraphQLDescription
import eu.darken.backend.webserver.graphql.schemas.GraphQLMutation
import eu.darken.backend.webserver.graphql.schemas.GraphQLQuery
import java.time.Instant
import javax.inject.Inject

data class Hello(val name: String) {
    val createdAt = Instant.now()
}

class HelloQuery @Inject constructor() : GraphQLQuery {
    companion object {
        val hellos = mutableListOf<Hello>()
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
        return hello
    }
}