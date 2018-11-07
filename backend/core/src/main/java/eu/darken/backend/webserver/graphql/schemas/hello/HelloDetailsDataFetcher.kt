package eu.darken.backend.webserver.graphql.schemas.hello

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import javax.inject.Inject

// This will only be used if the client requests the `helloDetails` field
class HelloDetailsDataFetcher @Inject constructor() : DataFetcher<Any> {
    override fun get(environment: DataFetchingEnvironment?): Any {
        val parent = environment?.getSource<Hello>()
        val loader = environment?.getDataLoader<String, HelloDetails>(HelloDetails::class.simpleName)
        return loader!!.load(parent?.helloId.toString())
    }
}