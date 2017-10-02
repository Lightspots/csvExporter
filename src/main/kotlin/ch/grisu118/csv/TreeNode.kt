package ch.grisu118.csv

internal data class TreeNode(private val header: String, private val order: Int = 0, internal val prefix: String) {

  internal val values = mutableListOf<String>()
  internal val children = mutableMapOf<String, TreeNode>()
  private val isLeaf get() = children.isEmpty()

  fun asCSV(lines: Int): String {
    val builder = StringBuilder()
    for (child in children.values.sortedBy { it.order }) {
      child.csvHeader(builder, prefix)
    }
    for (i in 0 until lines) {
      builder.appendln()
      for (child in children.values.sortedBy { it.order }) {
        child.csvValue(builder, i)
      }
    }
    return builder.toString()
  }

  private fun csvHeader(builder: StringBuilder, combinedPrefix: String) {
    if (isLeaf) {
      builder.append("\"$combinedPrefix$header\"${SEPARATOR}")
    } else {
      for (child in children.values.sortedBy { it.order }) {
        child.csvHeader(builder, "$combinedPrefix$prefix")
      }
    }
  }

  private fun csvValue(builder: StringBuilder, index: Int) {
    if (isLeaf) {
      if (values.size > index) {
        builder.append("\"${values[index]}\"${SEPARATOR}")
      } else {
        builder.append("\"\"${SEPARATOR}")
      }
    } else {
      for (child in children.values.sortedBy { it.order }) {
        child.csvValue(builder, index)
      }
    }
  }

  fun toString(pretty: Boolean): String {
    return if (pretty) {
      val builder = StringBuilder()
      debugTree(builder, "", true)
      builder.toString()
    } else {
      toString()
    }
  }

  private fun debugTree(builder: StringBuilder, prefix: String, isTail: Boolean) {
    builder.appendln("$prefix${if (isTail) "└── " else "├── "}$header")
    val values = children.values.sortedBy { it.order }
    for (child in values.take(Math.max(children.size - 1, 0))) {
      child.debugTree(builder, "$prefix${if (isTail) "    " else "│   "}", false)
    }
    if (values.isNotEmpty()) {
      values.last()
        .debugTree(builder, "$prefix${if (isTail) "    " else "│   "}", true)
    }
  }

  override fun toString(): String {
    return "TreeNode(header='$header', order=$order, values=$values, children=$children)"
  }

  companion object {
    val SEPARATOR = ","
  }
}