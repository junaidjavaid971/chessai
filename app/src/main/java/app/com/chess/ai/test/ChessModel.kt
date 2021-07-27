package app.com.chess.ai.test

import android.util.Log
import app.com.chess.ai.R
import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.Piece
import com.github.bhlangonijr.chesslib.Side
import com.github.bhlangonijr.chesslib.Square
import com.github.bhlangonijr.chesslib.move.Move

class ChessModel {
    var piecesBox = mutableSetOf<ChessPiece>()
    var rowColBinding = hashMapOf<RowCol, Int>()
    var possibleMovements = ArrayList<RowCol>()
    private val TAG = "MainActivityTAG"
    private val MTAG = "MovementTAG"
    var emptySquares: List<Square>? = null
    var whiteRookSquares: List<Square>? = null
    var blackBishopSquares: List<Square>? = null
    var whiteBishopSquares: List<Square>? = null
    var blackRookSquares: List<Square>? = null
    var blackKnightSquares: List<Square>? = null
    var whiteKnightSquares: List<Square>? = null
    var blackPawnSquares: List<Square>? = null
    var whitePawnSquares: List<Square>? = null
    var whiteKingSquares: List<Square>? = null
    var blackKingSquares: List<Square>? = null
    var whiteQueenSquares: List<Square>? = null
    var blackQueenSquares: List<Square>? = null
    lateinit var board: Board
    var origin: ChessPiece? = null

    init {
        initRowColBinding()
        initBoard()
        reset()
        movePiece(0, 0, 0, 1)
        Log.d(TAG, toString())
    }

    private fun initRowColBinding() {
        var count = 0
        for (row in 0..7) {
            for (col in 0..7) {
                rowColBinding[RowCol(row, col)] = count
                count++
            }
        }
    }

    private fun initBoard() {
        board = Board()
        Log.d(TAG, board.fen)
        board.loadFromFen(board.fen)

        emptySquares =
            board.getPieceLocation(Piece.NONE)
        blackBishopSquares =
            board.getPieceLocation(Piece.BLACK_BISHOP)
        whiteBishopSquares =
            board.getPieceLocation(Piece.WHITE_BISHOP)
        blackRookSquares =
            board.getPieceLocation(Piece.BLACK_ROOK)
        whiteRookSquares =
            board.getPieceLocation(Piece.WHITE_ROOK)
        blackKnightSquares =
            board.getPieceLocation(Piece.BLACK_KNIGHT)
        whiteKnightSquares =
            board.getPieceLocation(Piece.WHITE_KNIGHT)
        blackPawnSquares =
            board.getPieceLocation(Piece.BLACK_PAWN)
        whitePawnSquares =
            board.getPieceLocation(Piece.WHITE_PAWN)
        whiteKingSquares =
            board.getPieceLocation(Piece.WHITE_KING)
        blackKingSquares =
            board.getPieceLocation(Piece.BLACK_KING)
        whiteQueenSquares =
            board.getPieceLocation(Piece.WHITE_QUEEN)
        blackQueenSquares =
            board.getPieceLocation(Piece.BLACK_QUEEN)
    }

    fun possibleMovements(value: ChessPiece?) {
        possibleMovements.clear()
        val legalMoves = board.legalMoves()
        for (move in legalMoves) {
            if (move.from.toString().equals(value?.square?.value().toString())) {
                origin = value
                for (item in rowColBinding) {
                    val index = Square.valueOf(move.to.toString()).ordinal
                    if (item.value.toString() == index.toString()) {
                        possibleMovements.add(item.key)
                        Log.d("PossibleMoves", item.value.toString())
                    }
                }
            }
        }
    }

    fun makeMove(destination: ChessPiece?) {
        val move = Move(origin?.square, destination?.square)
        board.doMove(move)
        Log.d(MTAG, board.fen.toString())
    }

    fun sideToMove(): Side? {
        return board.sideToMove
    }

    fun movePiece(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int) {
        if (fromCol == toCol && fromRow == toRow) return
        val movingPiece = pieceAt(fromCol, fromRow) ?: return

        pieceAt(toCol, toRow)?.let {
            if (it.player == movingPiece.player) {
                return
            }
            piecesBox.remove(it)
        }
        piecesBox.remove(movingPiece)
        piecesBox.add(
            ChessPiece(
                toCol,
                toRow,
                movingPiece.player,
                movingPiece.square,
                movingPiece.rank,
                movingPiece.resId
            )
        )
    }

    fun pieceAt(col: Int, row: Int): ChessPiece? {
        for (piece in piecesBox) {
            if (col == piece.col && row == piece.row) {
                return piece
            }
        }
        return null
    }

