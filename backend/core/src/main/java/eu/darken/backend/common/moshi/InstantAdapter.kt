package eu.darken.backend.common.moshi

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.Instant

class InstantAdapter {
    @ToJson fun toJson(value: Instant): String = value.toString()

    @FromJson fun fromJson(value: String): Instant = Instant.parse(value)
}