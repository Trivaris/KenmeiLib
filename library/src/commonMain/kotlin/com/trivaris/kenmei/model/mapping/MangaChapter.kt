package com.trivaris.kenmei.model.mapping

import com.trivaris.kenmei.db.manga.Manga_chapter
import com.trivaris.kenmei.model.domain.MangaChapter
import com.trivaris.kenmei.model.dto.EntryChapterDto
import com.trivaris.kenmei.model.types.toBoolean

fun Manga_chapter.toDomain(): MangaChapter {
    return MangaChapter(
        id = id,
        url = url,
        chapterIdentifier = chapter_identifier,
        volume = volume,
        chapterNumber = chapter_number,
        title = title,
        releasedAt = released_at,
        locked = locked.toBoolean()
    )
}

fun EntryChapterDto.toDomain() =
    MangaChapter(
        id = id,
        url = url,
        chapterIdentifier = chapterIdentifier,
        volume = volume,
        chapterNumber = chapterNumber,
        title = title,
        releasedAt = releasedAt,
        locked = locked
    )

