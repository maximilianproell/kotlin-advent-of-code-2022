fun main() {
    fun part1(input: List<String>): Int {
        val allPoints = input.sumOf {
            val game = it.split(" ")
            val opponent = Shape.decryptShape(game.first())
            val me = Shape.decryptShape(game[1])

            // my points for this move
            me.play(opponent)
        }

        println("all points: $allPoints")

        return allPoints
    }

    fun part2(input: List<String>): Int {
        val allPoints = input.sumOf {
            val game = it.split(" ")
            val opponent = Shape.decryptShape(game.first())
            val outcome = Outcome.decryptOutcome(game[1])
            val me = opponent.getShapeForOutcome(outcome)

            // my points for this move
            me.play(opponent)
        }

        println("all points: $allPoints")

        return allPoints
    }

    val input = readInput("Day02")

    part1(input)
    part2(input)
}

enum class Outcome(val points: Int) {
    LOSE(0), DRAW(3), WIN(6);

    companion object {
        fun decryptOutcome(input: String): Outcome {
            return when (input) {
                "X" -> LOSE
                "Y" -> DRAW
                "Z" -> WIN
                else -> error("Can't decrypt input $input")
            }
        }
    }
}

sealed class Shape(val value: Int) {
    companion object {
        fun decryptShape(input: String): Shape {
            return when (input) {
                "A", "X" -> Rock
                "B", "Y" -> Paper
                "C", "Z" -> Scissor
                else -> error("Can't decrypt input $input.")
            }
        }
    }

    abstract fun play(shape: Shape): Int
    abstract fun getShapeForOutcome(outcome: Outcome): Shape

    object Rock : Shape(1) {
        override fun play(shape: Shape): Int {
            return when (shape) {
                is Paper -> Outcome.LOSE.points
                is Rock -> Outcome.DRAW.points
                is Scissor -> Outcome.WIN.points
            } + value
        }

        override fun getShapeForOutcome(outcome: Outcome): Shape {
            return when (outcome){
                Outcome.LOSE -> Scissor
                Outcome.DRAW -> this
                Outcome.WIN -> Paper
            }
        }
    }

    object Paper : Shape(2) {
        override fun play(shape: Shape): Int {
            return when (shape) {
                is Paper -> Outcome.DRAW.points
                is Rock -> Outcome.WIN.points
                is Scissor -> Outcome.LOSE.points
            } + value
        }

        override fun getShapeForOutcome(outcome: Outcome): Shape {
            return when (outcome){
                Outcome.LOSE -> Rock
                Outcome.DRAW -> this
                Outcome.WIN -> Scissor
            }
        }
    }

    object Scissor : Shape(3) {
        override fun play(shape: Shape): Int {
            return when (shape) {
                is Paper -> Outcome.WIN.points
                is Rock -> Outcome.LOSE.points
                is Scissor -> Outcome.DRAW.points
            } + value
        }

        override fun getShapeForOutcome(outcome: Outcome): Shape {
            return when (outcome){
                Outcome.LOSE -> Paper
                Outcome.DRAW -> this
                Outcome.WIN -> Rock
            }
        }
    }
}
