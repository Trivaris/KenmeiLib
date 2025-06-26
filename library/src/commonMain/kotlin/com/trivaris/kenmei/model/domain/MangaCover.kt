package com.trivaris.kenmei.model.domain

import com.trivaris.kenmei.db.manga.MangaDatabase
import com.trivaris.kenmei.db.manga.Manga_cover

data class MangaCover(
    val id: Long,
    val largeWebpUrl: String? = null,
    val smallWebpUrl: String? = null,
    val largeJpegUrl: String? = null,
    val smallJpegUrl: String? = null,
    val twitterUrl: String? = null,
    val openGraphUrl: String? = null,
): MangaObject<Manga_cover> {
    override fun insert(db: MangaDatabase) {
        db.manga_coverQueries.insertCover(
            large_webp_url = largeWebpUrl,
            small_webp_url = smallWebpUrl,
            large_jpeg_url = largeJpegUrl,
            small_jpeg_url = smallJpegUrl,
            twitter_url = twitterUrl,
            open_graph_url = openGraphUrl,
        )
    }
}
