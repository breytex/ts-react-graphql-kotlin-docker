package stack.saas.backend.common

import com.mongodb.async.client.MongoClients
import io.vertx.core.json.JsonObject
import org.bson.BsonDocument
import org.bson.conversions.Bson

fun Bson.toJsonObject(): JsonObject {
    val bsonDocument = this.toBsonDocument(BsonDocument::class.java, MongoClients.getDefaultCodecRegistry())
    return JsonObject(bsonDocument.toJson())
}