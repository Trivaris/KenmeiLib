package com.trivaris.kenmei.config

import com.trivaris.kenmei.config.model.KenmeiConfig
import com.trivaris.kenmei.config.model.KenmeiConfigJson
import kotlinx.atomicfu.atomic
import kotlinx.io.errors.IOException
import okio.FileSystem
import okio.Path.Companion.toPath

actual object KenmeiConfigProvider {
    private const val CONFIG_PATH = "kenmei_config.json"
    private val configRef = atomic<KenmeiConfig?>(null)

    actual val instance: KenmeiConfig
        get() = configRef.value ?: loadConfig().also { configRef.value = it }

    actual fun loadConfig(): KenmeiConfig {
        val path = CONFIG_PATH.toPath()
        return try {
            if (FileSystem.SYSTEM.exists(path)) {
                val content = FileSystem.SYSTEM.read(path) { readUtf8() }
                KenmeiConfigJson.decodeFromString(KenmeiConfig.serializer(), content)
            } else KenmeiConfig().also { saveConfig(it) }
        } catch (e: IOException) {
            KenmeiConfig().also { saveConfig(it) }
        }
    }

    actual fun saveConfig(config: KenmeiConfig) {
        val json = KenmeiConfigJson.encodeToString(KenmeiConfig.serializer(), config)
        FileSystem.SYSTEM.write(CONFIG_PATH.toPath()) {
            writeUtf8(json)
        }
        configRef.value = config
    }

    actual fun updatePreferences(prefs: Preferences) {
        instance.userPreferences = prefs
        saveConfig()
    }

    actual fun switchUser(userId: Long) {
        instance.userId = userId
        saveConfig()
    }
}