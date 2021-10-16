package app.com.chess.ai.interfaces

import app.com.chess.ai.models.global.endgame.RowCol

interface ChessPieceListener {
    fun chessPieceClicked(fromCol: Int, fromRow: Int, col: Int, row: Int)
    fun showToast(message: String)
    fun drawPiece(possibleMovements: ArrayList<RowCol>)
    fun onPgnUpdated(pgn: String)
}