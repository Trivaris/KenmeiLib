package com.trivaris.kenmei.model.mapping

import com.trivaris.kenmei.db.manga.Manga_alternative_title
import com.trivaris.kenmei.model.domain.MangaAlternativeTitle

fun Manga_alternative_title.toDomain() =
    MangaAlternativeTitle(
        seriesId = manga_series_id,
        title = title,
    )