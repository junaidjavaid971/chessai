package app.com.chess.ai.models.global.endgame

data class ChessMove(
    val initialPosition: MovingPieces,
    val finalPosition: MovingPieces
)

data class MovingPieces(val initialPiece: ChessPiece, val finalPiece: ChessPiece)