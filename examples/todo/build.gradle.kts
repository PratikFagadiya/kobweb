import com.varabyte.kobweb.gradle.application.util.kobwebServerJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.jetbrains.compose)
    id(libs.plugins.kobweb.application.get().pluginId)
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

group = "todo"
version = "1.0-SNAPSHOT"

kotlin {
    js(IR) {
        moduleName = "todo"
        browser {
            commonWebpackConfig {
                outputFileName = "todo.js"
            }
        }
        binaries.executable()
    }
    jvm {
        tasks.withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "11"
        }
        kobwebServerJar("todo.jar")
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(libs.kotlinx.serialization.json)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(libs.kobweb.core)
                implementation(libs.kobweb.silk.core)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.kobweb.api)
            }
        }
    }
}