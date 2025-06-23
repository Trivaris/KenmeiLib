package com.trivaris.kenmei.library.mappers

import com.trivaris.kenmei.library.domain.CoverImage as DomainCoverImage
import com.trivaris.kenmei.library.domain.CoverImageUrls as DomainCoverImageUrls
import com.trivaris.kenmei.library.domain.EntryAttributes as DomainEntryAttributes
import com.trivaris.kenmei.library.domain.LatestChapter as DomainLatestChapter
import com.trivaris.kenmei.library.domain.LibraryEntry as DomainLibraryEntry
import com.trivaris.kenmei.library.domain.Links as DomainLinks
import com.trivaris.kenmei.library.domain.MangaSourceChapter as DomainMangaSourceChapter
import com.trivaris.kenmei.library.domain.ReadChapter as DomainReadChapter
import com.trivaris.kenmei.library.dto.CoverImageDto
import com.trivaris.kenmei.library.dto.CoverImageUrlsDto
import com.trivaris.kenmei.library.dto.EntryAttributesDto
import com.trivaris.kenmei.library.dto.LatestChapterDto
import com.trivaris.kenmei.library.dto.LibraryEntryDto
import com.trivaris.kenmei.library.dto.LinksDto
import com.trivaris.kenmei.library.dto.MangaSourceChapterInfoDto
import com.trivaris.kenmei.library.dto.ReadChapterDto
import com.trivaris.kenmei.db.library.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

/*
 * Extension mappers between DTO, database and domain representations
 * of library related models.
 */

// region DTO -> Domain

fun LibraryEntryDto.toDomain(): DomainLibraryEntry = DomainLibraryEntry(
    id = id,
    mangaSourceId = mangaSourceId,
    mangaSeriesId = mangaSeriesId,
    userTagIds = userTagIds,
    attributes = attributes?.toDomain(),
    links = links?.toDomain(),
    readChapter = readChapter?.toDomain(),
    mangaSourceChapter = mangaSourceChapter?.toDomain()
)

fun EntryAttributesDto.toDomain(): DomainEntryAttributes = DomainEntryAttributes(
    title = title,
    cover = cover?.toDomain(),
    status = status,
    hidden = hidden == true,
    favourite = favourite == true,
    unread = unread == true,
    notes = notes,
    score = score,
    lastReadAt = lastReadAt,
    createdAt = createdAt,
    latestChapter = latestChapter?.toDomain(),
    contentRating = contentRating
)

fun CoverImageDto.toDomain(): DomainCoverImage = DomainCoverImage(
    webp = webp.toDomain(),
    jpeg = jpeg.toDomain()
)

fun CoverImageUrlsDto.toDomain(): DomainCoverImageUrls = DomainCoverImageUrls(
    large = large,
    small = small
)

fun LinksDto.toDomain(): DomainLinks = DomainLinks(
    seriesUrl = seriesUrl,
    mangaSeriesUrl = mangaSeriesUrl
)

fun ReadChapterDto.toDomain(): DomainReadChapter = DomainReadChapter(
    volume = volume,
    chapter = chapter,
    title = title
)

fun LatestChapterDto.toDomain(): DomainLatestChapter = DomainLatestChapter(
    id = id,
    url = url,
    chapterIdentifier = chapterIdentifier,
    volume = volume,
    chapter = chapter,
    title = title,
    releasedAt = releasedAt,
    locked = locked
)

fun MangaSourceChapterInfoDto.toDomain(): DomainMangaSourceChapter = DomainMangaSourceChapter(
    id = id,
    url = url,
    chapterIdentifier = chapterIdentifier,
    volume = volume,
    chapter = chapter,
    title = title,
    releasedAt = releasedAt,
    locked = locked
)

// endregion

// region DB -> Domain

fun Cover.toDomain(): DomainCoverImage = DomainCoverImage(
    webp = DomainCoverImageUrls(large_webp, small_webp),
    jpeg = DomainCoverImageUrls(large_jpeg, small_jpeg)
)

fun Link.toDomain(): DomainLinks = DomainLinks(
    seriesUrl = series_url,
    mangaSeriesUrl = kenmei_url
)

fun Read_chapter.toDomain(): DomainReadChapter = DomainReadChapter(
    volume = volume,
    chapter = chapter_number,
    title = title
)

