package com.trivaris.kenmei.manga

import co.touchlab.kermit.Logger
import com.trivaris.kenmei.session.SessionService
import com.trivaris.kenmei.config.KenmeiConfigProvider
import com.trivaris.kenmei.db.manga.MangaDatabase
import com.trivaris.kenmei.model.dto.EntryDto
import com.trivaris.kenmei.model.dto.EntryRootDto
import com.trivaris.kenmei.model.dto.SourceSiteRootDto
import com.trivaris.kenmei.model.dto.UserTagDto
import com.trivaris.kenmei.model.mapping.toDomain
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath

class MangaService(
    private val client: HttpClient,
    private val db: MangaDatabase,
    private val auth: SessionService
) {

    private val config get() = KenmeiConfigProvider.instance

    suspend fun syncLibrary() {
        val userTags = getUserTags()
        userTags.forEach {
            val tag = it.toDomain()
            tag.insert(db)
            Logger.i("Inserting Tag ${tag.name}")
        }
        val sourceSites = getSourceSites()
        sourceSites?.forEach {
            val site = it.toDomain()
            site.insert(db)
            Logger.i("Inserting Source Site ${site.name}")
        }
        val entries = getEntries()
        entries.forEach {
            val entry = it.toDomain(db)
            entry.insert(db)
            Logger.i("Inserting Entry ${entry.source?.name}")
        }
    }

    private suspend fun getUserTags() =
        client.request {
            method = HttpMethod.Get
            url {
                protocol = URLProtocol.HTTPS
                host = config.baseUrl.host
                encodedPath = "/api/v1/user_tags"
            }
            header(HttpHeaders.Authorization, "Bearer ${auth.getAccessToken()}")
            expectSuccess = true
        }.body<List<UserTagDto>>()

    private suspend fun getSourceSites() =
        client.request {
            method = HttpMethod.Get
            url {
                protocol = URLProtocol.HTTPS
                host = config.baseUrl.host
                encodedPath = "api/v1/manga_source_sites"
            }
            header(HttpHeaders.Authorization, "Bearer ${auth.getAccessToken()}")
            expectSuccess = true
        }.body<SourceSiteRootDto>().data

    private suspend fun getEntries(): List<EntryDto> {
        var page = 1
        var pages = -1
        val entries = mutableListOf<EntryDto>()
        while (page != pages) {
            val response = readDashboardPage(page)
            Logger.i("Syncing page $page of $response.pagy.pages")
            response.entries?.forEach { entry ->
                entries.add(entry)
            }

            pages = response.pagy?.pages ?: pages
            page++
        }
        Logger.i("Sync complete: $page pages processed.")

        return entries
    }

    private suspend fun readDashboardPage(page: Int): EntryRootDto =
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
        }.body<EntryRootDto>()

}