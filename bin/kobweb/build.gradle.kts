plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    application
}

group = "com.varabyte.kobweb"
version = "0.3.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://us-central1-maven.pkg.dev/varabyte-repos/public") }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.kotlinx.cli)
    implementation(libs.konsole)
    implementation(libs.jgit)
    implementation(libs.freemarker)
    implementation(libs.kaml)
}

application {
    applicationDefaultJvmArgs = listOf("-Dkobweb.version=${version}")
    mainClass.set("MainKt")
}