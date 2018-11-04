package eu.darken.backend.webserver.graphql.schemas

import com.expedia.graphql.annotations.GraphQLDescription

data class Hello(val name: String)

class HelloQuery {
    companion object {
        val hellos = mutableListOf<Hello>()
    }

    @GraphQLDescription("A friendly hello")
    fun hello(): String {
        return "helloooo"
    }

    @GraphQLDescription("A query that returns all hellos")
    fun allHellos(): List<Hello> {
        return HelloQuery.hellos
    }
}

class HelloMutation {
    @GraphQLDescription("A mutation that saves a hello")
    fun saveHello(value: String): Hello {
        val hello = Hello(value)
        HelloQuery.hellos.add(hello)
        return hello
    }
}