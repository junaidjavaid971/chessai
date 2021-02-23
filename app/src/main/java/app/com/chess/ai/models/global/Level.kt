package app.com.chess.ai.models.global

class Level {
    var level: String? = null
    var rating: Int? = 0
    var isLocked: Boolean = false

    constructor() {}
    constructor(level: String?, rating: Int?, isLocked: Boolean) {
        this.level = level
        this.rating = rating
        this.isLocked = isLocked
    }
}