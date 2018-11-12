package stack.saas.backend.webserver.graphql.components.hello.schema

import com.expedia.graphql.annotations.GraphQLDescription
import stack.saas.backend.common.graphql.GraphQLMutation
import stack.saas.backend.webserver.graphql.components.hello.HelloRepo
import javax.inject.Inject

@Suppress("unused")
class HelloMutation @Inject constructor(private val helloRepo: HelloRepo) : GraphQLMutation {

    @GraphQLDescription("A mutation that saves a hello")
    fun saveHello(value: String): Hello {
        val hello = Hello(value)
        hello.helloDetails = HelloDetails(hello.helloId)
        helloRepo.save(hello).blockingAwait()
        return hello
    }
}