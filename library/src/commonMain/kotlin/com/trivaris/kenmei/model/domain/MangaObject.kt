package com.trivaris.kenmei.model.domain

import com.trivaris.kenmei.db.manga.MangaDatabase

interface MangaObject<T> {
    fun insert(db: MangaDatabase)
}