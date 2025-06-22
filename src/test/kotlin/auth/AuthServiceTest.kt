package com.trivaris.kenmei.auth

import com.trivaris.kenmei.db.auth.AuthDatabase
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthServiceTest {
    private val db: AuthDatabase = AuthDatabaseProvider.instance
    private val mockResponse = javaClass
        .getResource("/response-success.json")!!
        .readText()
    private val mockEngine = MockEngine { request ->
        assertEquals("/auth/sessions", request.url.encodedPath)
        respond(
            content = mockResponse,
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
    }
    private val mockClient = HttpClient(mockEngine) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    private val testAuthService: AuthService = AuthService(
        mockClient,
        db
    )

    @Test
    fun testLogin() {
        runBlocking {
            testAuthService.login("kenmei@test.user.com", "testPass")
            val session = db.userSessionQueries.getActive().executeAsOne()
            println(session)
            assertEquals(98764, session.user_id)
            assertEquals("TestUser", session.username)
            assertEquals("testToken", session.token)
        }
    }

    @Test
    fun testActiveChanged() {
        val userLogin =
            Json.decodeFromString<LoginResponse>(javaClass.getResource("/response-success.json")!!.readText())
        val userLoginOne = userLogin.copy(user_id = 1)
        val userLoginTwo = userLogin.copy(user_id = 2)

        runBlocking {
            testAuthService.updateAuth(userLoginOne)
            val sessionOneBefore = db.userSessionQueries.getByUserId(1).executeAsOne()
            assertEquals(1, sessionOneBefore.is_active)

            testAuthService.updateAuth(userLoginTwo)
            val sessionOneAfter = db.userSessionQueries.getByUserId(1).executeAsOne()
            val sessionTwo = db.userSessionQueries.getByUserId(2).executeAsOne()
            assertEquals(0, sessionOneAfter.is_active)
            assertEquals(1, sessionTwo.is_active)

            val sessionActive = db.userSessionQueries.getActive().executeAsOne()
            assertEquals(2, sessionActive.is_active)
        }
    }

    @AfterTest
    fun tearDown() {
        db.userSessionQueries.clearAll()
    }

}