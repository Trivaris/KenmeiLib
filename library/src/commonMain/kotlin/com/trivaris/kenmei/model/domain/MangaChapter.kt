package com.trivaris.kenmei.model.domain

import com.trivaris.kenmei.db.manga.MangaDatabase
import com.trivaris.kenmei.db.manga.Manga_chapter
import com.trivaris.kenmei.model.types.toLong

data class MangaChapter(
    val id: Long,
    val url: String? = null,
    val chapterIdentifier: String? = null,
    val volume: String? = null,
    val chapterNumber: Double? = null,
    val title: String? = null,
    val releasedAt: String? = null,
    val locked: Boolean = false,
): MangaObject<Manga_chapter> {
    override fun insert(db: MangaDatabase) {
        db.manga_chapterQueries.insertChapter(
            id = id,
            url = url,
            chapter_identifier = chapterIdentifier,
            volume = volume,
            chapter_number = chapterNumber,
            title = title,
            released_at = releasedAt,
            locked = locked.toLong()
        )
    }
}
