package eu.darken.backend.webserver.graphql.schemas.hello

import com.expedia.graphql.annotations.GraphQLDescription
import com.squareup.moshi.Moshi
import eu.darken.backend.common.exts.logger
import eu.darken.backend.webserver.graphql.schemas.GraphQLQuery
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.ext.mongo.MongoClient
import javax.inject.Inject

@Suppress("unused")
class HelloQuery @Inject constructor(private val mongo: MongoClient, moshi: Moshi) : GraphQLQuery {
    private val adapter = moshi.adapter(Hello::class.java)
    private val log = logger(this::class)

    @GraphQLDescription("A friendly hello")
    fun hello(): String {
        return "helloooo"
    }

    @GraphQLDescription("A query that returns all hellos")
    fun allHellos(): List<Hello> {
        val start = System.currentTimeMillis()
        return mongo.rxFind(Hello.COLLECTION, JsonObject())
                .flattenAsObservable { it }
                .map { adapter.fromJson(it.toString())!! }
                .toList()
                .doOnSuccess { log.info(("##### allHellos(MongoDB) in ${System.currentTimeMillis() - start}ms (${it.size} items)")) }
                .blockingGet()
    }
}