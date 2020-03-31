package minesweeper.model

enum class CellType(val minesAround: Int?) {

    MINE(null),
    NONE(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8);
}

fun valueOf(minesAround: Int): CellType {
    return CellType.values().find { it.minesAround == minesAround }!!
}