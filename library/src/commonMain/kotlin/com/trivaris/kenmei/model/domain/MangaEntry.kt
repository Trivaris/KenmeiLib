package com.trivaris.kenmei.model.domain

import com.trivaris.kenmei.service.KenmeiConfigProvider
import com.trivaris.kenmei.db.manga.MangaDatabase
import com.trivaris.kenmei.db.manga.Manga_entry
import com.trivaris.kenmei.model.types.toLong

data class MangaEntry(
    val id: Long,
    val source: MangaSource? = null,
    val currentChapter: MangaChapter? = null,
    val userId: Long? = null,
    val status: Long? = null,
    val hidden: Boolean = false,
    val favourite: Boolean = false,
    val unread: Boolean = false,
    val notes: String? = null,
    val score: Double? = null,
    val lastReadAt: String? = null,
    val createdAt: String? = null,
    val seriesLink: String? = null,
    val kenmeiLink: String? = null,
    val userTags: List<MangaUserTag>? = null,
    val readChapterVolume: String? = null,
    val readChapterNumber: Double? = null,
    val readChapterTitle: String? = null,
): MangaObject<Manga_entry> {
    override fun insert(db: MangaDatabase) {
        val userIdNotNull = userId ?: KenmeiConfigProvider.instance.userId
        db.manga_entryQueries.insertEntry(
            id = id,
            manga_source_id = source?.id,
            current_chapter_id = currentChapter?.id,
            user_id = userIdNotNull,
            status = status,
            hidden = hidden.toLong(),
            favourite = favourite.toLong(),
            unread = unread.toLong(),
            notes = notes,
            score = score,
            last_read_at = lastReadAt,
            created_at = createdAt,
            series_link = seriesLink,
            kenmei_link = kenmeiLink,
            read_chapter_volume = readChapterVolume,
            read_chapter_number = readChapterNumber,
            read_chapter_title = readChapterTitle,
        )
        source?.insert(db)
        currentChapter?.insert(db)
        userTags?.forEach {
            db.manga_entryQueries.addTag(id, it.id)
            it.insert(db)
        }
    }
}
