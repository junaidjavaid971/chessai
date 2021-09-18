package app.com.chess.ai.test

import com.github.bhlangonijr.chesslib.Square
import com.github.bhlangonijr.chesslib.move.Move

data class ChessMove(
    val initialPosition: MovingPieces,
    val finalPosition: MovingPieces
)

data class MovingPieces(val initialPiece: ChessPiece, val finalPiece: ChessPiece)