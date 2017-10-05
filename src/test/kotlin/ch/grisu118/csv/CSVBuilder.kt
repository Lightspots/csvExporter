package ch.grisu118.csv

class CSVBuilder(private val separator: Separator) {

  private val lines = mutableListOf<String>()

  fun newLine(vararg line: String): CSVBuilder {
    lines.add(line.joinToString(separator = separator.value, postfix = separator.value) { "\"$it\"" })
    return this
  }

  fun build() = lines.joinToString(separator = System.lineSeparator())

}