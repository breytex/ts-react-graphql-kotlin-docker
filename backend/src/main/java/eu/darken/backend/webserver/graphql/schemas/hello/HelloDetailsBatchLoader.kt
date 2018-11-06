package eu.darken.backend.webserver.graphql.schemas.hello

import com.mongodb.async.client.MongoClients.getDefaultCodecRegistry
import com.mongodb.client.model.Filters
import com.squareup.moshi.Moshi
import eu.darken.backend.common.exts.logger
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.ext.mongo.MongoClient
import org.bson.BsonDocument
import org.dataloader.BatchLoader
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import javax.inject.Inject

class HelloDetailsBatchLoader @Inject constructor(val mongo: MongoClient, moshi: Moshi) : BatchLoader<String, HelloDetails> {
    private val adapterDetails = moshi.adapter(HelloDetails::class.java)!!
    val log = logger(this::class)

    override fun load(keys: MutableList<String>): CompletionStage<MutableList<HelloDetails?>> {
        val start = System.currentTimeMillis()
        val future = CompletableFuture<MutableList<HelloDetails?>>()

        val queryFilter = Filters.`in`("helloId", keys)
        val bsonDocument = queryFilter.toBsonDocument(BsonDocument::class.java, getDefaultCodecRegistry())

        val helloMap = mutableMapOf<String, HelloDetails>()
        mongo.rxFind(HelloDetails.COLLECTION, JsonObject(bsonDocument.toJson()))
                .doOnSuccess { log.info("##### Got ${it.size} HelloDetails in ${System.currentTimeMillis() - start}ms") }
                .flattenAsObservable { it }
                .map {
                    val value = adapterDetails.fromJsonValue(it.map)!!
                    helloMap[value.helloId.toString()] = value
                    return@map value
                }
                .toList()
                .map {
                    val hellos = mutableListOf<HelloDetails?>()
                    keys.forEach { id -> hellos.add(helloMap.get(id)) }
                    return@map hellos
                }
                .doFinally { log.info("##### HelloDetailsBatchLoader done in ${System.currentTimeMillis() - start}ms (${keys.size} items)") }
                .subscribe({ future.complete(it) }, { future.completeExceptionally(it) })
        return future
    }

}