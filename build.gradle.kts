plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.kotest.multiplatform)
  alias(libs.plugins.arrow.config.formatter)
  alias(libs.plugins.arrow.config.kotlin)
}

repositories {
  mavenCentral()
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(libs.kotlin.stdlib)
        implementation(libs.arrow.core)
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
      }
    }
  }
}
