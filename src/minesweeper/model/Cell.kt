package minesweeper.model

import minesweeper.exception.FailureException

class Cell(val type: CellType, state: CellState) {

    var state: CellState = state
        private set

    fun mark() {
        state = when (state) {
            CellState.MARKED -> CellState.UNMARKED
            CellState.UNMARKED -> CellState.MARKED
            CellState.FREE -> CellState.FREE
        }
    }

    fun free() {
        state = when (state) {
            CellState.MARKED -> CellState.FREE
            CellState.UNMARKED -> if (type == CellType.MINE) throw FailureException(
                "You stepped on a mine and failed!"
            ) else CellState.FREE
            CellState.FREE -> CellState.FREE
        }
    }

    fun reveal() {
        if (type == CellType.MINE) {
            state = CellState.FREE
        }
    }
}
