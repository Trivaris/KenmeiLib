package com.trivaris.kenmei.service

import com.trivaris.kenmei.model.domain.ConfigMain

expect object KenmeiConfigProvider {
    val instance: ConfigMain

    fun updatePreferences(prefs: Preferences)
    fun switchUser(userId: Long)
    fun loadConfig(): ConfigMain
    fun saveConfig(config: ConfigMain = instance)
}