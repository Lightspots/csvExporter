package ch.grisu118.csv

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

class CSVTest {

  @Before
  fun setUp() {
    CSVKonfig.SEPARATOR = Separator.COMMA
    CSVKonfig.CONVERTER.clear()
  }

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

  @Test
  fun testOtherSeparator() {
    CSVKonfig.SEPARATOR = Separator.SEMICOLON
    val obj = ObjectWithChilds(PrivateObject("123", "CH"), TestObject(321L))
    assertThat(listOf(obj).asCSV().toString(),
      equalTo(CSVBuilder(Separator.SEMICOLON)
        .newLine("pOKode", "pOCombined", "tOid", "tOprop", "tOstring")
        .newLine("123", "CH-123", "321", "Hello World", "Hello World")
        .build()
      ))
  }


  @Test
  fun testBooleanTransform() {
    class BooleanObject(@CSVField private val loremIpsum: Boolean)

    CSVKonfig.CONVERTER[Boolean::class] = {
      if (it as Boolean) "Yes" else "No"
    }

    assertThat(listOf(BooleanObject(true), BooleanObject(false)).asCSV().toString(),
      equalTo(CSVBuilder(Separator.COMMA)
        .newLine("loremIpsum")
        .newLine("Yes")
        .newLine("No")
        .build()
      ))
  }

  @Test
  fun testArrayUseToString() {
    class ArrayObject(@CSVField val array: IntArray)

    val a = intArrayOf(1, 2, 42, 118)
    assertThat(listOf(ArrayObject(a)).asCSV().toString(),
      equalTo(CSVBuilder()
        .newLine("array")
        .newLine(a.toString())
        .build()
      ))
  }

  @Test
  fun testQuotationEscape() {
    class SimpleObj(@CSVField(header = "Field With \"") private val someString: String)

    assertThat(listOf(SimpleObj("Hello\" dude\"\" :)")).asCSV().toString(),
      equalTo(CSVBuilder(Separator.COMMA)
        .newLine("Field With \"\"")
        .newLine("Hello\"\" dude\"\"\"\" :)")
        .build()
      ))
  }

  @Test
  fun testEnum() {
    class SimpleObj(@CSVField private val enum: TestEnum)

    assertThat(listOf(SimpleObj(TestEnum.VALUE)).asCSV().toString(),
      equalTo(CSVBuilder(Separator.COMMA)
        .newLine("enum")
        .newLine("VALUE")
        .build()
      ))
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

  enum class TestEnum {
    VALUE,
    KEY
  }
}


