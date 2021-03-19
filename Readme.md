# CSV Exporter

[![Build Status](https://github.com/Lightspots/csvExporter/actions/workflows/build.yml/badge.svg)](https://github.com/Lightspots/csvExporter/actions/workflows/build.yml)
[ ![Download](https://img.shields.io/github/v/release/Lightspots/csvExporter)](https://github.com/Lightspots/csvExporter/packages/681691)

CSV Exporter is a small library using reflection to generate a csv from kotlin or java objects.

## Getting started

Add the github repository and the dependency to your build script

````kotlin
repositories {
  maven {
    name = "GitHubPackages"
    url = uri("https://maven.pkg.github.com/lightspots/csvexporter")
    credentials {
      username = project.findProperty("gpr.user") ?: System.getenv("USERNAME")
      password = project.findProperty("gpr.key") ?: System.getenv("TOKEN")
    }
  }
}
 
dependencies {
  implementation("ch.grisu118:csvexporter:1.1.0")
}
````

To use an Kotlin or Java Object as source for the csv generation. The fields which should be end up in the csv need
the `@CSVField` annotation.

On the annotation you can specify three optional parameters
* header: String - The name used for the header, needs to be unique for an object. If empty the field name is used.
* order: Int - The sort order of the columns in the csv, `Int.MIN_VALUE` is first, `Int.MAX_VALUE` last.
* prefix: String - Set a prefix which is prepended to the headers of the fields value type.

Supported Locations:
* Fields
* Property Getter

Supported Types:
* Primitive JVM Types
* Custom Objects (fields of them annotated with `@CSVField`)

### Kotlin

Simply call `.asCSV()` on a collection of objects annotated with `@CSVField`

````kotlin
class Sample(@CSVField private val loremIpsum: String)

println(listOf(Sample("ReadmeSample")).asCSV())
````
This code above will print the CSV to the console.

### Java
In Java you have to use the `CSV.generate(Collection)` to generate the CSV.

*In Java only field annotations work! Getter Annotations are ignored! If needed, create an Issue*

````java
class POJO {

    @CSVField(header = "Name", order = 1)
    private final String name;

    @CSVField(header = "Active", order = 2)
    private final boolean active;

    @CSVField
    private final Integer number;

    POJO(String name, boolean active, int number) {
      this.name = name;
      this.active = active;
      this.number = number;
    }
}

System.out.println(CSV.generate(Collection.singleton(new POJO("ReadmeSample", true, 42))))
````

## Configuration

You can configure the separator to use. This is done on the `CSVKonfig` object.

````kotlin
CSVKonfig.SEPARATOR = Separator.SEMICOLON
````

Also you can set additional converter function to retrieve value of an object.

So, instead of having boolean written as `true` and `false`,
you can convert them to `yes` / `no` or something else

````kotlin
CSVKonfig.CONVERTER[Boolean::class] = {
  if (it as Boolean) "Yes" else "No"
}
````