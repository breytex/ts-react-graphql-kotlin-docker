package stack.saas.backend.common.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import org.bson.types.ObjectId


class ObjectIdJacksonModule : SimpleModule() {
    init {
        addSerializer(ObjectId::class.java, Serializer())
        addDeserializer(ObjectId::class.java, Deserializer())
    }

    inner class Serializer : JsonSerializer<ObjectId>() {
        override fun serialize(value: ObjectId, jgen: JsonGenerator, provider: SerializerProvider) {
            jgen.writeStartObject()
            jgen.writeFieldName("\$oid")
            jgen.writeString(value.toHexString())
            jgen.writeEndObject()
        }
    }

    inner class Deserializer : StdDeserializer<ObjectId>(ObjectId::class.java) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ObjectId {
            val tree: JsonNode = p.readValueAsTree()
            return ObjectId(tree.get("\$oid").textValue())
        }

    }
}