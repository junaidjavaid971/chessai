package app.com.chess.ai.test

import android.util.Log
import app.com.chess.ai.R
import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.Piece
import com.github.bhlangonijr.chesslib.Side
import com.github.bhlangonijr.chesslib.Square
import com.github.bhlangonijr.chesslib.move.Move
import java.lang.Exception

class ChessModel {
    constructor(chessPieceListener: ChessPieceListener) {
        this.chessPieceListener = chessPieceListener
    }

    constructor()

    private val TAG = "MainActivityTAG"
    private val MTAG = "MovementTAG"

    val piecesBox = arrayListOf<ChessPiece>()
    var rowColBinding = hashMapOf<RowCol, Int>()
    var possibleMovements = ArrayList<RowCol>()

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
    var chessPieceListener: ChessPieceListener? = null

    lateinit var board: Board
    var origin: ChessPiece? = null

    private var fromCol: Int = -1
    private var fromRow: Int = -1

    init {
        initRowColBinding()
        initBoard()
        reset()
        Log.d(TAG, toString())
    }

    private fun initRowColBinding() {
        var count = 0
        for (row in 0..7) {
            for (col in 0..7) {
                rowColBinding[RowCol(row, col)] = count
                count++

                Log.d("ROWCOL", "Row: " + row + " - Col: " + col + " - Index: " + count)
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

    fun makeMove(col: Int, row: Int) {
        if (possibleMovements.contains(RowCol(row, col))) {
            Log.d(MTAG, "FEN Before: " + board.fen)
            val destination = pieceAt(col, row)
            val move = Move(origin?.square, destination?.square)
            try {
                if (board.isMoveLegal(move, false)) {
                    possibleMovements.clear()
                    board.doMove(move)
                    Log.d(MTAG, "FEN After: " + board.fen)
                }
            } catch (e: Exception) {
                print(e.localizedMessage)
            }
            chessPieceListener?.chessPieceClicked(fromCol, fromRow, col, row)
        } else {
            val temp = pieceAt(col, row)
            if (temp?.player == ChessPlayer.EMPTY) return
            if (((board.sideToMove == Side.WHITE) && (temp?.player == ChessPlayer.WHITE)) || ((board.sideToMove == Side.BLACK) && (temp?.player == ChessPlayer.BLACK))) {
                possibleMovements(temp)
                if (possibleMovements.isNotEmpty()) {
                    chessPieceListener?.drawPiece(possibleMovements)
                    fromCol = col
                    fromRow = row
                }
            } else {
                chessPieceListener?.showToast("Opponent's turn")
            }
        }
    }

    fun drawPossibleMovements(possibleMovements: ArrayList<RowCol>) {
        for (item in possibleMovements) {
            val piece = pieceAt(item.col, item.row)
            piecesBox.remove(piece)

            piecesBox.add(
                ChessPiece(
                    piece?.col!!,
                    piece.row,
                    piece.player,
                    piece.square,
                    piece.rank,
                    R.drawable.ic_dot
                )
            )
        }
    }

    fun movePiece(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int) {
        if (fromCol == toCol && fromRow == toRow) return
        val movingPiece = pieceAt(fromCol, fromRow) ?: return
        val piece = pieceAt(toCol, toRow)
        piece?.let {
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
                piece?.square!!,
                movingPiece.rank,
                movingPiece.resId
            )
        )
        piecesBox.add(
            ChessPiece(
                fromCol,
                fromRow,
                piece.player,
                movingPiece.square,
                piece.rank,
                0
            )
        )
    }

    fun clearPossibleMovements() {
        for (item in 0 until piecesBox.size) {
            val possiblePiece = pieceAt(piecesBox[item].col, piecesBox[item].row)
            if (possiblePiece?.resId == R.drawable.ic_dot) {
                piecesBox.remove(possiblePiece)
                var resId = 0
                if (possiblePiece.player == ChessPlayer.BLACK) {
                    when (possiblePiece.rank) {
                        ChessRank.NONE -> resId = 0
                        ChessRank.BISHOP -> resId = R.drawable.ic_bishop
                        ChessRank.KING -> resId = R.drawable.ic_king
                        ChessRank.QUEEN -> resId = R.drawable.ic_queen
                        ChessRank.ROOK -> resId = R.drawable.ic_rook
                        ChessRank.KNIGHT -> resId = R.drawable.ic_knight
                        ChessRank.PAWN -> resId = R.drawable.ic_pawn
                    }
                } else if (possiblePiece.player == ChessPlayer.WHITE) {
                    when (possiblePiece.rank) {
                        ChessRank.NONE -> resId = 0
                        ChessRank.BISHOP -> resId = R.drawable.ic_bishop_white
                        ChessRank.KING -> resId = R.drawable.ic_king_white
                        ChessRank.QUEEN -> resId = R.drawable.ic_queen_white
                        ChessRank.ROOK -> resId = R.drawable.ic_rook_white
                        ChessRank.KNIGHT -> resId = R.drawable.ic_knight_white
                        ChessRank.PAWN -> resId = R.drawable.ic_pawn_white
                    }
                }

                piecesBox.add(
                    ChessPiece(
                        possiblePiece.col,
                        possiblePiece.row,
                        possiblePiece.player,
                        possiblePiece.square,
                        possiblePiece.rank,
                        resId
                    )
                )
            }
        }
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
                    R.drawable.ic_rook_white
                )
            )
            piecesBox.add(
                ChessPiece(
                    0 + i * 7,
                    7,
                    ChessPlayer.BLACK,
                    blackRookSquares!![i],
                    ChessRank.ROOK,
                    R.drawable.ic_rook
                )
            )

            piecesBox.add(
                ChessPiece(
                    1 + i * 5,
                    0,
                    ChessPlayer.WHITE,
                    whiteKnightSquares!![i],
                    ChessRank.KNIGHT,
                    R.drawable.ic_knight_white
                )
            )
            piecesBox.add(
                ChessPiece(
                    1 + i * 5,
                    7,
                    ChessPlayer.BLACK,
                    blackKnightSquares!![i],
                    ChessRank.KNIGHT,
                    R.drawable.ic_knight
                )
            )

            piecesBox.add(
                ChessPiece(
                    2 + i * 3,
                    0,
                    ChessPlayer.WHITE,
                    whiteBishopSquares!![i],
                    ChessRank.BISHOP,
                    R.drawable.ic_bishop_white
                )
            )
            piecesBox.add(
                ChessPiece(
                    2 + i * 3,
                    7,
                    ChessPlayer.BLACK,
                    blackBishopSquares!![i],
                    ChessRank.BISHOP,
                    R.drawable.ic_bishop
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
                    R.drawable.ic_pawn_white
                )
            )
            piecesBox.add(
                ChessPiece(
                    i,
                    6,
                    ChessPlayer.BLACK,
                    blackPawnSquares!![i],
                    ChessRank.PAWN,
                    R.drawable.ic_pawn
                )
            )
        }
        for (row in 2..5) {
            for (col in 0..7) {
                piecesBox.add(
                    ChessPiece(
                        col,
                        row,
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
                R.drawable.ic_queen_white
            )
        )
        piecesBox.add(
            ChessPiece(
                3,
                7,
                ChessPlayer.BLACK,
                blackQueenSquares!![0],
                ChessRank.QUEEN,
                R.drawable.ic_queen
            )
        )

        piecesBox.add(
            ChessPiece(
                4,
                0,
                ChessPlayer.WHITE,
                whiteKingSquares!![0],
                ChessRank.KING,
                R.drawable.ic_king_white
            )
        )
        piecesBox.add(
            ChessPiece(
                4,
                7,
                ChessPlayer.BLACK,
                blackKingSquares!![0],
                ChessRank.KING,
                R.drawable.ic_king
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

    fun setListener(chessPieceListener: ChessPieceListener) {
        this.chessPieceListener = chessPieceListener
    }
}