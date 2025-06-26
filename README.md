**Project Overview**

This repository contains a Kotlin/JVM library for interacting with the “Kenmei” API. It uses SQLDelight for database access and Ktor as the HTTP client. The project provides services for user authentication and for synchronizing a user’s manga library.

Main Components
---
```
src/main/kotlin/
├─ core/           // utilities for DB creation, HTTP client, and config handling
├─ auth/           // authentication models and AuthService
├─ library/        // library DTOs, domain models, mappers, and LibraryService
└─ ...

src/devMain/kotlin/
└─ Main.kt         // sample “dev” entry point that syncs the library

auth/src/main/sqldelight/auth/           // SQLDelight schema for user sessions
library/src/main/sqldelight/library/     // SQLDelight schema for library entries and related tables
```

The `build.gradle.kts` file configures Kotlin 2.1, SQLDelight 2.1, and Ktor 3.1. It sets up two SQLDelight databases:
- AuthDatabase (for user_session records)
- LibraryDatabase (for manga entry tables)

## Database Schema

---

### Key tables (from `library` database):

* **manga\_series** – core metadata about a manga series. Fields include `title`, `slug`, `score`, `content_type`, and a reference to `cover_id`. Related to `alternative_title`, `manga_series_classification`, and `manga_source`.

* **manga\_entry** – user-specific tracking for a manga series. References `manga_series_id`, `manga_source_id`, `read_chapter_id`, and `manga_source_chapter_id`. Includes flags like `favourite`, `unread`, `hidden`, plus fields like `score`, `last_read_at`, and `status`. Indexes recommended on `user_id`, `favourite`, and `status`.

* **manga\_entry\_user\_tag** – join table linking `manga_entry` to `user_tag`. Includes foreign keys with cascading deletes and an index on `user_tag_id`.

* **user\_tag** – user-defined tags, identified by `id`, with `name`, `description`, and `entry_count`.

* **manga\_source\_sites** – defines external source sites (e.g., MangaDex, MangaPlus). Tracks `online`, `disabled`, and `site_type`.

* **manga\_source** – represents a manga series on a specific source site. Links to `manga_series` and `manga_source_sites`. Fields include `region_locked`, `site_active`, and chapter metadata like `latest_chapter_id`.

* **manga\_chapter** – chapters for a specific `manga_source`. Includes `chapter_number`, `title`, `released_at`, and `locked`.

* **read\_chapter** – tracks the user's last read chapter per entry, storing `volume`, `chapter_number`, and `title`.

* **manga\_series\_classification** – join table linking `manga_series` to `classification_category`. Enables series categorization.

* **classification\_category** – stores classification metadata such as genre or theme categories.

* **classification\_category\_name** – multilingual or alias support for classification categories.

* **alternative\_title** – alternative titles for a `manga_series`, for localization or aliases.

* **cover** – stores URLs for cover images in multiple formats and resolutions.



### Auth database:

* **user\_sessions** – standalone table storing `user_id` and auth `token`. Managed separately from the main library database and accessed via `ATTACH DATABASE` if cross-querying is needed.


Services and Domain Models
---
- **AuthService** handles login and token refresh. It uses `HttpClient` to hit the Kenmei API and persists tokens in the `AuthDatabase`.

- **LibraryService** syncs the user’s library from the API, converts network DTOs to database models, and writes them to `LibraryDatabase`. It exposes methods like `syncLibrary` and `markLatestChapterRead`.

- **Domain models** in `library/domain` map to DTOs and database entities via extension functions in `library/mappers`.

Development Entry Point and Tests
---
The `src/devMain/kotlin/Main.kt` file shows how to create `KenmeiClient`, connect to the databases, and perform a sync.

This project demonstrates a typical Kotlin backend/desktop library setup with SQLDelight-managed schemas, Ktor-based networking, and a small domain layer for representing manga library data.