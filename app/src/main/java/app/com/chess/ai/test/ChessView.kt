package app.com.chess.ai.test

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import app.com.chess.ai.R
import com.github.bhlangonijr.chesslib.Side
import kotlin.math.min

class ChessView(context: Context, attrs: AttributeSet?) : View(context, attrs), ChessPieceListener {
    private var originX = 20f
    private var originY = 200f
    private var cellSide = 130f
    private val TAG = "MainActivityTAG"
    private val MTAG = "MovementTAG"
    private var movingPiece: ChessPiece? = null
    private val scaleFactor = .9f
    private val paint = Paint()
    private var movingPieceBitmap: Bitmap? = null
    private final val lightColor = resources.getColor(R.color.colorWhite)
    private val darkColor = resources.getColor(R.color.colorOrange)

    private val bitmaps = mutableMapOf<Int, Bitmap>()
    private val imgResIDs = setOf(
        R.drawable.bishop_black,
        R.drawable.bishop_white,
        R.drawable.king_black,
        R.drawable.king_white,
        R.drawable.queen_black,
        R.drawable.queen_white,
        R.drawable.rook_black,
        R.drawable.rook_white,
        R.drawable.knight_black,
        R.drawable.knight_white,
        R.drawable.pawn_black,
        R.drawable.pawn_white
    )
    var canvas: Canvas? = null
    var chessDelegate: ChessDelegate? = null
    private var movingPieceX = -1f
    private var movingPieceY = -1f
    private var chessModel: ChessModel? = null

    init {
        loadBitmaps()
        chessModel = ChessModel(this)
    }

    override fun onDraw(canvas: Canvas) {
        canvas ?: return
        this.canvas = canvas
        val chessBoardSide = min(width, height) * scaleFactor
        cellSide = chessBoardSide / 8f
        originX = (width - chessBoardSide) / 2f
        originY = (height - chessBoardSide) / 2f

        drawChessBoard()
        drawPieces()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                /*movingPieceX = event.x
                movingPieceY = event.y*/
            }
            MotionEvent.ACTION_UP -> {
                val col = ((event.x - originX) / cellSide).toInt()
                val row = 7 - ((event.y - originY) / cellSide).toInt()
                chessModel?.makeMove(col, row)
            }
        }
        invalidate()
        return true
    }

    private fun drawPieces() {
        for (row in 0..7) {
            for (col in 0..7) {
                if (row != -1 || col != -1) {
                    chessDelegate?.pieceAt(col, row)
                        ?.let {
                            if (it.resId != 0)
                                drawPieceAt(col, row, it.resId)
                        }
                }

                chessDelegate?.pieceAt(col, row)
                    ?.let {
                        if (it != movingPiece && it.resId != 0) {
                            drawPieceAt(col, row, it.resId)
                        }
                    }
            }
        }

        movingPieceBitmap?.let {
            canvas?.drawBitmap(
                it,
                null,
                RectF(
                    movingPieceX - cellSide / 2,
                    movingPieceY - cellSide / 2,
                    movingPieceX + cellSide / 2,
                    movingPieceY + cellSide / 2
                ),
                paint
            )
        }
    }

    private fun drawPieceAt(col: Int, row: Int, resId: Int) {
        val bitmap = bitmaps[resId]!!
        canvas?.drawBitmap(
            bitmap,
            null,
            RectF(
                originX + col * cellSide,
                originY + (7 - row) * cellSide,
                originX + (col + 1) * cellSide,
                originY + ((7 - row) + 1) * cellSide
            ),
            paint
        )
    }

    private fun loadBitmaps() {
        imgResIDs.forEach {
            bitmaps[it] = BitmapFactory.decodeResource(resources, it)
        }
    }

    private fun drawChessBoard() {
        for (row in 0..7) {
            for (col in 0..7) {
                drawSquareAt(row, col, (row + col) % 2 == 1)
            }
        }
    }

    private fun drawSquareAt(col: Int, row: Int, isDark: Boolean) {
        paint.color = if (isDark) darkColor else lightColor
        canvas?.drawRect(
            originX + col * cellSide,
            originY + row * cellSide,
            originX + (col + 1) * cellSide,
            originY + (row + 1) * cellSide,
            paint
        )
    }

    override fun chessPieceClicked(fromCol: Int, fromRow: Int, col: Int, row: Int) {
        chessDelegate?.movePiece(fromCol, fromRow, col, row)
        movingPiece = null
        movingPieceBitmap = null
    }

    override fun showToast(message: String) {
        chessDelegate?.showToast(message)
    }
}