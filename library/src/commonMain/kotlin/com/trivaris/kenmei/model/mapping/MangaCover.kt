package com.trivaris.kenmei.model.mapping

import com.trivaris.kenmei.db.manga.Manga_cover
import com.trivaris.kenmei.model.domain.MangaCover

fun Manga_cover.toDomain() =
    MangaCover(
        id = id,
        largeWebpUrl = large_webp_url,
        largeJpegUrl = large_jpeg_url,
        smallWebpUrl = small_webp_url,
        smallJpegUrl = small_jpeg_url,
        twitterUrl = twitter_url,
        openGraphUrl = open_graph_url,
    )
