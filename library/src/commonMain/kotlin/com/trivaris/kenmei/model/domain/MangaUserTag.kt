package com.trivaris.kenmei.model.domain

import com.trivaris.kenmei.db.manga.MangaDatabase
import com.trivaris.kenmei.db.manga.Manga_user_tag

data class MangaUserTag(
    val id: Long,
    val name: String? = null,
    val description: String? = null,
    val entryCount: Long? = null,
): MangaObject<Manga_user_tag> {
    override fun insert(db: MangaDatabase) {
        db.manga_user_tagQueries.insertUserTag(
            id = id,
            name = name,
            description = description,
            entry_count = entryCount,
        )
    }
}
