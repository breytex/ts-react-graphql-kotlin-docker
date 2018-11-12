package stack.saas.backend

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.module.kotlin.KotlinModule
import dagger.Module
import dagger.Provides
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.mongo.MongoClient
import stack.saas.backend.common.jackson.IsoDateJacksonModule
import stack.saas.backend.common.jackson.ObjectIdJacksonModule
import stack.saas.backend.common.logger
import javax.inject.Named
import javax.inject.Singleton

@Module
class MongoModule {
    private val log = logger(MongoModule::class)

    init {
        for (objectMapper in arrayOf(Json.mapper, Json.prettyMapper)) {
            objectMapper.apply {
                registerModule(KotlinModule())
                registerModule(Jdk8Module())
                registerModule(IsoDateJacksonModule())
                registerModule(ObjectIdJacksonModule())
            }
        }
        log.info("Init done")
    }

    @Provides
    @Singleton
    fun provideMongoClient(vertx: Vertx, @Named("config") config: JsonObject): MongoClient {
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
