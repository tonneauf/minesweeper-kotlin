package minesweeper.exception

class FailureException(message: String) : Throwable(message, null) {

    override fun toString(): String {
        return message ?: "The game failed"
    }
}