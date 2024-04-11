// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
//    alias(libs.plugins.hilt) apply false
    id("com.diffplug.spotless") version "6.22.0" apply true
    id("io.gitlab.arturbosch.detekt") version "1.19.0" apply true
}

allprojects {
    apply(plugin = "com.diffplug.spotless")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    dependencies { detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.3") }
}

subprojects {
    spotless {
        kotlin {
            target("**/*.kt")
            targetExclude("**/generated/**", "**/*Test.kt")
            ktlint()
            indentWithSpaces(4)
            endWithNewline()
            trimTrailingWhitespace()
        }
        kotlinGradle {
            target("**/*.gradle.kts")
            trimTrailingWhitespace()
            indentWithSpaces(4)
            endWithNewline()
        }
    }
    detekt {
        baseline = file("$rootDir/config/detekt/detekt-baseline.xml")
        config = files("$rootDir/config/detekt/detekt.yml")
        buildUponDefaultConfig = true

        reports {
            html.enabled = true
            html.destination = file("build/reports/tests/detekt.html")
        }
    }
}

true // Needed to make the Suppress annotation work for the plugins block
