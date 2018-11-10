package stack.saas.backend.common.moshi

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.*

class UUIDAdapter {
    @ToJson fun toJson(uuid: UUID): String = uuid.toString()

    @FromJson fun fromJson(value: String): UUID = UUID.fromString(value)
}