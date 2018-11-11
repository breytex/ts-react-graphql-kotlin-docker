package stack.saas.backend.common.moshi

import com.squareup.moshi.JsonAdapter
import io.vertx.core.json.JsonObject

@Suppress("UNCHECKED_CAST")
fun <T> JsonAdapter<T>.toJsonMap(obj: T): Map<String, Any> {
    return this.toJsonValue(obj) as Map<String, Any>
}

fun <T> JsonAdapter<T>.toJsonObject(obj: T): JsonObject {
    return JsonObject(this.toJsonMap(obj))
}