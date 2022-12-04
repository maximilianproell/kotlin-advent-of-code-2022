fun main() {
    fun part1(input: List<String>): Int {
        return input.mapNotNull { items ->
            val rucksack = Rucksack.createRucksack(items)
            val sharedItem = rucksack.findDuplicateItem()
            sharedItem?.priority
        }.sum()
    }

    fun part2(input: List<String>): Int {
        return buildList {
            for (index in input.indices step 3) {
                val char = findCharacterAppearingInAll(
                    input[index + 0],
                    input[index + 1],
                    input[index + 2],
                )
                add(char?.priority ?: 0)
            }
        }.sum()
    }

    val testInputString = """
        vJrwpWtwJgWrhcsFMMfFFhFp
        jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
        PmmdzqPrVvPwwTWBwg
        wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
        ttgJtRGJQctTZtZT
        CrZsJsPPZsGzwwsLwLmpwMDw
    """.trimIndent()

    val input = readInput("Day03")
    // val input = testInputString.lines()
    println(part1(input))
    println(part2(input))
}

private fun findCharacterAppearingInAll(vararg lines: String): Char? {
    val maps = lines.map { string ->
        string.groupBy { it }
    }
    val combinedString = lines.reduce { x1, x2 -> x1 + x2 }
    val combinedMap = combinedString.groupBy { it }

    combinedMap.keys.forEach { key ->
        if (maps.all { map -> map.contains(key) })
            return key
    }
    return null
}

/**
 * Converts this Char to the priority defined in the problem statement:
 * Lowercase item types a through z have priorities 1 through 26.
 * Uppercase item types A through Z have priorities 27 through 52.
 */
private val Char.priority: Int
    get() {
        return if (this.isUpperCase()) (this - 38).code
        else (this - 96).code
    }

data class Rucksack(
    val compartment1: String,
    val compartment2: String
) {
    companion object {
        fun createRucksack(input: String): Rucksack {
            val (firstHalf, secondHalf) = input.chunked(input.length / 2)
            return Rucksack(
                compartment1 = firstHalf,
                compartment2 = secondHalf,
            )
        }
    }

    fun findDuplicateItem(): Char? {
        val groupedChars = compartment2.groupBy { it }

        compartment1.forEach { char ->
            if (groupedChars.contains(char))
                return char
        }
        return null
    }
}