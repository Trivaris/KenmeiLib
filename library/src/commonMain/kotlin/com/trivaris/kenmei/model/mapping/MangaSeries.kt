package com.trivaris.kenmei.model.mapping

import com.trivaris.kenmei.db.manga.MangaDatabase
import com.trivaris.kenmei.db.manga.Manga_series
import com.trivaris.kenmei.model.domain.MangaCover
import com.trivaris.kenmei.model.domain.MangaSeries

fun Manga_series.toDomain(
    db: MangaDatabase
): MangaSeries {
    val cover = db.manga_seriesQueries.getCoverInformation(id).executeAsOneOrNull()?.toDomain() ?: MangaCover(seriesId = id)
    val alternativeTitles = db.manga_seriesQueries.getAlternativeNames(id).executeAsList().map { it.toDomain() }
    val classifications = db.manga_seriesQueries.getClassifications(id).executeAsList().map { it.toDomain() }
    return MangaSeries(
        id = id,
        url = url,
        slug = slug,
        title = title,
        score = score,
        scoreDistributionId = score_distribution_id,
        titleEN = title_en,
        titleENJP = title_en_jp,
        contentType = content_type,
        contentRating = content_rating,
        publicationStatus = publication_status,
        description = description,
        malID = mal_id,
        usersTracking = users_tracking,
        cover = cover,
        alternativeTitles = alternativeTitles,
        classifications = classifications,

        )
}
