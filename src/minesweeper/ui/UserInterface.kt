package minesweeper.ui

import minesweeper.exception.FailureException
import minesweeper.model.Field

interface UserInterface {

    fun displayField(field: Field)

    fun displayWin(field: Field)

    fun displayLose(field: Field, e: FailureException)

    fun readMinesNumber(): Int

    fun readTurn(): TurnInput
}