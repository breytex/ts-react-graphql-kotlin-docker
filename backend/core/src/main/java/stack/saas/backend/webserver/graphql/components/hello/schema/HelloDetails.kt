package stack.saas.backend.webserver.graphql.components.hello.schema

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.bson.types.ObjectId
import java.time.Instant

@Suppress("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
data class HelloDetails constructor(@JsonProperty("_id") val helloId: ObjectId, val createdAt: Instant = Instant.now()) {
    companion object {
        const val COLLECTION = "hellodetails"
    }
}