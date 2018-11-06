package eu.darken.backend.webserver.graphql.schemas.hello

import com.expedia.graphql.annotations.GraphQLDescription
import com.squareup.moshi.Moshi
import eu.darken.backend.webserver.graphql.schemas.GraphQLMutation
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.ext.mongo.MongoClient
import java.util.*
import javax.inject.Inject

@Suppress("unused")
class HelloMutation @Inject constructor(private val mongo: MongoClient, moshi: Moshi) : GraphQLMutation {

    private val adapter = moshi.adapter(Hello::class.java)
    private val adapterDetails = moshi.adapter(HelloDetails::class.java)

    @GraphQLDescription("A mutation that saves a hello")
    fun saveHello(value: String): Hello {
        val hello = Hello(value, UUID.randomUUID())
        hello.helloDetails = HelloDetails(hello.helloId)
        return mongo.rxSave(Hello.COLLECTION, JsonObject(adapter.toJson(hello)))
                .flatMap { mongo.rxSave(HelloDetails.COLLECTION, JsonObject(adapterDetails.toJson(hello.helloDetails))) }
                .map { hello }
                .blockingGet()
    }
}