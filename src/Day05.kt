import java.io.File

fun main() {
    fun extractMoveActionsFromInput(input: String): List<Int> {
        return input
            .replaceFirst("move", "")
            .replaceFirst("from", "")
            .replaceFirst("to", "")
            .chunked(3)
            .map { it.trim().toInt() }
    }

    fun part1(input: String): String {
        val (stacksString, moveActionString) = input.split("\n\n")

        val supplyStack = SupplyStack.createSupplyStack(stacksString)

        moveActionString.lines().forEach { moveAction ->
            val (numberOfCrates, stackSource, stackDestination) = extractMoveActionsFromInput(moveAction)

            supplyStack moveSingle numberOfCrates from (stackSource to stackDestination)
        }

        return supplyStack
            .getStackMap()
            .toSortedMap()
            .values
            .map { it.lastOrNull() ?: "" }
            .reduce { acc, s -> acc + s }
    }

    fun part2(input: String): String {
        val (stacksString, moveActionString) = input.split("\n\n")

        val supplyStack = SupplyStack.createSupplyStack(stacksString)

        moveActionString.lines().forEach { moveAction ->
            val (numberOfCrates, stackSource, stackDestination) = extractMoveActionsFromInput(moveAction)

            supplyStack moveAll numberOfCrates from (stackSource to stackDestination)
        }

        return supplyStack
            .getStackMap()
            .toSortedMap()
            .values
            .map { it.lastOrNull() ?: "" }
            .reduce { acc, s -> acc + s }
    }

    val testInputString = """
            [D]    
        [N] [C]    
        [Z] [M] [P]
         1   2   3 
        
        move 1 from 2 to 1
        move 3 from 1 to 3
        move 2 from 2 to 1
        move 1 from 1 to 2
    """.trimIndent()

    val input = File(("src/Day05.txt")).readText()
    // val input = testInputString
    println(part1(input))
    println(part2(input))
}

data class SupplyStack(
    private val stackMap: MutableMap<Int, ArrayList<String>>
) {
    companion object {
        fun createSupplyStack(input: String): SupplyStack {
            val lines = input.lines()
            val mStackMap = mutableMapOf<Int, ArrayList<String>>()

            lines.forEachIndexed { lineIndex, lineString ->
                if (lineIndex == lines.lastIndex) return@forEachIndexed
                // every crate has the width of 4 (including the whitespace next to it)
                val entries = lineString.chunked(4)
                for ((index, element) in entries.withIndex()) {
                    if (element.isNotBlank()) {
                        val char = element
                            .replace("]", "")
                            .replace("[", "")
                            .trim()
                        // always add to the beginning
                        mStackMap.putIfAbsent(index + 1, arrayListOf(char))?.add(0, char)
                    }
                }
            }

            return SupplyStack(mStackMap)
        }
    }

    fun getStackMap() = stackMap.toMap()


    inner class MoveAction(
        private val numberOfCrates: Int,
        private val moveSingle: Boolean,
    ) {
        infix fun from(mPair: Pair<Int, Int>) {
            val fromArray = stackMap.getOrElse(key = mPair.first) { null }
            val toArray = stackMap.getOrElse(key = mPair.second) { null }
            fromArray?.let { elementsFrom ->
                if (elementsFrom.isEmpty()) return@let
                if (moveSingle) {
                    repeat(numberOfCrates) {
                        val lastElement = elementsFrom.removeLast()
                        toArray?.add(lastElement)
                    }
                } else {
                    val elementsToMove = elementsFrom.subList(
                        elementsFrom.size - numberOfCrates,
                        elementsFrom.size
                    )

                    toArray?.addAll(elementsToMove)

                    elementsToMove.clear()
                }
            }
        }
    }


    infix fun moveSingle(numberOfCrates: Int): MoveAction = MoveAction(
        numberOfCrates = numberOfCrates,
        moveSingle = true
    )

    infix fun moveAll(numberOfCrates: Int): MoveAction = MoveAction(
        numberOfCrates = numberOfCrates,
        moveSingle = false
    )
}