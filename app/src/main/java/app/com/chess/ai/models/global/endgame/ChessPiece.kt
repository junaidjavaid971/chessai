package app.com.chess.ai.models.global.endgame

import com.github.bhlangonijr.chesslib.Square

data class ChessPiece(
    val col: Int,
    val row: Int,
    val player: ChessPlayer,
    val square: Square,
    val rank: ChessRank,
    var resId: Int
)