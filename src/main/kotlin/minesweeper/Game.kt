package minesweeper

import minesweeper.exception.FailureException
import minesweeper.model.Field
import minesweeper.terminal.Terminal
import minesweeper.ui.Action

fun main() {

    val terminal = Terminal()
    val field = Field(mines = terminal.readMinesNumber())

    do {
        terminal.displayField(field)

        try {
            turn(field, terminal)

        } catch (e: FailureException) {
            field.reveal()
            terminal.displayLose(field, e)
            return
        }

    } while (!field.isTheGameEnd())

    terminal.displayWin(field)
}

private fun turn(field: Field, terminal: Terminal) {

    val input = terminal.readTurn()

    when (input.action) {
        Action.MINE -> field.mark(input.x, input.y)
        Action.FREE -> field.free(input.x, input.y)
    }
}