package ch.grisu118.csv

import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

class CSV internal constructor(private val csvData: TreeNode, private val lineCount: Int) {

  /**
   * @return the csv as string.
   */
  override fun toString(): String {
    return csvData.asCSV(lineCount)
  }

  companion object {
    /**
     * Generates a csv object from the given source collection. Considering all Fields annotated with [CSVField].
     * @param source the source for the csv generation.
     * @return the generated csv object.
     */
    fun generate(source: Collection<Any>): CSV {
      val root = TreeNode("", prefix = "")
      source.forEachIndexed { index, obj ->
        generateWithReflection(root, obj, index)
      }
      return CSV(root, source.size)
    }

    private fun generateWithReflection(parent: TreeNode, obj: Any, index: Int) {
      for (member in obj::class.declaredMemberProperties) {
        val annotation = member.getter.findAnnotation<CSVField>()
        if (annotation == null) {
          val javaAnnotation = member.javaField?.getDeclaredAnnotationsByType(CSVField::class.java)
          if (javaAnnotation?.size == 1) {
            val a = javaAnnotation[0]
            if (!member.getter.isAccessible) {
              member.getter.isAccessible = true
            }
            addValue(parent, a.header, a.order, a.prefix, member.getter.call(obj), index)
          }
        } else {
          if (!member.getter.isAccessible) {
            member.getter.isAccessible = true
          }
          addValue(parent, annotation.header, annotation.order, annotation.prefix, member.getter.call(obj), index)
        }
      }
    }

    private fun addValue(parent: TreeNode, header: String, order: Int, prefix: String, value: Any?, index: Int) {
      if (value != null) {
        val data = parent.children.computeIfAbsent(header, { TreeNode(it, order, prefix) })
        if (value.isPrimitive()) {
          if (data.values.size < index) {
            for (i in 0 until index) {
              data.values.add(i, "")
            }
          }
          data.values.add(index, value.toString())
        } else {
          generateWithReflection(data, value, index)
        }
      }
    }
  }

}