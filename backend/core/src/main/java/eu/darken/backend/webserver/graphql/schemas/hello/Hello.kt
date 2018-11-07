package eu.darken.backend.webserver.graphql.schemas.hello

import com.expedia.graphql.annotations.GraphQLDescription
import java.util.*

@Suppress("unused")
data class Hello(val name: String, val helloId: UUID) {
    companion object {
        const val COLLECTION = "hellos"
    }

    @property:GraphQLDescription("This value will be lazily fetched, only when requested.")
    @Transient
    lateinit var helloDetails: HelloDetails
}