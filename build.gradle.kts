import org.jetbrains.dokka.gradle.DokkaTask
import kotlinx.knit.KnitPluginExtension

plugins {
  base
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.kotest.multiplatform)
  alias(libs.plugins.arrow.config.kotlin)
  alias(libs.plugins.arrow.config.publish)
  alias(libs.plugins.arrow.config.nexus)
  alias(libs.plugins.dokka)
  alias(libs.plugins.kotlinx.knit)
}

repositories {
  mavenCentral()
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(libs.kotlin.stdlib)
        api(libs.arrow.core)
      }
    }

    commonTest {
      dependencies {
        implementation(libs.kotest.assertions)
        implementation(libs.kotest.property)
        implementation(libs.kotest.engine)
        implementation(libs.kotest.datatest)
        implementation(libs.kotest.arrow)
      }
    }

    val jvmTest by getting {
      dependencies {
        implementation(libs.kotest.junit5)
        implementation(libs.kotlinx.knit.test)
      }
    }
  }
}

configure<KnitPluginExtension> {
  siteRoot = "https://arrow-kt.github.io/arrow-exact/"
}

tasks {
  afterEvaluate {
    withType<DokkaTask>().configureEach {
      outputDirectory.set(rootDir.resolve("docs"))
      moduleName.set("Arrow Exact")
      dokkaSourceSets {
        named("commonMain") {
          includes.from("README.md")
          externalDocumentationLink("https://apidocs.arrow-kt.io")
          sourceLink {
            localDirectory.set(file("src/commonMain/kotlin"))
            remoteUrl.set(uri("https://github.com/arrow-kt/arrow-exact/tree/main/src/commonMain/kotlin").toURL())
            remoteLineSuffix.set("#L")
          }
        }
      }
    }
  }

  getByName("knitPrepare").dependsOn(getTasksByName("dokka", true))
}
