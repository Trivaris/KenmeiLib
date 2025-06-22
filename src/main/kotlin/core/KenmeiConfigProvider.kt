package com.trivaris.kenmei.core

import com.trivaris.kenmei.core.model.Preferences
import java.io.File

object KenmeiConfigProvider {
    private const val CONFIG_PATH = "data/config.json"

    @Volatile
    private var _config: KenmeiConfig? = null

    val instance: KenmeiConfig
        get() = _config ?: loadConfig()

    @Synchronized
    fun loadConfig(): KenmeiConfig {
        val file = File(CONFIG_PATH)
        _config =
            if (file.exists()) KenmeiConfig.fromJson(file.readText())
            else KenmeiConfig().also { saveConfig(it) }
        return _config!!
    }

    @Synchronized
    fun saveConfig(config: KenmeiConfig = instance) {
        File(CONFIG_PATH).parentFile?.mkdirs()
        File(CONFIG_PATH).writeText(config.toJson())
    }

    @Synchronized
    fun updatePreferences(prefs: Preferences) {
        instance.userPreferences = prefs
        saveConfig()
    }

    @Synchronized
    fun switchUser(userId: Long) {
        instance.userId = userId
        saveConfig()
    }
}
