package minesweeper.ui

enum class Action {

    FREE,
    MINE;

    override fun toString(): String {
        return name
    }
}

fun getAction(value: String?): Action {
    val nonNullValue = value ?: throw IllegalArgumentException("Action cannot be empty")
    return Action.values().find { nonNullValue.toUpperCase() == it.name }
        ?: throw IllegalArgumentException("Action can only be one of these: ${Action.values().joinToString { action -> action.toString() }}")
}