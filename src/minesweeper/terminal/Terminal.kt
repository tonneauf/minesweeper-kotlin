package minesweeper.terminal

import minesweeper.exception.FailureException
import minesweeper.model.Cell
import minesweeper.model.CellState
import minesweeper.model.CellType
import minesweeper.model.Field
import minesweeper.ui.TurnInput
import minesweeper.ui.UserInterface
import minesweeper.ui.getAction

class Terminal : UserInterface {

    private val columnSeparator = "|"
    private val rowSeparator = "-"

    override fun displayField(field: Field) {
        val fieldRepresentation = StringBuilder()

        // Header
        fieldRepresentation.append(" ").append(columnSeparator)
        for (i in field.board.indices) {
            fieldRepresentation.append(i + 1)
        }
        fieldRepresentation.append(columnSeparator).appendln()
        fieldRepresentation.append(rowSeparator).append(columnSeparator)
        for (i in field.board.indices) {
            fieldRepresentation.append(rowSeparator)
        }
        fieldRepresentation.append(columnSeparator).appendln()

        // Main
        for (j in field.board[0].indices) {
            fieldRepresentation.append(j + 1).append(columnSeparator)
            for (i in field.board.indices) {
                fieldRepresentation.append(displayCell(field.board[i][j]))
            }
            fieldRepresentation.append(columnSeparator).appendln()
        }

        // Footer
        fieldRepresentation.append(rowSeparator).append(columnSeparator)
        for (i in field.board.indices) {
            fieldRepresentation.append(rowSeparator)
        }
        fieldRepresentation.append(columnSeparator)

        println()
        println(fieldRepresentation.toString())
    }

    override fun displayWin(field: Field) {
        displayField(field)
        println("Congratulations! You found all mines!")
    }

    override fun displayLose(field: Field, e: FailureException) {
        displayField(field)
        println(e)
    }

    override fun readMinesNumber(): Int {

        println("How many mines do you want on the field?")
        do {
            val line: String? = readLine()
            try {
                return line?.toInt() ?: throw IllegalArgumentException("The number of mines must not be empty.")

            } catch (e: NumberFormatException) {
                println("The number of mines should be an integer.")
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        } while (true)
    }

    override fun readTurn(): TurnInput {

        println("Set/delete mines marks (x and y coordinates):")
        do {
            val action = readLine() ?: throw IllegalArgumentException("Action could not be empty")
            try {
                val splitAction = action.split(" ")
                if (splitAction.size != 3) {
                    throw IllegalArgumentException("The input must be of format \"x y action\"")
                }

                return TurnInput(
                    splitAction[0].toInt() - 1,
                    splitAction[1].toInt() - 1,
                    getAction(splitAction[2])
                )

            } catch (e: NumberFormatException) {
                println("A coordinate should be an integer.")
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        } while (true)
    }

    private fun displayCell(cell: Cell): String {
        return when (cell.state) {
            CellState.UNMARKED -> "."
            CellState.MARKED -> "*"
            CellState.FREE -> when (cell.type) {
                CellType.MINE -> "X"
                CellType.NONE -> "/"
                else -> cell.type.minesAround?.toString()!!
            }
        }
    }
}