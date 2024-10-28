import org.jetbrains.intellij.platform.gradle.extensions.intellijPlatform

plugins {
    kotlin("jvm") version "2.0.20"
    id("org.jetbrains.intellij.platform") version "2.1.0"
}

group = "fr.craftmywebsite"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {

        phpstorm("2024.2")
        bundledPlugin("com.jetbrains.php")

        pluginVerifier()
        zipSigner()
        instrumentationTools()
    }
}

kotlin {
    jvmToolchain(21)
}

tasks.named("buildSearchableOptions") {
    enabled = false
}