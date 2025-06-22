val ktorVersion: String by project
val sqlDelightVersion: String by project
val logbackVersion: String by project
val slf4jVersion: String by project
val kotlinxVersion: String by project

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
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxVersion")
    implementation("app.cash.sqldelight:sqlite-driver:$sqlDelightVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.slf4j:slf4j-api:$slf4jVersion")

    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    testImplementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation(kotlin("test"))
}

sqldelight {
    databases {
        create("AuthDatabase") {
            packageName.set("com.trivaris.kenmei.db.auth")
            srcDirs("src/main/sqldelight/auth")
            schemaOutputDirectory.set(file("src/main/sqldelight/databases/auth"))
        }
        create("LibraryDatabase") {
            packageName.set("com.trivaris.kenmei.db.library")
            srcDirs("src/main/sqldelight/library")
            schemaOutputDirectory.set(file("src/main/sqldelight/databases/library"))
        }
    }
}

sourceSets {
    val main by getting
    create("devMain") {
        kotlin.srcDir("src/devMain/kotlin")
        resources.srcDir("src/devMain/resources")
        compileClasspath += main.compileClasspath
        runtimeClasspath += main.runtimeClasspath
        compileClasspath += main.output
        runtimeClasspath += main.output
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<JavaExec>("runDev") {
    group = "application"
    description = "Runs the devMain entrypoint"
    classpath = sourceSets["devMain"].runtimeClasspath
    mainClass.set("com.trivaris.kenmei.MainKt") // <-- FIXED
}

kotlin {
    jvmToolchain(21)
}