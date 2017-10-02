package ch.grisu118.csv

internal fun Any.isPrimitive(): Boolean {
  return this is Byte
    || this is Short
    || this is Char
    || this is Int
    || this is Long
    || this is Float
    || this is Double
    || this is Boolean
    || this is String
}

/**
 * Generates a csv object from this source collection. Considering all Fields annotated with [CSVField].
 * @return the generated csv object.
 */
fun Collection<Any>.asCSV(): CSV {
  return CSV.generate(this)
}