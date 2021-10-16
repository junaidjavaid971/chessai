package app.com.chess.ai.interfaces

import app.com.chess.ai.models.global.training.ChessPiece

interface ChessBoardListener {
    fun onChessSquareSelected(position: Int)
    fun onChessSquareSelectedFirstTime(chessPiece: ChessPiece, position: Int)
    fun onChessSquareMoved(chessPiece: ChessPiece, position: Int)
}