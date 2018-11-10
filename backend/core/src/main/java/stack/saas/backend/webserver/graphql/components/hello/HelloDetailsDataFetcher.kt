package stack.saas.backend.webserver.graphql.components.hello

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import stack.saas.backend.webserver.graphql.components.hello.schema.Hello
import stack.saas.backend.webserver.graphql.components.hello.schema.HelloDetails
import javax.inject.Inject

// This will only be used if the client requests the `helloDetails` field
class HelloDetailsDataFetcher @Inject constructor() : DataFetcher<Any> {
    override fun get(environment: DataFetchingEnvironment?): Any {
        val parent = environment?.getSource<Hello>()
        val loader = environment?.getDataLoader<String, HelloDetails>(HelloDetails::class.simpleName)
        return loader!!.load(parent?.helloId.toString())
    }
}