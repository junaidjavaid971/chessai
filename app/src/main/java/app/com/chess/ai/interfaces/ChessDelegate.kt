package app.com.chess.ai.interfaces

import app.com.chess.ai.models.global.endgame.ChessPiece

interface ChessDelegate {
    fun pieceAt(col: Int, row: Int): ChessPiece?
    fun possibleMovementAt(col: Int, row: Int): ChessPiece?
    fun makeMove(col: Int, row: Int)
    fun movePiece(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int)
    fun showToast(string: String)
}