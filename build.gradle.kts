import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.version
import org.jetbrains.dokka.gradle.DokkaTask
import java.util.Date

val kotlinVersion = "1.1.51"

buildscript {
  repositories {
    jcenter()
  }

  dependencies {
    classpath("org.jetbrains.dokka:dokka-gradle-plugin:0.9.15")
  }
}

apply {
  plugin("org.jetbrains.dokka")
}

plugins {
  kotlin("jvm", "1.1.51")
  id("com.jfrog.bintray") version ("1.7.3")
  maven
  `maven-publish`
}

group = "ch.grisu118"
version = "0.7.1"

repositories {
  mavenCentral()
}

dependencies {
  compile(kotlin("stdlib-jre8", kotlinVersion))
  compile(kotlin("reflect", kotlinVersion))
  testCompile(kotlin("test-junit", kotlinVersion))
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