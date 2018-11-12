package stack.saas.backend.webserver.graphql.components.hello

import com.mongodb.async.client.MongoClients
import com.mongodb.client.model.Filters
import io.reactivex.Completable
import io.reactivex.Single
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.ext.mongo.MongoClient
import org.bson.BsonDocument
import org.bson.types.ObjectId
import stack.saas.backend.common.logger
import stack.saas.backend.webserver.graphql.components.hello.schema.Hello
import stack.saas.backend.webserver.graphql.components.hello.schema.HelloDetails
import javax.inject.Inject

class HelloRepo @Inject constructor(private val mongo: MongoClient) {
    private val log = logger(this::class)
    fun save(hello: Hello): Completable {
        return Single.just(hello)
                .flatMap {
                    val saveHello = mongo.rxSave(Hello.COLLECTION, JsonObject.mapFrom(it))
                    val saveDetails = mongo.rxSave(HelloDetails.COLLECTION, JsonObject.mapFrom(it.helloDetails))
                    Single.zipArray({ ignore -> ignore }, arrayOf(saveHello, saveDetails))
                }
                .toCompletable()
    }

    fun getAllHellos(): Single<List<Hello>> {
        return mongo.rxFind(Hello.COLLECTION, JsonObject())
                .flattenAsObservable { it }
                .map { it.mapTo(Hello::class.java)!! }
                .toList()
    }

    fun getAllDetails(ids: List<ObjectId>): Single<List<HelloDetails?>> {
        val queryFilter = Filters.`in`("_id", ids)
        val bsonDocument = queryFilter.toBsonDocument(BsonDocument::class.java, MongoClients.getDefaultCodecRegistry())
        return mongo.rxFind(HelloDetails.COLLECTION, JsonObject(bsonDocument.toJson()))
                .flattenAsObservable { it }
                .map { it.mapTo(HelloDetails::class.java) }
                .toList()
    }


}