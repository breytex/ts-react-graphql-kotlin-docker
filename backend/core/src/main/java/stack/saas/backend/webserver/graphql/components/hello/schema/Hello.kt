package stack.saas.backend.webserver.graphql.components.hello.schema

import com.expedia.graphql.annotations.GraphQLDescription
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.bson.types.ObjectId

data class Hello(val name: String, @JsonProperty("_id") val helloId: ObjectId = ObjectId.get()) {
    companion object {
        const val COLLECTION = "hellos"
    }

    @property:GraphQLDescription("This value will be lazily fetched, only when requested.")
    @JsonIgnore
    lateinit var helloDetails: HelloDetails
}