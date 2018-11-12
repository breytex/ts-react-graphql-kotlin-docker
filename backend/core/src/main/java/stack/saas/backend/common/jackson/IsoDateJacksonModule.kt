package stack.saas.backend.common.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import java.time.Instant


class IsoDateJacksonModule : SimpleModule() {
    init {
        addSerializer(Instant::class.java, Serializer())
        addDeserializer(Instant::class.java, Deserializer())
    }

    inner class Serializer : JsonSerializer<Instant>() {
        override fun serialize(value: Instant, jgen: JsonGenerator, provider: SerializerProvider) {
            jgen.writeStartObject()
            jgen.writeFieldName("\$date")
            jgen.writeString(value.toString())
            jgen.writeEndObject()
        }
    }

    inner class Deserializer : StdDeserializer<Instant>(Instant::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Instant {
            val tree: JsonNode = p.readValueAsTree()
            return Instant.parse(tree.get("\$date").textValue())
        }

    }
}