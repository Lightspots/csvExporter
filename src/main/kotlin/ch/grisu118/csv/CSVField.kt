package ch.grisu118.csv

/**
 * @property header The header to display for this field, needs to be unique in an object. If empty the field name is used.
 * @property order The order of the column, as lower as more left it will be.
 * @property prefix An prefix which will be recursively added to the header of the annotated fields. Ignored for primitive fields.
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class CSVField(val header: String = "", val order: Int = 0, val prefix: String = "")