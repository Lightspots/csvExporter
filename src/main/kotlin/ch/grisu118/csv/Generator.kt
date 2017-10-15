package ch.grisu118.csv

import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

internal object Generator {

  internal fun generateWithReflection(parent: TreeNode, obj: Any, index: Int) {
    for (member in obj::class.declaredMemberProperties) {
      val annotation = member.getter.findAnnotation<CSVField>()
      if (annotation == null) {
        val javaAnnotation = member.javaField?.getDeclaredAnnotationsByType(CSVField::class.java)
        if (javaAnnotation?.size == 1) {
          val a = javaAnnotation[0]
          if (!member.getter.isAccessible) {
            member.getter.isAccessible = true
          }
          addValue(parent, if (a.header.isBlank()) member.name else a.header, a.order, a.prefix,
            member.getter.call(obj), index)
        }
      } else {
        if (!member.getter.isAccessible) {
          member.getter.isAccessible = true
        }
        addValue(parent, if (annotation.header.isBlank()) member.name else annotation.header, annotation.order,
          annotation.prefix, member.getter.call(obj), index)
      }
    }
  }

  private fun addValue(parent: TreeNode, header: String, order: Int, prefix: String, value: Any?, index: Int) {
    if (value != null) {
      val data = parent.children.computeIfAbsent(header, { TreeNode(it, order, prefix) })
      when {
        CSVKonfig.CONVERTER.contains(value::class) -> {
          data.addValue(index, CSVKonfig.CONVERTER[value::class]!!.invoke(value))
        }
        value.isPrimitive() -> {
          data.addValue(index, value.toString())
        }
        value is Array<*>
          || value is IntArray
          || value is ShortArray
          || value is ByteArray
          || value is CharArray
          || value is LongArray
          || value is FloatArray
          || value is DoubleArray
          || value is BooleanArray
          || value is Collection<*> -> {
          data.addValue(index, value.toString())
        }
        value is Enum<*> -> data.addValue(index, value.toString())
        else -> generateWithReflection(data, value, index)
      }
    }
  }
}