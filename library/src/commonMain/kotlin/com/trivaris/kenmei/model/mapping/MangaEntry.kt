package com.trivaris.kenmei.model.mapping

import com.trivaris.kenmei.config.KenmeiConfigProvider
import com.trivaris.kenmei.db.manga.MangaDatabase
import com.trivaris.kenmei.db.manga.Manga_entry
import com.trivaris.kenmei.model.domain.MangaEntry
import com.trivaris.kenmei.model.domain.MangaSource
import com.trivaris.kenmei.model.dto.EntryDto
import com.trivaris.kenmei.model.types.toBoolean

fun Manga_entry.toDomain(
    db: MangaDatabase,
): MangaEntry {
    val source =
        manga_source_id?.let { db.manga_sourceQueries.getSource(it).executeAsOneOrNull()?.toDomain(db) ?: MangaSource( id = manga_source_id ) }
    val currentChapter =
        current_chapter_id?.let { db.manga_chapterQueries.getChapter(it).executeAsOne() }?.toDomain()
    val userTags =
        db.manga_entryQueries.getTags(id).executeAsList().map { it.toDomain() }

    return MangaEntry(
        id = id,
        source = source,
        currentChapter = currentChapter,
        userId = user_id,
        status = status,
        hidden = hidden.toBoolean(),
        favourite = favourite.toBoolean(),
        unread = unread.toBoolean(),
        notes = notes,
        score = score,
        lastReadAt = last_read_at,
        createdAt = created_at,
        seriesLink = series_link,
        kenmeiLink = kenmei_link,
        userTags = userTags,
        readChapterTitle = read_chapter_title,
        readChapterNumber = read_chapter_number,
        readChapterVolume = read_chapter_volume,
    )
}

fun EntryDto.toDomain(
    db: MangaDatabase
): MangaEntry {
    val userId = KenmeiConfigProvider.instance.userId
    val mangaSource =
        mangaSourceId?.let { db.manga_sourceQueries.getSource(it).executeAsOneOrNull()?.toDomain(db) ?: MangaSource(id = it) }
    val currentChapter = mangaSourceChapter?.toDomain()
    val userTags = userTagIds?.mapNotNull { db.manga_user_tagQueries.getTag(it).executeAsOneOrNull()?.toDomain() }

    return MangaEntry(
        id = id,
        source = mangaSource,
        currentChapter = currentChapter,
        userId = userId,
        status = attributes?.status?.toLong(),
        hidden = attributes?.hidden ?: false,
        favourite = attributes?.favourite ?: false,
        unread = attributes?.unread ?: false,
        notes = attributes?.notes,
        score = attributes?.score?.value,
        lastReadAt = attributes?.lastReadAt,
        createdAt = attributes?.createdAt,
        seriesLink = links?.seriesUrl,
        kenmeiLink = links?.mangaSeriesUrl,
        userTags = userTags,
        readChapterTitle = readChapter?.title,
        readChapterNumber = readChapter?.chapter?.toDouble(),
        readChapterVolume = readChapter?.volume?.toString()
    )
}