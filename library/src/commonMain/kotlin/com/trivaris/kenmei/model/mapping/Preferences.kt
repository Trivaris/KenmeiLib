package com.trivaris.kenmei.model.mapping

import com.trivaris.kenmei.model.domain.ConfigUserPreferences
import com.trivaris.kenmei.model.domain.ConfigPremium
import com.trivaris.kenmei.model.domain.ConfigUserImage
import com.trivaris.kenmei.model.dto.LoginAvatarDto
import com.trivaris.kenmei.model.dto.LoginBannerDto
import com.trivaris.kenmei.model.dto.LoginDto
import com.trivaris.kenmei.model.dto.LoginPremiumDto

private fun LoginAvatarDto?.toDomain(): ConfigUserImage? = this?.let {
    ConfigUserImage(
        largeGifUrl = it.gif?.large,
        smallGifUrl = it.gif?.small,
        largeJpegUrl = it.jpeg?.large,
        smallJpegUrl = it.jpeg?.small,
        largeWebpUrl = it.webp?.large,
        smallWebpUrl = it.webp?.small
    )
}

private fun LoginBannerDto?.toDomain(): ConfigUserImage? = this?.let {
    ConfigUserImage(
        largeGifUrl = it.gif?.large,
        smallGifUrl = it.gif?.small,
        largeJpegUrl = it.jpeg?.large,
        smallJpegUrl = it.jpeg?.small,
        largeWebpUrl = it.webp?.large,
        smallWebpUrl = it.webp?.small
    )
}

private fun LoginPremiumDto?.toDomain(): ConfigPremium? = this?.let {
    ConfigPremium(
        tier = it.tier,
        isPastDue = it.isPastDue,
        isActive = it.isActive,
        hasAccount = it.hasAccount
    )
}

fun LoginDto.toPreferences() = ConfigUserPreferences(
    username = username,
    avatar = avatar.toDomain(),
    banner = banner.toDomain(),
    email = email,
    unconfirmedEmail = unconfirmedEmail,
    role = role,
    features = features,
    hasEntries = hasEntries,
    hasTags = hasTags,
    premium = premium.toDomain(),
    termsAccepted = termsAccepted,
    privacyLevel = privacyLevel,
    themePreference = themePreference,
    themeMode = themeMode
)