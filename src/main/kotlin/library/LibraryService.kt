package com.trivaris.kenmei.library

import com.trivaris.kenmei.auth.AuthService
import com.trivaris.kenmei.auth.models.MangaEntryUpdate
import com.trivaris.kenmei.auth.models.MangaEntryWrapper
import com.trivaris.kenmei.core.KenmeiConfigProvider
import com.trivaris.kenmei.db.library.LibraryDatabase
import com.trivaris.kenmei.library.domain.LibraryEntry as DomainLibraryEntry
import com.trivaris.kenmei.library.mappers.toDomain
import com.trivaris.kenmei.library.dto.CoverImageDto
import com.trivaris.kenmei.library.dto.LatestChapterDto
import com.trivaris.kenmei.library.dto.LibraryEntryDto
import com.trivaris.kenmei.library.dto.LibraryResponseDto
import com.trivaris.kenmei.library.dto.LinksDto
import com.trivaris.kenmei.library.dto.MangaSourceChapterDto
import com.trivaris.kenmei.library.dto.MangaSourceChapterInfoDto
import com.trivaris.kenmei.library.dto.MangaSourceChaptersResponseDto
import com.trivaris.kenmei.library.dto.ReadChapterDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

class LibraryService(
    private val client: HttpClient,
    private val db: LibraryDatabase,
    private val auth: AuthService
) {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val config get() = KenmeiConfigProvider.instance
    private fun Boolean.toLong() = if (this) 1L else 0L

    suspend fun syncLibrary() {
        var page = 1
        var pages = -1
        while (page != pages) {
            val response = readPage(page)
            logger.info("Syncing page {} of {}", page, response.pagy.pages)
            response.entries.forEach {
                insertLibraryEntry(it)
            }

            pages = response.pagy.pages
            page++
        }
        logger.info("Sync complete: {} pages processed.", page)
    }

    private suspend fun readPage(page: Int): LibraryResponseDto =
        client.request {
            method = HttpMethod.Get
            url {
                protocol = URLProtocol.HTTPS
                host = config.baseUrl.host
                encodedPath = "/api/v2/manga_entries"
                parameters["page"] = page.toString()
            }
            header(HttpHeaders.Authorization, "Bearer ${auth.getAccessToken()}")
            expectSuccess = true
        }.body<LibraryResponseDto>()

    suspend fun markLatestChapterRead(mangaId: Long) {
        val series = db.library_entryQueries.getById(mangaId).executeAsOneOrNull()
        series?.latest_chapter_id?.let {
            markChapterRead(mangaId, it)
        }
    }

    fun getAll(): List<DomainLibraryEntry> =
        db.library_entryQueries.getAllEntries().executeAsList().map { entry ->
            val link = entry.link_id?.let { db.linkQueries.getById(it).executeAsOneOrNull() }
            val cover = entry.cover_id?.let { db.coverQueries.getById(it).executeAsOneOrNull() }
            val read = entry.read_chapter_id?.let { db.read_chapterQueries.getById(it).executeAsOneOrNull() }
            val latest = entry.latest_chapter_id?.let { db.latest_chapterQueries.getById(it).executeAsOneOrNull() }
            val source = entry.source_chapter_id?.let { db.source_chapterQueries.getById(it).executeAsOneOrNull() }
            entry.toDomain(link, cover, read, latest, source)
        }

    suspend fun markChapterRead(mangaId: Long, sourceChapterId: Long) {
        client.request {
            method = HttpMethod.Put
            url {
                protocol = URLProtocol.HTTPS
                host = config.baseUrl.host
                encodedPath = "/api/v2/manga_entries/${mangaId}"
            }
            header(HttpHeaders.Authorization, "Bearer ${auth.getAccessToken()}")
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(
                MangaEntryWrapper(MangaEntryUpdate(sourceChapterId))
            )
            expectSuccess = true
        }
    }

    private suspend fun fetchMangaChapters(mangaId: Long, saveInLibrary: Boolean): List<MangaSourceChapterDto> {
        val response = client.request {
            method = HttpMethod.Get
            url {
                protocol = URLProtocol.HTTPS
                host = config.baseUrl.host
                encodedPath = "/api/v1/manga_source_chapters"
                parameters.append("manga_source_ids[]", mangaId.toString())
                parameters.append("query", "")
            }

            header(HttpHeaders.Authorization, "Bearer ${auth.getAccessToken()}")
            accept(ContentType.Application.Json)
            expectSuccess = true
        }.body<MangaSourceChaptersResponseDto>().data

        if (saveInLibrary) {
            response.forEach {
                insertSourceChapter(it.toMangaSourceChapterInfoDto(), it.mangaSourceId)
            }
        }

        return response
    }

    suspend fun insertLibraryEntry(entry: LibraryEntryDto) = withContext(Dispatchers.IO) {
        db.transaction {
            val linksId = entry.links
                ?.let { insertLinks(it) }

            val coverId = entry.attributes?.cover
                ?.let { insertCover(it) }

            val readChapterId = entry.readChapter
                ?.let { insertReadChapter(it) }

            val latestChapterId = entry.attributes?.latestChapter
                ?.let { insertLatestChapter(it) }

            val sourceChapterId = entry.mangaSourceChapter
                ?.let { insertSourceChapter(it, entry.id) }

            insertManga(
                manga = entry,
                linksId = linksId,
                coverId = coverId,
                readChapterId = readChapterId,
                latestChapterId = latestChapterId,
                sourceChapterId = sourceChapterId
            )

            if (entry.attributes == null)
                logger.warn("Entry with null attributes, id={}", entry.id)
            else
                logger.debug("Inserted manga entry id={}, title={}", entry.id, entry.attributes.title)
        }
    }

    private fun insertLinks(links: LinksDto): Long =
        db.linkQueries.insertOrReplaceLink(
            series_url = links.seriesUrl,
            kenmei_url = links.mangaSeriesUrl
        ).let { db.linkQueries.lastInsertRowId().executeAsOne() }

    private fun insertCover(cover: CoverImageDto): Long =
        db.coverQueries.insertOrReplaceCover(
            large_webp = cover.webp.large,
            small_webp = cover.webp.small,
            large_jpeg = cover.jpeg.large,
            small_jpeg = cover.jpeg.small,
        ).let { db.coverQueries.lastInsertRowId().executeAsOne() }

    private fun insertReadChapter(readChapter: ReadChapterDto): Long =
        db.read_chapterQueries.insertOrReplaceReadChapter(
            volume = readChapter.volume,
            chapter_number = readChapter.chapter,
            title = readChapter.title,
        ).let { db.read_chapterQueries.lastInsertRowId().executeAsOne() }

    private fun insertLatestChapter(latestChapter: LatestChapterDto): Long =
        db.latest_chapterQueries.insertOrReplaceLatestChapter(
            id = latestChapter.id,
            url = latestChapter.url,
            chapter_identifier = latestChapter.chapterIdentifier,
            volume = latestChapter.volume,
            chapter_number = latestChapter.chapter,
            title = latestChapter.title,
            released_at = latestChapter.releasedAt,
            locked = (latestChapter.locked == true).toLong()
        ).let { latestChapter.id }

    private fun insertSourceChapter(
        sourceChapter: MangaSourceChapterInfoDto,
        libraryEntryId: Long
    ): Long =
        db.source_chapterQueries.insertOrReplaceSourceChapter(
            id = sourceChapter.id,
            library_entry_id = libraryEntryId,
            url = sourceChapter.url,
            chapter_identifier = sourceChapter.chapterIdentifier,
            volume = sourceChapter.volume,
            chapter_number = sourceChapter.chapter,
            title = sourceChapter.title,
            released_at = sourceChapter.releasedAt,
            locked = (sourceChapter.locked == true).toLong()
        ).let { sourceChapter.id }

    private fun insertManga(
        manga: LibraryEntryDto,
        linksId: Long?,
        coverId: Long?,
        readChapterId: Long?,
        latestChapterId: Long?,
        sourceChapterId: Long?,
    ) =
        db.library_entryQueries.insertOrReplaceLibraryEntry(
            id = manga.id,
            link_id = linksId,
            cover_id = coverId,
            read_chapter_id = readChapterId,
            latest_chapter_id = latestChapterId,
            source_chapter_id = sourceChapterId,
            title = manga.attributes?.title,
            manga_source_id = manga.mangaSourceId,
            manga_series_id = manga.mangaSeriesId,
            user_tag_ids = Json.encodeToString(manga.userTagIds),
            status = manga.attributes?.status,
            hidden = (manga.attributes?.hidden == true).toLong(),
            favourite = (manga.attributes?.favourite == true).toLong(),
            unread = (manga.attributes?.unread == true).toLong(),
            notes = manga.attributes?.notes,
            score = manga.attributes?.score,
            last_read_at = manga.attributes?.lastReadAt,
            created_at = manga.attributes?.createdAt,
            content_rating = manga.attributes?.contentRating,
        )
}