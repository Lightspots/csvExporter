package ch.grisu118.csv

import kotlin.reflect.KClass

object CSVKonfig {
  /**
   * The separator to use for the generation of the csv.
   */
  var SEPARATOR: Separator = Separator.COMMA
  /**
   * Map holding function which can convert a object into string.
   * If a function is present. The generator uses the function to get the value.
   */
  val CONVERTER: MutableMap<KClass<*>, (Any) -> String> = mutableMapOf()
}