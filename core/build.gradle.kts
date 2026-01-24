import com.android.build.api.dsl.androidLibrary
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.vanniktech.mavenPublish)
    kotlin("plugin.serialization") version "2.2.21"
    id("org.jetbrains.dokka") version "2.1.0"
    signing
}

group = "io.github.lunalobos"
version = "1.0.0-beta.2"

kotlin {


    jvm {
        testRuns.all {
            executionTask.configure {
                jvmArgs("-Xms2g", "-Xmx4g")
            }
        }

    }

    android {
        namespace = "io.github.lunalobos.chess4kt"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        withJava() // enable java compilation support
        withHostTestBuilder {}.configure {}
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    linuxX64()
    js(IR) {
        outputModuleName = "chess4js"
        browser {
        }

        compilerOptions {
            freeCompilerArgs.add("-Xes-long-as-bigint")
            target = "es2015"
        }

        binaries.library(

        )
    }

    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {


        commonMain.dependencies {
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
        }
    }
}

mavenPublishing {

    publishToMavenCentral()

    signAllPublications()

    coordinates(group.toString(), "chess4kt", version.toString())

    pom {
        name = "chess4kt"
        description = "A kotlin multiplatform library for related chess operations"
        inceptionYear = "2025"
        url = "https://github.com/lunalobos/chess4kt/"
        licenses {
            license {
                name = "Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "repo"
            }
        }
        developers {
            developer {
                id = "lunalobosmiguel@gmail.com"
                name = "Miguel Angel Luna Lobos"
                url = "https://github.com/lunalobos"
                email = "lunalobosmiguel@gmail.com"
                organization = "lunalobos"
                organizationUrl = "https://github.com/lunalobos"
            }
        }
        scm {
            url = "https://github.com/lunalobos/chess4kt/"
            connection = "cm:git:https://github.com/lunalobos/chess4kt.git"
            developerConnection = "scm:git:ssh://git@github.com:lunalobos/chess4kt.git"
        }
    }
}

tasks.withType<KotlinJsCompile>().configureEach {
    compilerOptions {
        target = "es2015"
    }
}


