package com.trivaris.kenmei.model.domain

import com.trivaris.kenmei.db.manga.MangaDatabase
import com.trivaris.kenmei.db.manga.Manga_series

data class MangaSeries (
    val id: Long,
    val url: String? = null,
    val slug: String? = null,
    val title: String? = null,
    val score: Double? = null,
    val scoreDistributionId: Long? = null,
    val titleEN: String? = null,
    val titleENJP: String? = null,
    val contentType: String? = null,
    val contentRating: String? = null,
    val publicationStatus: String? = null,
    val description: String? = null,
    val malID: Long? = null,
    val usersTracking: Long? = null,
    val cover: MangaCover? = null,
    val alternativeTitles: List<MangaAlternativeTitle>? = null,
    val classifications: List<MangaClassification>? = null,
): MangaObject<Manga_series> {
    override fun insert(db: MangaDatabase) {
        db.manga_seriesQueries.insertSeries(
            id = id,
            url = url,
            slug = slug,
            title = title,
            score = score,
            score_distribution_id = scoreDistributionId,
            title_en = titleEN,
            title_en_jp = titleENJP,
            content_type = contentType,
            content_rating = contentRating,
            publication_status = publicationStatus,
            description = description,
            mal_id = malID,
            users_tracking = usersTracking,
        )
        db.manga_seriesQueries.addCoverInformation(
            manga_series_id = id,
            large_webp_url = cover?.largeWebpUrl,
            small_webp_url = cover?.smallWebpUrl,
            large_jpeg_url = cover?.largeJpegUrl,
            small_jpeg_url = cover?.smallJpegUrl,
            twitter_url = cover?.twitterUrl,
            open_graph_url = cover?.openGraphUrl,
        )
        alternativeTitles?.forEach {
            db.manga_seriesQueries.addAlternativeTitle(id, it.title)
        }
        classifications?.forEach {
            it.insert(db)
            db.manga_seriesQueries.addClassification(id, it.id)
        }
    }
}
