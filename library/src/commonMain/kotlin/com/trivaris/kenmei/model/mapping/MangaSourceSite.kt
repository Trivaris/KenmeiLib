package com.trivaris.kenmei.model.mapping

import com.trivaris.kenmei.db.manga.Manga_source_site
import com.trivaris.kenmei.model.domain.MangaSourceSite
import com.trivaris.kenmei.model.dto.SourceSiteDto
import com.trivaris.kenmei.model.types.toBoolean
import com.trivaris.kenmei.model.types.toLong

fun Manga_source_site.toDomain() =
    MangaSourceSite(
        id = id,
        name = name,
        siteType = site_type,
        online = online.toBoolean(),
        disabled = disabled.toBoolean(),
    )

fun SourceSiteDto.toDomain() =
    MangaSourceSite(
        id = id,
        name = name,
        siteType = siteType,
        online = online,
        disabled = disabled,
    )