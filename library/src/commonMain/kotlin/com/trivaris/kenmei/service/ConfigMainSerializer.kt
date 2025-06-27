package com.trivaris.kenmei.service

import androidx.datastore.core.okio.OkioSerializer
import com.trivaris.kenmei.model.domain.ConfigMain
import com.trivaris.kenmei.model.types.KenmeiConfigJson
import okio.BufferedSink
import okio.BufferedSource

internal object ConfigMainSerializer : OkioSerializer<ConfigMain> {
    override val defaultValue: ConfigMain = ConfigMain()

    override suspend fun readFrom(source: BufferedSource): ConfigMain {
        return try {
            val text = source.readUtf8()
            KenmeiConfigJson.decodeFromString(ConfigMain.serializer(), text)
        } catch (_: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: ConfigMain, sink: BufferedSink) {
        val text = KenmeiConfigJson.encodeToString(ConfigMain.serializer(), t)
        sink.writeUtf8(text)
    }
}