    fun reset() {
        piecesBox.removeAll(piecesBox)
        for (i in 0..1) {
            piecesBox.add(
                ChessPiece(
                    0 + i * 7,
                    0,
                    ChessPlayer.WHITE,
                    whiteRookSquares!![i],
                    ChessRank.ROOK,
                    R.drawable.rook_white
                )
            )
            piecesBox.add(
                ChessPiece(
                    0 + i * 7,
                    7,
                    ChessPlayer.BLACK,
                    blackRookSquares!![i],
                    ChessRank.ROOK,
                    R.drawable.rook_black
                )
            )

            piecesBox.add(
                ChessPiece(
                    1 + i * 5,
                    0,
                    ChessPlayer.WHITE,
                    whiteKnightSquares!![i],
                    ChessRank.KNIGHT,
                    R.drawable.knight_white
                )
            )
            piecesBox.add(
                ChessPiece(
                    1 + i * 5,
                    7,
                    ChessPlayer.BLACK,
                    blackKnightSquares!![i],
                    ChessRank.KNIGHT,
                    R.drawable.knight_black
                )
            )

            piecesBox.add(
                ChessPiece(
                    2 + i * 3,
                    0,
                    ChessPlayer.WHITE,
                    whiteBishopSquares!![i],
                    ChessRank.BISHOP,
                    R.drawable.bishop_white
                )
            )
            piecesBox.add(
                ChessPiece(
                    2 + i * 3,
                    7,
                    ChessPlayer.BLACK,
                    blackBishopSquares!![i],
                    ChessRank.BISHOP,
                    R.drawable.bishop_black
                )
            )
        }

        for (i in 0..7) {
            piecesBox.add(
                ChessPiece(
                    i,
                    1,
                    ChessPlayer.WHITE,
                    whitePawnSquares!![i],
                    ChessRank.PAWN,
                    R.drawable.pawn_white
                )
            )
            piecesBox.add(
                ChessPiece(
                    i,
                    6,
                    ChessPlayer.BLACK,
                    blackPawnSquares!![i],
                    ChessRank.PAWN,
                    R.drawable.pawn_black
                )
            )
        }
        for (row in 0..7) {
            for (col in 2..5) {
                piecesBox.add(
                    ChessPiece(
                        row,
                        col,
                        ChessPlayer.EMPTY,
                        Square.squareAt(rowColBinding.get(RowCol(row, col))!!),
                        ChessRank.NONE,
                        0
                    )
                )
            }
        }
        piecesBox.add(
            ChessPiece(
                3,
                0,
                ChessPlayer.WHITE,
                whiteQueenSquares!![0],
                ChessRank.QUEEN,
                R.drawable.queen_white
            )
        )
        piecesBox.add(
            ChessPiece(
                3,
                7,
                ChessPlayer.BLACK,
                blackQueenSquares!![0],
                ChessRank.QUEEN,
                R.drawable.queen_black
            )
        )

        piecesBox.add(
            ChessPiece(
                4,
                0,
                ChessPlayer.WHITE,
                whiteKingSquares!![0],
                ChessRank.KING,
                R.drawable.king_white
            )
        )
        piecesBox.add(
            ChessPiece(
                4,
                7,
                ChessPlayer.BLACK,
                blackKingSquares!![0],
                ChessRank.KING,
                R.drawable.king_black
            )
        )
    }

    override fun toString(): String {
        var desc = " \n"
        for (row in 7 downTo 0) {
            desc += "$row"
            for (col in 0..7) {
                val piece = pieceAt(col, row)
                if (piece == null) {
                    desc += " ."
                } else {
                    val white = piece.player == ChessPlayer.WHITE
                    desc += " "
                    when (piece.rank) {
                        ChessRank.KING -> {
                            desc += if (white) "k" else "K"
                        }
                        ChessRank.QUEEN -> {
                            desc += if (white) "q" else "Q"
                        }
                        ChessRank.BISHOP -> {
                            desc += if (white) "b" else "B"
                        }
                        ChessRank.ROOK -> {
                            desc += if (white) "r" else "R"
                        }
                        ChessRank.KNIGHT -> {
                            desc += if (white) "n" else "N"
                        }
                        ChessRank.PAWN -> {
                            desc += if (white) "p" else "P"
                        }
                    }
                }
            }
            desc += "\n"
        }
        desc += "  0 1 2 3 4 5 6 7"
        return desc
    }

    fun returnPossibleMovements(): ArrayList<RowCol> {
        if (possibleMovements.isEmpty()) {
            return ArrayList()
        } else {
            return possibleMovements
        }
    }
}