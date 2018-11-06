package eu.darken.backend.webserver.graphql.schemas.hello

import com.expedia.graphql.annotations.GraphQLDescription
import com.squareup.moshi.Moshi
import eu.darken.backend.webserver.graphql.schemas.GraphQLMutation
import eu.darken.backend.webserver.graphql.schemas.GraphQLQuery
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.ext.mongo.MongoClient
import java.time.Instant
import java.util.*
import java.util.concurrent.CompletableFuture
import javax.inject.Inject

@Suppress("unused")
data class Hello(val name: String, val helloId: UUID) {
    companion object {
        const val COLLECTION = "hellos"
    }

    @property:GraphQLDescription("This value will be lazily fetched, only when requested.")
    @Transient
    lateinit var helloDetails: HelloDetails
}

@Suppress("unused")
data class HelloDetails(val helloId: UUID) {
    companion object {
        const val COLLECTION = "hellodetails"
    }

    var createdAt: Instant = Instant.now()
}

@Suppress("unused")
class HelloQuery @Inject constructor(private val mongo: MongoClient, moshi: Moshi) : GraphQLQuery {
    private val adapter = moshi.adapter(Hello::class.java)

    @GraphQLDescription("A friendly hello")
    fun hello(): String {
        return "helloooo"
    }

    @GraphQLDescription("A query that returns all hellos")
    fun allHellos(): List<Hello> {
        return mongo.rxFind(Hello.COLLECTION, JsonObject())
                .flattenAsObservable { it }
                .map { adapter.fromJson(it.toString())!! }
                .toList()
                .blockingGet()
    }
}

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

// This will only be used if the client requests the `helloDetails` field
class HelloDetailsDataFetcher @Inject constructor(private val mongo: MongoClient, moshi: Moshi) : DataFetcher<Any> {
    private val adapter = moshi.adapter(HelloDetails::class.java)

    override fun get(environment: DataFetchingEnvironment?): Any {
        val sourceHello = environment?.getSource<Hello>()
        val json = JsonObject().put("helloId", sourceHello?.helloId.toString())
        val future = CompletableFuture<HelloDetails>()
        mongo.rxFindOne(HelloDetails.COLLECTION, json, null)
                .map { adapter.fromJsonValue(it.map)!! }
                .subscribe({ it -> future.complete(it) }, { err -> future.completeExceptionally(err) })
        return future
    }
}