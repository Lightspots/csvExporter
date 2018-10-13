import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.version
import org.jetbrains.dokka.gradle.DokkaTask
import java.util.Date

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
  id("com.jfrog.bintray") version ("1.8.4")
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

bintray {
  user = if (project.hasProperty("bintrayUser")) project.property("bintrayUser").toString() else ""
  key = if (project.hasProperty("bintrayApiKey")) project.property("bintrayApiKey").toString() else ""
  setPublications("Bintray")
  pkg = PackageConfig()
  pkg.repo = "kotlin"
  pkg.name = "csvExporter"
  pkg.setLicenses("MIT")
  pkg.vcsUrl = "https://github.com/Grisu118/csvExporter.git"
  pkg.version = VersionConfig()
  pkg.version.name = project.version.toString()
  pkg.version.desc = ""
  pkg.version.released = Date().toString()
  pkg.version.vcsTag = project.version.toString()
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
  publications {
    (publications) {
      "Bintray"(MavenPublication::class) {
        from(components["java"])
        artifact(sourcesJar)
        artifact(dokkaJar)
      }
    }
  }
}