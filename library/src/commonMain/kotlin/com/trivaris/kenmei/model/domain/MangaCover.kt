package com.trivaris.kenmei.model.domain

import com.trivaris.kenmei.db.manga.MangaDatabase
import com.trivaris.kenmei.db.manga.Manga_cover

data class MangaCover(
    val seriesId: Long,
    val largeWebpUrl: String? = null,
    val smallWebpUrl: String? = null,
    val largeJpegUrl: String? = null,
    val smallJpegUrl: String? = null,
    val twitterUrl: String? = null,
    val openGraphUrl: String? = null,
): MangaObject<Manga_cover> {
    override fun insert(db: MangaDatabase) { error("inserting Cover Information directly is not supported and must not be called") }
}
