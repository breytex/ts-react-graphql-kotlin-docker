package stack.saas.backend.webserver.graphql.components.hello.schema

import java.time.Instant
import java.util.*

@Suppress("unused")
data class HelloDetails(val helloId: UUID) {
    companion object {
        const val COLLECTION = "hellodetails"
    }

    var createdAt: Instant = Instant.now()
}