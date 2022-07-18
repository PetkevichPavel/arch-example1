package com.arch.app.network

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.io.IOException

class Rfc3339ZonedDateTimeJsonAdapter : JsonAdapter<ZonedDateTime>() {
    @Synchronized
    @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): ZonedDateTime? =
        reader.peek().takeIf { it == JsonReader.Token.NULL }?.let {
            reader.skipValue()
            null
        } ?: ZonedDateTime.parse(reader.nextString()).withZoneSameInstant(ZoneId.systemDefault())

    @Synchronized
    @Throws(IOException::class)
    override fun toJson(writer: JsonWriter, value: ZonedDateTime?) {
        value?.let {
            writer.value(DateTimeFormatter.ISO_ZONED_DATE_TIME.format(value))
        } ?: run {
            writer.nullValue()
            return
        }
    }
}
