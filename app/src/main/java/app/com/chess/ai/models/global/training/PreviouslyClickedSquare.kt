package app.com.chess.ai.models.global.training

class PreviouslyClickedSquare {
    var squarePosition = 100
    var isCorrect = false

    constructor() {}
    constructor(squarePosition: Int?, isCorrect: Boolean) {
        this.squarePosition = squarePosition!!
        this.isCorrect = isCorrect
    }
}