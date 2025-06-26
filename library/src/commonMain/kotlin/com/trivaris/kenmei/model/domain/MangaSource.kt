package com.trivaris.kenmei.model.domain

import com.trivaris.kenmei.db.manga.MangaDatabase
import com.trivaris.kenmei.db.manga.Manga_source
import com.trivaris.kenmei.model.types.toLong

data class MangaSource(
    val id: Long,
    val series: MangaSeries? = null,
    val latestChapter: MangaChapter? = null,
    val firstChapter: MangaChapter? = null,
    val site: MangaSourceSite? = null,
    val name: String? = null,
    val deprecated: Boolean = false,
    val siteActive: Boolean = false,
    val regionLocked: Boolean = false,
    val siteType: String? = null,
    val chaptersCount: Long? = null,
): MangaObject<Manga_source> {
    override fun insert(db: MangaDatabase) {
        db.manga_sourceQueries.insertOrReplaceSource(
            id = id,
            manga_series_id = series?.id,
            latest_chapter_id = latestChapter?.id,
            first_chapter_id = firstChapter?.id,
            site_id = site?.id,
            name = name,
            deprecated = deprecated.toLong(),
            site_active = siteActive.toLong(),
            region_locked = regionLocked.toLong(),
            site_type = siteType,
            chapters_count = chaptersCount,
        )
        series?.insert(db)
        latestChapter?.insert(db)
        firstChapter?.insert(db)
        site?.insert(db)
    }
}
