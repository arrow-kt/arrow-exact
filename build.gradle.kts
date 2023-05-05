plugins {
  kotlin("multiplatform") version "1.8.21" apply true
  id("io.kotest.multiplatform") version "5.6.0" apply true
}

group "org.example"
version "1.0"

repositories {
  mavenCentral()
  maven {
    url = uri("https://oss.sonatype.org/content/repositories/snapshots")
  }
}

kotlin {
  jvm()

  js(IR) {
    browser()
    nodejs()
  }

  linuxX64()

  mingwX64()

  iosArm64()
  iosSimulatorArm64()
  iosX64()
  macosArm64()
  macosX64()
  tvosArm64()
  tvosSimulatorArm64()
  tvosX64()
  watchosArm32()
  watchosArm64()
  watchosSimulatorArm64()
  watchosX64()

  sourceSets {
    commonMain {
      dependencies {
        implementation(kotlin("stdlib-common"))
        implementation("io.arrow-kt:arrow-core:1.2.0-RC")
      }
    }

    commonTest {
      dependencies {
        implementation("io.kotest:kotest-property:5.6.1")
        implementation("io.kotest:kotest-framework-engine:5.6.1")
        implementation("io.kotest:kotest-assertions-core:5.6.1")
        implementation("io.kotest:kotest-framework-datatest:5.6.1")
        implementation("io.kotest.extensions:kotest-assertions-arrow:1.3.3")
      }
    }

    val jvmTest by getting {
      dependencies {
        implementation("io.kotest:kotest-runner-junit5-jvm:5.6.1")
      }
    }
  }
}
