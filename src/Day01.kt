import java.io.File

fun main() {
    fun part1(elves: List<Elf>): Int {
        val maxCalories = elves.maxOfOrNull { it.sumOfCaloriesCarried() }
        println("Max calories: $maxCalories")
        return maxCalories ?: 0
    }

    fun part2(elves: List<Elf>): Int {
        val caloriesOfMaxThree = elves.map { it.sumOfCaloriesCarried() }.sorted().takeLast(3).sum()
        println("Total of calories carried by elves carrying most calories: $caloriesOfMaxThree")
        return caloriesOfMaxThree
    }

    val input = File("./src/Day01.txt").readText()
    val elves = Elf.createElves(input)
    part1(elves)
    part2(elves)
}

data class Elf(
    val items: List<Int>
) {
    companion object {
        fun createElves(inputData: String): List<Elf> {
            return inputData.split("\r\n\r\n").map { itemsString ->
                itemsString.lines().map { it.toInt() }
            }.map { integers ->
                Elf(integers)
            }
        }
    }

    fun sumOfCaloriesCarried() = items.sum()
}