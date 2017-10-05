package ch.grisu118.csv

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test

class CSVTest {

  @Test
  fun testSimple() {
    assertThat(CSV.generate(listOf(TestObject(2L))).toString(),
      equalTo(CSVBuilder(Separator.COMMA)
        .newLine("id", "prop", "string")
        .newLine("2", "Hello World", "Hello World")
        .build()
      ))
  }

  @Test
  fun testPrivateFieldAndPropterty() {
    assertThat(listOf(PrivateObject("123541", "HH")).asCSV().toString(),
      equalTo(CSVBuilder(Separator.COMMA)
        .newLine("Kode", "Combined")
        .newLine("123541", "HH-123541")
        .build()
      ))
  }

  @Test
  fun testPrefix() {
    val obj = ObjectWithChilds(PrivateObject("123", "CH"), TestObject(321L))
    assertThat(listOf(obj).asCSV().toString(),
      equalTo(CSVBuilder(Separator.COMMA)
        .newLine("pOKode", "pOCombined", "tOid", "tOprop", "tOstring")
        .newLine("123", "CH-123", "321", "Hello World", "Hello World")
        .build()
      ))
  }

  @Test
  fun testOptionalHeader() {
    class OptionalObj(@CSVField private val loremIpsum: String)

    assertThat(listOf(OptionalObj("junitTest")).asCSV().toString(),
      equalTo(CSVBuilder(Separator.COMMA)
        .newLine("loremIpsum")
        .newLine("junitTest")
        .build()
      ))
  }
}

class PrivateObject(@CSVField("Kode") private val code: String, private val prefix: String) {
  private val combined
    @CSVField(header = "Combined")
    get() = "$prefix-$code"
}

class TestObject(@field:CSVField("id", 1) val id: Long) {

  @field:CSVField("string", 3)
  val string = "Hello World"

  @get:CSVField("prop", 2)
  val prop
    get() = string
}

class ObjectWithChilds(@CSVField("pObject", prefix = "pO") val privateObject: PrivateObject, @CSVField("testObj",
  prefix = "tO") val testObject: TestObject)
