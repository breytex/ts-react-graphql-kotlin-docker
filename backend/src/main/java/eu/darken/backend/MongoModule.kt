package eu.darken.backend

import dagger.Module
import dagger.Provides
import eu.darken.backend.common.exts.logger
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.mongo.MongoClient
import javax.inject.Named
import javax.inject.Singleton

@Module
class MongoModule {
    private val log = logger(MongoModule::class)

    @Provides
    @Singleton
    fun provideMongoClient(vertx: Vertx, @Named("ENV") config: JsonObject): MongoClient {
        val mongoconfig = JsonObject(mapOf(
                "connection_string" to config.getString("MONGO_URI"),
                "db_name" to config.getString("MONGO_DATABASE"),
                "username" to config.getString("MONGO_USERNAME"),
                "password" to config.getString("MONGO_PASSWORD")
        ))
        val mongoClient = MongoClient.createShared(vertx, mongoconfig)

        log.info("MongoClient: {}", mongoClient)
        return mongoClient
    }
}
