package app.com.chess.ai.test

interface ChessPieceListener {
    fun chessPieceClicked(fromCol: Int, fromRow: Int, col: Int, row: Int)
    fun showToast(message: String)
}