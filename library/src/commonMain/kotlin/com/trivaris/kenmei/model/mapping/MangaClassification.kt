package com.trivaris.kenmei.model.mapping

import com.trivaris.kenmei.db.manga.Manga_classification
import com.trivaris.kenmei.model.domain.MangaClassification
import com.trivaris.kenmei.model.dto.SeriesClassificationDto

fun Manga_classification.toDomain() =
    MangaClassification(
        id = id,
        category = category,
        name = name
    )

fun SeriesClassificationDto.toDomain() =
    MangaClassification(
        id = id,
        category = category,
        name = name,
    )