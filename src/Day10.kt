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
        // 40 x 6 "pixels", i.e. "#", otherwise "."
        // the x value controls the position of the sprite
        val charList = buildList {
            var cycles = 0
            var currentX = 1 // here, the current x is the middle position of the sprite "###"
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

                fun movePixelAndCheckForDrawing() {
                    val spriteRange = currentX - 1..currentX + 1
                    // debug code
                    /*val debugSpritePosition = List(40) { index -> if (index in spriteRange) "#" else "." }
                        .joinToString(separator = "")
                    println(debugSpritePosition)
                    println("crt draws at position ${(cycles - 1) % 40}")*/
                    if ((cycles - 1) % 40 in spriteRange) {
                        add('#')
                    } else add('.')
                }

                movePixelAndCheckForDrawing()

                if (instruction is Instruction.AddX) {
                    cycles++
                    movePixelAndCheckForDrawing()
                    currentX += instruction.x
                }
            }

        }

        val result = charList.chunked(40).joinToString(separator = "") { crtRow ->
            String(crtRow.toCharArray()) + "\n"
        }
        return result
    }

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}

private sealed interface Instruction {
    data class AddX(
        val x: Int
    ) : Instruction

    object Noop : Instruction
}

