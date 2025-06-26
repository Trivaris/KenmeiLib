package com.trivaris.kenmei.model.domain

import com.trivaris.kenmei.db.manga.MangaDatabase
import com.trivaris.kenmei.db.manga.Manga_classification

data class MangaClassification(
    val id: Long,
    val category: String? = null,
    val name: String? = null,
): MangaObject<Manga_classification> {
    override fun insert(db: MangaDatabase) {
        db.manga_classificationQueries.insertClassification(
            id = id,
            category = category,
            name = name,
        )
    }
}
