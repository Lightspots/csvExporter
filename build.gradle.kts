import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.version
import org.jetbrains.dokka.gradle.DokkaTask

buildscript {
  repositories {
    jcenter()
  }

  dependencies {
    classpath("org.jetbrains.dokka:dokka-gradle-plugin:0.9.17")
  }
}

apply {
  plugin("org.jetbrains.dokka")
}

plugins {
  kotlin("jvm") version ("1.2.71")
  maven
  `maven-publish`
  jacoco
}

group = "ch.grisu118"
version = "1.0.3"

repositories {
  mavenCentral()
}

dependencies {
  compile(kotlin("stdlib-jdk8"))
  compile(kotlin("reflect"))
  testCompile(kotlin("test-junit"))
}

tasks.withType<DokkaTask> {
  outputFormat = "html"
  outputDirectory = "${project.buildDir}/javaDoc"
}

tasks {
  "jacocoTestReport"(JacocoReport::class) {
    reports {
      xml.isEnabled = true
      html.isEnabled = true
    }
  }
}

val sourcesJar by tasks.creating(Jar::class) {
  classifier = "sources"
  from(java.sourceSets["main"].allSource)
}

val dokkaJar by tasks.creating(Jar::class) {
  classifier = "javadoc"
  from("${project.buildDir}/javaDoc")
  dependsOn("dokka")
}

publishing {
  repositories {
    maven {
      name = "GitHubPackages"
      url = uri("https://maven.pkg.github.com/lightspots/csvexporter")
      credentials {
        username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
        password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
      }
    }
  }
  publications {
    (publications) {
      "gpr"(MavenPublication::class) {
        from(components["java"])
        artifact(sourcesJar)
        artifact(dokkaJar)
      }
    }
  }
}