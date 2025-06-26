package com.trivaris.kenmei.model.domain

import com.trivaris.kenmei.db.manga.MangaDatabase
import com.trivaris.kenmei.db.manga.Manga_alternative_title
import kotlinx.serialization.Serializable

@Serializable
data class MangaAlternativeTitle(
    val seriesId: Long?,
    val title: String?,
): MangaObject<Manga_alternative_title> {
    override fun insert(db: MangaDatabase) { error("inserting Alternative Titles directly is not supported and must not be called") }
}
