package eu.darken.backend

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import dagger.Module
import eu.darken.backend.common.exts.logger
import io.vertx.core.json.Json


@Module
class AppModule {
    private val log = logger(AppModule::class)

    init {
        for (objectMapper in arrayOf(Json.mapper, Json.prettyMapper)) {
            objectMapper.registerModule(KotlinModule())
            objectMapper.registerModule(ParameterNamesModule())
            objectMapper.registerModule(Jdk8Module())
            objectMapper.registerModule(JavaTimeModule())
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            objectMapper.dateFormat = StdDateFormat()
        }
        log.info("Init done")
    }
}
