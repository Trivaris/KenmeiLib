package com.trivaris.kenmei.service

import com.trivaris.kenmei.model.domain.ConfigMain
import com.trivaris.kenmei.model.types.KenmeiConfigJson
import kotlinx.atomicfu.atomic
import java.io.File

actual object KenmeiConfigProvider {
    private const val CONFIG_PATH = "data/config.json"
    private val configRef = atomic<ConfigMain?>(null)

    actual val instance: ConfigMain
        get() = configRef.value ?: loadConfig().also { configRef.value = it }

    actual fun loadConfig(): ConfigMain {
        val file = File(CONFIG_PATH)
        val config = if (file.exists()) {
            KenmeiConfigJson.decodeFromString(ConfigMain.serializer(), file.readText())
        } else {
            ConfigMain().also { saveConfig(it) }
        }
        configRef.value = config
        return config
    }

    actual fun saveConfig(config: ConfigMain) {
        File(CONFIG_PATH).apply {
            parentFile?.mkdirs()
            writeText(KenmeiConfigJson.encodeToString(ConfigMain.serializer(), config))
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