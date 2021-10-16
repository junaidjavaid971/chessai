package app.com.chess.ai.models.global.training

class Displayer {
    var chessSquare: String? = null
    var isCorrect = false

    constructor() {}
    constructor(chessSquare: String?, isCorrect: Boolean) {
        this.chessSquare = chessSquare
        this.isCorrect = isCorrect
    }
}