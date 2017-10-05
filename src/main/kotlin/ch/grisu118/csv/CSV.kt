package ch.grisu118.csv

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
    @JvmStatic
    fun generate(source: Collection<Any>): CSV {
      val root = TreeNode("", prefix = "")
      source.forEachIndexed { index, obj ->
        Generator.generateWithReflection(root, obj, index)
      }
      return CSV(root, source.size)
    }
  }

}