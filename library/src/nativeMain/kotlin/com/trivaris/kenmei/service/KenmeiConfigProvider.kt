package com.trivaris.kenmei.service

import com.trivaris.kenmei.model.domain.ConfigMain
import com.trivaris.kenmei.model.types.KenmeiConfigJson
import kotlinx.atomicfu.atomic
import kotlinx.io.errors.IOException
import okio.FileSystem
import okio.Path.Companion.toPath

actual object KenmeiConfigProvider {
    private const val CONFIG_PATH = "kenmei_config.json"
    private val configRef = atomic<ConfigMain?>(null)

    actual val instance: ConfigMain
        get() = configRef.value ?: loadConfig().also { configRef.value = it }

    actual fun loadConfig(): ConfigMain {
        val path = CONFIG_PATH.toPath()
        return try {
            if (FileSystem.SYSTEM.exists(path)) {
                val content = FileSystem.SYSTEM.read(path) { readUtf8() }
                KenmeiConfigJson.decodeFromString(ConfigMain.serializer(), content)
            } else ConfigMain().also { saveConfig(it) }
        } catch (e: IOException) {
            ConfigMain().also { saveConfig(it) }
        }
    }

    actual fun saveConfig(config: ConfigMain) {
        val json = KenmeiConfigJson.encodeToString(ConfigMain.serializer(), config)
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