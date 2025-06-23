package com.trivaris.kenmei.library.domain

/** Domain representation of a cover image */
data class CoverImageUrls(
    val large: String?,
    val small: String?
)

data class CoverImage(
    val webp: CoverImageUrls,
    val jpeg: CoverImageUrls
)

/** Domain representation of a chapter */
data class ReadChapter(
    val volume: String?,
    val chapter: Double?,
    val title: String?
)

/** Domain representation of the latest known chapter */
data class LatestChapter(
    val id: Long,
    val url: String?,
    val chapterIdentifier: String?,
    val volume: String?,
    val chapter: Double?,
    val title: String?,
    val releasedAt: String?,
    val locked: Boolean?
)

/** Domain representation of an available source chapter */
data class MangaSourceChapter(
    val id: Long,
    val url: String?,
    val chapterIdentifier: String?,
    val volume: String?,
    val chapter: Double?,
    val title: String?,
    val releasedAt: String?,
    val locked: Boolean?
)

data class Links(
    val seriesUrl: String?,
    val mangaSeriesUrl: String?
)

data class EntryAttributes(
    val title: String?,
    val cover: CoverImage?,
    val status: Long?,
    val hidden: Boolean,
    val favourite: Boolean,
    val unread: Boolean,
    val notes: String?,
    val score: String?,
    val lastReadAt: String?,
    val createdAt: String?,
    val latestChapter: LatestChapter?,
    val contentRating: String?
)

/**
 * Complete domain representation of a library entry combining
 * network DTOs and database entities.
 */
data class LibraryEntry(
    val id: Long,
    val mangaSourceId: Long,
    val mangaSeriesId: Long,
    val userTagIds: List<String>?,
    val attributes: EntryAttributes?,
    val links: Links?,
    val readChapter: ReadChapter?,
    val mangaSourceChapter: MangaSourceChapter?
)
