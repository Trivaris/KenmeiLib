import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.vanniktech.mavenpublish)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.atomicfu)
}

group = "com.trivaris.kenmei"
version = "1.0.0"

sqldelight {
    databases {
        create("MangaDatabase") {
            packageName.set("com.trivaris.kenmei.db.manga")
            srcDirs(listOf("src/commonMain/sqldelight/manga"))
            deriveSchemaFromMigrations.set(true)
            schemaOutputDirectory.set(
                file("src/commonMain/sqldelight/databases/manga")
            )
        }

        create("SessionDatabase") {
            packageName.set("com.trivaris.kenmei.db.session")
            srcDirs(listOf("src/commonMain/sqldelight/session"))
            deriveSchemaFromMigrations.set(true)
            schemaOutputDirectory.set(
                file("src/commonMain/sqldelight/databases/session")
            )
        }
    }
}

kotlin {
    jvm()
    jvmToolchain(11)
    androidTarget {
        publishLibraryVariants("release")
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    linuxX64()
    // iosArm64()
    // iosX64()
    // macosX64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.serialization)
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.io)
                implementation(libs.kermit)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.sqldelight.driver.android)
                implementation(libs.ktor.client.okhttp)
                implementation(libs.kotlinx.coroutines.android)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.sqldelight.driver.jvm)
                implementation(libs.ktor.client.okhttp)
            }
        }
        val nativeMain by creating  {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.sqldelight.driver.native)
                implementation(libs.ktor.client.curl)
                implementation(libs.okio)
            }
        }
        val linuxX64Main by getting { dependsOn(nativeMain) }
        // val iosArm64Main by getting { dependsOn(nativeMain) }
        // val iosX64Main by getting { dependsOn(nativeMain) }
        // val macosX64Main by getting { dependsOn(nativeMain) }

    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}

android {
    namespace = "com.trivaris.kenmei"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}



tasks.register<JavaExec>("runJvmMain") {
    group = "application"
    description = "Runs the jvmMain entry point"

    val target = kotlin.targets.getByName("jvm") as KotlinJvmTarget
    val compilation = target.compilations.getByName("main")

    classpath = files(
        compilation.output.classesDirs,
        compilation.output.resourcesDir,
        compilation.runtimeDependencyFiles
    )

    mainClass.set("com.trivaris.kenmei.MainKt") // adjust if needed
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(group.toString(), "library", version.toString())

    pom {
        name = "KenmeiLib"
        description = "A library to query the Manga Tracking service Kenmei"
        inceptionYear = "2025"
        url = "https://github.com/trivaris/KenmeiLib/"
        licenses {
            license {
                name = "MIT License"
                url = "https://opensource.org/licenses/MIT"
                distribution = "repo"
            }
        }
        developers {
            developer {
                id = "trivaris"
                name = "Trivaris"
                url = "https://github.com/trivaris"
            }
        }
        scm {
            url = "https://github.com/trivaris/KenmeiLib"
            connection = "scm:git:git://github.com/trivaris/KenmeiLib.git"
            developerConnection = "scm:git:ssh://github.com:trivaris/KenmeiLib.git"
        }
    }
}
