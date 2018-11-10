package stack.saas.backend.webserver.graphql.components.hello

import com.mongodb.async.client.MongoClients
import com.mongodb.client.model.Filters
import com.squareup.moshi.Moshi
import io.reactivex.Completable
import io.reactivex.Single
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.ext.mongo.MongoClient
import org.bson.BsonDocument
import stack.saas.backend.common.logger
import stack.saas.backend.webserver.graphql.components.hello.schema.Hello
import stack.saas.backend.webserver.graphql.components.hello.schema.HelloDetails
import javax.inject.Inject

class HelloRepo @Inject constructor(private val mongo: MongoClient, moshi: Moshi) {
    private val log = logger(this::class)

    private val helloAdapter = moshi.adapter(Hello::class.java)
    private val detailsAdapter = moshi.adapter(HelloDetails::class.java)

    fun save(hello: Hello): Completable {
        return Single.just(hello)
                .flatMap {
                    val saveHello = mongo.rxSave(Hello.COLLECTION, JsonObject(helloAdapter.toJson(it)))
                    val saveDetails = mongo.rxSave(HelloDetails.COLLECTION, JsonObject(detailsAdapter.toJson(it.helloDetails)))
                    Single.zipArray({ ignore -> ignore }, arrayOf(saveHello, saveDetails))
                }
                .toCompletable()
    }

    fun getAllHellos(): Single<List<Hello>> {
        return mongo.rxFind(Hello.COLLECTION, JsonObject())
                .flattenAsObservable { it }
                .map { helloAdapter.fromJson(it.toString())!! }
                .toList()
    }

    fun getAllDetails(ids: List<String>): Single<List<HelloDetails?>> {
        val queryFilter = Filters.`in`("helloId", ids)
        val bsonDocument = queryFilter.toBsonDocument(BsonDocument::class.java, MongoClients.getDefaultCodecRegistry())
        return mongo.rxFind(HelloDetails.COLLECTION, JsonObject(bsonDocument.toJson()))
                .flattenAsObservable { it }
                .map { detailsAdapter.fromJsonValue(it.map)!! }
                .toList()
    }


}