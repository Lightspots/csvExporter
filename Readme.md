# CSV Exporter

[![Build Status](https://travis-ci.org/Grisu118/csvExporter.svg?branch=master)](https://travis-ci.org/Grisu118/csvExporter)
[ ![Download](https://api.bintray.com/packages/grisu118/kotlin/csvExporter/images/download.svg) ](https://bintray.com/grisu118/kotlin/csvExporter/_latestVersion)

CSV Exporter is a small library using reflection to generate a csv from kotlin or java objects.

## Getting started

Add the bintray repository and the dependency to your build script
````groovy
repositories { 
    maven { 
        url "https://dl.bintray.com/grisu118/kotlin" 
     } 
 }
 
dependencies {
    compile 'ch.grisu118:csvExporter:0.5.0'
}
````

To use an Kotlin or Java Object as source for the csv generation.
The fields which should be end up in the csv need the `@CSVField` annotation.

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

