plugins {
  kotlin("jvm") version ("1.4.30")
  id("org.jetbrains.dokka") version "1.4.30"
  maven
  `maven-publish`
  jacoco
}

group = "ch.grisu118"
version = "1.1.0"

repositories {
  mavenCentral()
  jcenter()
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))
  implementation(kotlin("reflect"))
  testImplementation(kotlin("test-junit"))
}

tasks {
  "jacocoTestReport"(JacocoReport::class) {
    reports {
      xml.isEnabled = true
      html.isEnabled = true
    }
  }
}

java {
  withJavadocJar()
  withSourcesJar()
}

tasks.javadoc.configure {
  dependsOn("dokkaHtml")
  setDestinationDir(File(buildDir, "dokka/html"))
}

tasks.assemble.configure {
  doLast {
    logger.lifecycle("::set-output name=version::$version")
  }
}

publishing {
  repositories {
    maven {
      name = "GitHubPackages"
      url = uri("https://maven.pkg.github.com/lightspots/csvexporter")
      credentials {
        username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
        password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
      }
    }
  }
  publications {
    create<MavenPublication>("gpr") {
      from(components["java"])
    }
  }
}