import java.util.SortedSet

fun main() {
    fun part1(input: List<String>): Int {
        var cycles = 0
        var currentX = 1
        var totalSignalStrength = 0
        input.forEach { line ->
            val instruction = if (line.contains("noop")) {
                Instruction.Noop
            } else {
                // must be addx instruction
                val x = line
                    .substringAfter("addx ")
                    .toInt()
                Instruction.AddX(x)
            }

            cycles++
            fun checkCycle() {
                when (cycles) {
                    20, 60, 100, 140, 180, 220 -> {
                        println("cycle: $cycles and x: $currentX")
                        totalSignalStrength += cycles * currentX
                    }
                }
            }
            checkCycle()
            if (instruction is Instruction.AddX) {
                cycles++
                checkCycle() // check DURING the cycle
                currentX += instruction.x
            }
        }

        return totalSignalStrength
    }

    fun part2(input: List<String>): String {
        return ""
    }

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}

private sealed class Instruction(val cycles: Int) {
    data class AddX(
        val x: Int
    ) : Instruction(cycles = 2)

    object Noop : Instruction(cycles = 1)
}

