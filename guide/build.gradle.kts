import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id(libs.plugins.kotlin.multiplatform.get().pluginId)
}

repositories {
  mavenCentral()
}

kotlin {
  jvm()
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(rootProject.project)
      }
    }
    val jvmTest by getting {
      dependencies {
        implementation(libs.kotlinx.knit.test)
        implementation(libs.kotest.assertions)
        implementation(libs.kotest.property)
        implementation(libs.kotest.junit5)
        implementation(libs.kotest.engine)
      }
    }
  }
}

tasks {
  withType<Test>().configureEach {
    useJUnitPlatform()
    testLogging {
      setExceptionFormat("full")
      setEvents(listOf("passed", "skipped", "failed", "standardOut", "standardError"))
    }
  }
}
