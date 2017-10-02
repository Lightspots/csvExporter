val kotlinVersion = "1.1.51"

plugins {
  kotlin("jvm", "1.1.51")
}

group = "ch.grisu118"
version = "0.5.0"

repositories {
  mavenCentral()
}

dependencies {
  compile(kotlin("stdlib-jre8", kotlinVersion))
  compile(kotlin("reflect", kotlinVersion))
  testCompile(kotlin("test-junit", kotlinVersion))
}