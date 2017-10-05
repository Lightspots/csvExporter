package ch.grisu118.csv

import kotlin.reflect.KClass

object CSVKonfig {
  var SEPARATOR: Separator = Separator.COMMA
  val CONVERTER: MutableMap<KClass<*>, (Any) -> String> = mutableMapOf()
}