fun main() {
    fun part1(input: TreeData): Int {
        val (trees, width, height) = input

        // That's the border of the map, which is always visible.
        val visibleEdge = width * 2 + height * 2 - 4 // Minus the corners

        var insideVisible = 0
        val startIndex = width + 1
        val endIndex = width * height - 1 - width - 1 // Minus the edge

        fun isEdge(index: Int): Boolean {
            return index < width || // first row
                    index >= (width * height - width) || // last row
                    (index + 1) % width == 0 || // right edge
                    index % width == 0 // left edge
        }

        for (i in startIndex..endIndex) {
            if (isEdge(i))
                continue

            val thisTree = trees[i]
            val columnIndex = i % width
            val rowIndex = i / height
            val treesRight = trees.subList(i + 1, i + (width - columnIndex))
            val treesLeft = trees.subList(rowIndex * width, i)
            val treesUp = trees
                .subList(columnIndex, i)
                .getEveryNthElement(width)
            val treesDown = trees
                .subList(i + width, trees.size)
                .getEveryNthElement(width)

            if (treesRight.all { it < thisTree } || treesLeft.all { it < thisTree } ||
                treesUp.all { it < thisTree } || treesDown.all { it < thisTree }) {
                insideVisible++
            }
        }

        println("visible because on edge: $visibleEdge")

        val visibleFromOutside = insideVisible
        println("visible from outside: $visibleFromOutside")

        return visibleEdge + visibleFromOutside
    }

    fun part2(input: List<String>): String {
        return ""
    }

    fun parseData(input: List<String>): TreeData {
        val width = input.first().length
        val height = input.size

        val treesList = input.flatMap { line ->
            line.toCharArray().map { it.digitToInt() }
        }

        val trees = arrayListOf(*treesList.toTypedArray())

        return TreeData(
            trees = trees,
            arrayWidth = width,
            arrayHeight = height,
        )
    }

    val testInput = """
        30373
        25512
        65332
        33549
        35390
        """.trimIndent().lines()

    val input = readInput("Day08")
    // val input = testInput
    val treeData = parseData(input)
    println(part1(treeData))
    println(part2(input))
}

private data class TreeData(
    val trees: ArrayList<Int>,
    val arrayWidth: Int,
    val arrayHeight: Int,
)

private fun <E> List<E>.getEveryNthElement(stepSize: Int): List<E> {
    return buildList {
        for (index in this@getEveryNthElement.indices step stepSize) {
            add(this@getEveryNthElement[index])
        }
    }
}