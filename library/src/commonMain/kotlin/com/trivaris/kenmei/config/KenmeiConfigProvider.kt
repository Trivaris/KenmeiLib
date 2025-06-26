package com.trivaris.kenmei.config

import com.trivaris.kenmei.config.model.KenmeiConfig

expect object KenmeiConfigProvider {
    val instance: KenmeiConfig

    fun updatePreferences(prefs: Preferences)
    fun switchUser(userId: Long)
    fun loadConfig(): KenmeiConfig
    fun saveConfig(config: KenmeiConfig = instance)
}