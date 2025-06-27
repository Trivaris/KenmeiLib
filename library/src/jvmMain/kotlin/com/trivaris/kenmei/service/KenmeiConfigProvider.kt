package com.trivaris.kenmei.service

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import com.trivaris.kenmei.model.domain.ConfigMain
import com.trivaris.kenmei.model.domain.ConfigUserPreferences
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath

actual object KenmeiConfigProvider {
    private const val CONFIG_PATH = "data/config.pb"
    private val configRef = atomic<ConfigMain?>(null)

    private val scope = CoroutineScope(Dispatchers.Default)
    private val dataStore: DataStore<ConfigMain> = DataStoreFactory.create(
        serializer = ConfigMainSerializer,
        scope = scope,
        produceFile = {
            val path: Path = CONFIG_PATH.toPath()
            path.parent?.let { FileSystem.SYSTEM.createDirectories(it) }
            path
        }
    )

    actual val instance: ConfigMain
        get() = configRef.value ?: loadConfig().also { configRef.value = it }

    actual fun loadConfig(): ConfigMain {
        val config = runBlocking { dataStore.data.first() }
        configRef.value = config
        return config
    }

    actual fun saveConfig(config: ConfigMain) {
        runBlocking { dataStore.updateData { config } }
        configRef.value = config
    }

    actual fun updatePreferences(prefs: ConfigUserPreferences) {
        saveConfig(instance.copy(preferences = prefs))
    }

    actual fun switchUser(userId: Long) {
        saveConfig(instance.copy(userId = userId))
    }
}