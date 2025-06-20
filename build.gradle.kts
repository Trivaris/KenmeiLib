val ktorVersion: String by project
val sqlDelightVersion: String by project

plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.serialization") version "2.1.20"
    id("app.cash.sqldelight") version "2.1.0"
}

group = "com.trivaris.kenmei"
version = "0.1.0"

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-client-core:${ktorVersion}")
    implementation("io.ktor:ktor-client-cio:${ktorVersion}")
    implementation("io.ktor:ktor-client-content-negotiation:${ktorVersion}")
    implementation("io.ktor:ktor-serialization-kotlinx-json:${ktorVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("app.cash.sqldelight:sqlite-driver:${sqlDelightVersion}")

    testImplementation("io.ktor:ktor-client-mock:${ktorVersion}")
    testImplementation("io.ktor:ktor-client-content-negotiation:${ktorVersion}")
    testImplementation("io.ktor:ktor-serialization-kotlinx-json:${ktorVersion}")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation(kotlin("test"))
}

sqldelight {
    databases {
        create("AuthDatabase") {
            packageName.set("com.trivaris.kenmei.auth")
            schemaOutputDirectory.set(file("src/main/sqldelight/databases"))
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}