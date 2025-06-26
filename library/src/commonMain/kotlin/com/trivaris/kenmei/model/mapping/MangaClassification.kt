package com.trivaris.kenmei.model.mapping

import com.trivaris.kenmei.db.manga.Manga_classification
import com.trivaris.kenmei.model.domain.MangaClassification

fun Manga_classification.toDomain() =
    MangaClassification(
        id = id,
        category = category,
        name = name
    )
