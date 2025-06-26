package com.trivaris.kenmei.config

import com.trivaris.kenmei.config.model.KenmeiConfig
import com.trivaris.kenmei.config.model.KenmeiConfigJson
import kotlinx.atomicfu.atomic
import java.io.File

actual object KenmeiConfigProvider {
    private const val CONFIG_PATH = "data/config.json"
    private val configRef = atomic<KenmeiConfig?>(null)

    actual val instance: KenmeiConfig
        get() = configRef.value ?: loadConfig().also { configRef.value = it }

    actual fun loadConfig(): KenmeiConfig {
        val file = File(CONFIG_PATH)
        val config = if (file.exists())
            KenmeiConfigJson.decodeFromString(KenmeiConfig.serializer(), file.readText())
        else
            KenmeiConfig().also { saveConfig(it) }
        configRef.value = config
        return config
    }

    actual fun saveConfig(config: KenmeiConfig) {
        File(CONFIG_PATH).apply {
            parentFile?.mkdirs()
            writeText(KenmeiConfigJson.encodeToString(KenmeiConfig.serializer(), config))
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