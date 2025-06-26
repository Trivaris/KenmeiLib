package com.trivaris.kenmei.model.mapping

import com.trivaris.kenmei.db.manga.Manga_user_tag
import com.trivaris.kenmei.model.domain.MangaUserTag
import com.trivaris.kenmei.model.dto.UserTagDto

fun Manga_user_tag.toDomain() =
    MangaUserTag(
        id = id,
        name = name,
        description = description,
        entryCount = entry_count,
    )

fun UserTagDto.toDomain() =
    MangaUserTag(
        id = id,
        name = name,
        description = description,
        entryCount = entryCount,
    )
