package app.com.chess.ai.test

interface ChessDelegate {
    fun pieceAt(col: Int, row: Int): ChessPiece?
    fun movePiece(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int)
    fun showToast(string: String)
}