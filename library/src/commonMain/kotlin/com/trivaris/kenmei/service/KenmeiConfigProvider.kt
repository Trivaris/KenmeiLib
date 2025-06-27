package com.trivaris.kenmei.service

import com.trivaris.kenmei.model.domain.ConfigMain
import com.trivaris.kenmei.model.domain.ConfigUserPreferences

expect object KenmeiConfigProvider {
    val instance: ConfigMain

    fun updatePreferences(prefs: ConfigUserPreferences)
    fun switchUser(userId: Long)
    fun loadConfig(): ConfigMain
    fun saveConfig(config: ConfigMain = instance)
}