package com.trivaris.kenmei.model.domain

import com.trivaris.kenmei.db.manga.MangaDatabase
import com.trivaris.kenmei.db.manga.Manga_source_site
import com.trivaris.kenmei.model.types.toLong

data class MangaSourceSite(
    val id: Long,
    val name: String? = null,
    val siteType: String? = null,
    val online: Boolean = false,
    val disabled: Boolean = false,
): MangaObject<Manga_source_site> {
    override fun insert(db: MangaDatabase) {
        db.manga_source_siteQueries.insertSourceSite(
            id = id,
            name = name,
            site_type = siteType,
            online = online.toLong(),
            disabled = disabled.toLong(),
        )
    }
}
