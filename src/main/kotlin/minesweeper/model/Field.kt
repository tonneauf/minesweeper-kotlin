package minesweeper.model

import kotlin.random.Random

class Field(private val sizeX: Int = 9, private val sizeY: Int = 9, private val mines: Int = 10) {

    private var initialized = false

    var board: Array<Array<Cell>> = arrayOf()
        private set

    init {
        if (mines > sizeX * sizeY - 9) {
            throw IllegalArgumentException("Too much mines asked...Can't go over ${sizeX * sizeY - 9}")
        }

        initializeBoard()
    }

    fun isTheGameEnd(): Boolean {
        if (!initialized) {
            return false
        }

        var allMinesMarked = true
        var allNonMineCellAreFree = true

        exploreBoard { x, y ->
            val cell = board[x][y]

            // Mine not marked or empty cell not free, makes the game still playing
            if (cell.type == CellType.MINE && cell.state != CellState.MARKED) {
                allMinesMarked = false
            }
            if (cell.type != CellType.MINE && cell.state != CellState.FREE) {
                allNonMineCellAreFree = false
            }
        }

        return allMinesMarked || allNonMineCellAreFree
    }

    fun mark(x: Int, y: Int) {
        board[x][y].mark()
    }

    fun free(x: Int, y: Int) {
        if (!initialized) {
            initialize(x, y)
        }

        board[x][y].free()

        if (board[x][y].type == CellType.NONE) {
            exploreAroundCell(x, y) { otherCellX, otherCellY ->
                if (board[otherCellX][otherCellY].type != CellType.MINE && board[otherCellX][otherCellY].state != CellState.FREE) {
                    free(otherCellX, otherCellY)
                }
            }
        }
    }

    fun reveal() {
        exploreBoard { x, y ->
            board[x][y].reveal()
        }
    }

    private fun initialize(firstTurnInputX: Int, firstTurnInputY: Int) {
        initializeCellWithMines(firstTurnInputX, firstTurnInputY)
        initializeCellWithoutMines()
        initialized = true
    }

    private fun initializeBoard() {
        for (i in 1..sizeX) {
            board += Array(sizeY) {
                Cell(
                    type = CellType.NONE,
                    state = CellState.UNMARKED
                )
            }
        }
    }

    private fun initializeCellWithMines(firstTurnInputX: Int, firstTurnInputY: Int) {

        var remainingMines = mines
        val excludedPositions = getExcludedMinePositions(firstTurnInputX, firstTurnInputY)

        while (remainingMines > 0) {

            val x = Random.nextInt(0, sizeX)
            val y = Random.nextInt(0, sizeY)

            if (!excludedPositions.contains(Pair(x, y)) && board[x][y].type != CellType.MINE) {
                board[x][y] = Cell(CellType.MINE, board[x][y].state)
                remainingMines--
            }
        }
    }

    private fun getExcludedMinePositions(firstTurnInputX: Int, firstTurnInputY: Int): HashSet<Pair<Int, Int>> {
        val excludedValues = HashSet<Pair<Int, Int>>()

        excludedValues.add(Pair(firstTurnInputX, firstTurnInputY))

        exploreAroundCell(firstTurnInputX, firstTurnInputY) { otherCellX, otherCellY ->
            excludedValues.add(Pair(otherCellX, otherCellY))
        }

        return excludedValues
    }

    private fun initializeCellWithoutMines() {

        exploreBoard { x, y ->
            if (board[x][y].type == CellType.MINE) {
                return@exploreBoard
            }

            var minesAround = 0

            exploreAroundCell(x, y) { otherCellX, otherCellY ->
                if (board[otherCellX][otherCellY].type == CellType.MINE) {
                    minesAround++
                }
            }

            if (minesAround != 0) {
                board[x][y] = Cell(valueOf(minesAround), board[x][y].state)
            }
        }
    }

    private fun exploreBoard(action: (Int, Int) -> Unit) {
        for (x in board.indices) {
            for (y in board[x].indices) {
                action(x, y)
            }
        }
    }

    private fun exploreAroundCell(x: Int, y: Int, action: (Int, Int) -> Any) {
        for (k in -1..1) {
            for (l in -1..1) {
                // The current cell
                if (k == 0 && l == 0) {
                    continue
                }
                // Out of the board
                if (x + k < 0 || y + l < 0 || x + k >= sizeX || y + l >= sizeY) {
                    continue
                }

                action(x + k, y + l)
            }
        }
    }
}