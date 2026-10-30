@file:Suppress("UNCHECKED_CAST")

try {
    val processEnv = Class.forName("java.lang.ProcessEnvironment")
    val theEnv = processEnv.getDeclaredField("theEnvironment").apply { isAccessible = true }.get(null) as MutableMap<String, String>
    theEnv.remove("ANDROID_PREFS_ROOT")
    val theCiEnv = processEnv.getDeclaredField("theCaseInsensitiveEnvironment").apply { isAccessible = true }.get(null) as MutableMap<String, String>
    theCiEnv.remove("ANDROID_PREFS_ROOT")
} catch (_: Throwable) {}

pluginManagement { repositories { google(); mavenCentral(); gradlePluginPortal() } }
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}
dependencyResolutionManagement { repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS); repositories { google(); mavenCentral() } }
rootProject.name = "DailyMate"
include(":app")
