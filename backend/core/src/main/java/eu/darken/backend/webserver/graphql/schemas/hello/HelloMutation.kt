package eu.darken.backend.webserver.graphql.schemas.hello

import com.expedia.graphql.annotations.GraphQLDescription
import eu.darken.backend.webserver.graphql.schemas.GraphQLMutation
import java.util.*
import javax.inject.Inject

@Suppress("unused")
class HelloMutation @Inject constructor(private val helloRepo: HelloRepo) : GraphQLMutation {

    @GraphQLDescription("A mutation that saves a hello")
    fun saveHello(value: String): Hello {
        val hello = Hello(value, UUID.randomUUID())
        hello.helloDetails = HelloDetails(hello.helloId)
        helloRepo.save(hello).blockingAwait()
        return hello
    }
}