package com.trivaris.kenmei.model.mapping

import com.trivaris.kenmei.db.manga.MangaDatabase
import com.trivaris.kenmei.db.manga.Manga_series
import com.trivaris.kenmei.db.manga.Manga_source
import com.trivaris.kenmei.model.domain.MangaChapter
import com.trivaris.kenmei.model.domain.MangaSeries
import com.trivaris.kenmei.model.domain.MangaSource
import com.trivaris.kenmei.model.domain.MangaSourceSite
import com.trivaris.kenmei.model.types.toBoolean

fun Manga_source.toDomain(
    db: MangaDatabase
): MangaSource {
    val series =
        manga_series_id?.let { db.manga_seriesQueries.getSeries(it).executeAsOneOrNull()?.toDomain(db) ?: MangaSeries(id = it) }
    val latestChapter =
        latest_chapter_id?.let { db.manga_chapterQueries.getChapter(it).executeAsOneOrNull()?.toDomain() ?: MangaChapter(id = it) }
    val firstChapter =
        first_chapter_id?.let { db.manga_chapterQueries.getChapter(it).executeAsOneOrNull()?.toDomain() ?: MangaChapter(id = it) }
    val sourceSite =
        site_id?.let { db.manga_source_siteQueries.getSourceSite(it).executeAsOneOrNull()?.toDomain() ?: MangaSourceSite(id = it) }
    return MangaSource(
        id = id,
        series = series,
        latestChapter = latestChapter,
        firstChapter = firstChapter,
        site = sourceSite,
        name = name,
        deprecated = deprecated.toBoolean(),
        siteActive = site_active.toBoolean(),
        regionLocked = region_locked.toBoolean(),
        siteType = site_type,
        chaptersCount = chapters_count
    )
}