fun Latest_chapter.toDomain(): DomainLatestChapter = DomainLatestChapter(
    id = id,
    url = url,
    chapterIdentifier = chapter_identifier,
    volume = volume,
    chapter = chapter_number,
    title = title,
    releasedAt = released_at,
    locked = locked?.let { it == 1L }
)

fun Source_chapter.toDomain(): DomainMangaSourceChapter = DomainMangaSourceChapter(
    id = id,
    url = url,
    chapterIdentifier = chapter_identifier,
    volume = volume,
    chapter = chapter_number,
    title = title,
    releasedAt = released_at,
    locked = locked?.let { it == 1L }
)

fun Library_entry.toDomain(
    link: Link? = null,
    cover: Cover? = null,
    readChapter: Read_chapter? = null,
    latestChapter: Latest_chapter? = null,
    sourceChapter: Source_chapter? = null
): DomainLibraryEntry {
    val tags: List<String>? = user_tag_ids?.let { Json.decodeFromString(it) }

    val attrs = DomainEntryAttributes(
        title = title,
        cover = cover?.toDomain(),
        status = status,
        hidden = hidden == 1L,
        favourite = favourite == 1L,
        unread = unread == 1L,
        notes = notes,
        score = score,
        lastReadAt = last_read_at,
        createdAt = created_at,
        latestChapter = latestChapter?.toDomain(),
        contentRating = content_rating
    )

    return DomainLibraryEntry(
        id = id,
        mangaSourceId = manga_source_id,
        mangaSeriesId = manga_series_id,
        userTagIds = tags,
        attributes = attrs,
        links = link?.toDomain(),
        readChapter = readChapter?.toDomain(),
        mangaSourceChapter = sourceChapter?.toDomain()
    )
}

// endregion

// region Domain -> DB

fun DomainLibraryEntry.toDbModel(
    linkId: Long?,
    coverId: Long?,
    readChapterId: Long?,
    latestChapterId: Long?,
    sourceChapterId: Long?
): Library_entry = Library_entry(
    id = id,
    link_id = linkId,
    cover_id = coverId,
    read_chapter_id = readChapterId,
    latest_chapter_id = latestChapterId,
    source_chapter_id = sourceChapterId,
    title = attributes?.title,
    manga_source_id = mangaSourceId,
    manga_series_id = mangaSeriesId,
    user_tag_ids = userTagIds?.let { Json.encodeToString(it) },
    status = attributes?.status,
    hidden = if (attributes?.hidden == true) 1 else 0,
    favourite = if (attributes?.favourite == true) 1 else 0,
    unread = if (attributes?.unread == true) 1 else 0,
    notes = attributes?.notes,
    score = attributes?.score,
    last_read_at = attributes?.lastReadAt,
    created_at = attributes?.createdAt,
    content_rating = attributes?.contentRating
)

fun DomainLinks.toDbModel(id: Long? = null): Link = Link(
    id = id ?: 0L,
    series_url = seriesUrl,
    kenmei_url = mangaSeriesUrl
)

fun DomainCoverImage.toDbModel(id: Long? = null): Cover = Cover(
    id = id ?: 0L,
    large_webp = webp.large,
    small_webp = webp.small,
    large_jpeg = jpeg.large,
    small_jpeg = jpeg.small
)

fun DomainReadChapter.toDbModel(id: Long? = null): Read_chapter = Read_chapter(
    id = id ?: 0L,
    volume = volume,
    chapter_number = chapter,
    title = title
)

fun DomainLatestChapter.toDbModel(): Latest_chapter = Latest_chapter(
    id = id,
    url = url,
    chapter_identifier = chapterIdentifier,
    volume = volume,
    chapter_number = chapter,
    title = title,
    released_at = releasedAt,
    locked = locked?.let { if (it) 1L else 0L }
)

fun DomainMangaSourceChapter.toDbModel(libraryEntryId: Long): Source_chapter = Source_chapter(
    id = id,
    library_entry_id = libraryEntryId,
    url = url,
    chapter_identifier = chapterIdentifier,
    volume = volume,
    chapter_number = chapter,
    title = title,
    released_at = releasedAt,
    locked = locked?.let { if (it) 1L else 0L }
)

// endregion


