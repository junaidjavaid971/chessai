package app.com.chess.ai.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import app.com.chess.ai.R
import app.com.chess.ai.interfaces.ChessDelegate
import app.com.chess.ai.models.global.endgame.ChessModel
import app.com.chess.ai.models.global.endgame.ChessPiece
import kotlin.math.min


class ChessView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    //Constants & Variables
    private var originX = 20f
    private var originY = 200f
    private var cellSide = 130f
    private val scaleFactor = .9f

    //Objects
    private val paint = Paint()
    private var movingPiece: ChessPiece? = null
    private var movingPieceBitmap: Bitmap? = null

    //Colors
    private val lightColor = resources.getColor(R.color.colorWhite)
    private val darkColor = resources.getColor(R.color.colorOrange)

    private val bitmaps = mutableMapOf<Int, Bitmap>()
    private val imgResIDs = setOf(
        R.drawable.ic_bishop,
        R.drawable.ic_bishop_white,
        R.drawable.ic_king,
        R.drawable.ic_king_white,
        R.drawable.ic_queen,
        R.drawable.ic_queen_white,
        R.drawable.ic_rook,
        R.drawable.ic_rook_white,
        R.drawable.ic_knight,
        R.drawable.ic_knight_white,
        R.drawable.ic_pawn,
        R.drawable.ic_pawn_white,
        R.drawable.ic_dot
    )
    var canvas: Canvas? = null
    var chessDelegate: ChessDelegate? = null
    private var movingPieceX = -1f
    private var movingPieceY = -1f
    private var chessModel: ChessModel? = null

    init {
        loadBitmaps()
        chessModel = ChessModel()
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
            MotionEvent.ACTION_UP -> {
                val col = ((event.x - originX) / cellSide).toInt()
                val row = 7 - ((event.y - originY) / cellSide).toInt()
                chessDelegate?.makeMove(col, row)
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

                chessDelegate?.possibleMovementAt(col, row)?.let {
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
            bitmaps[it] = getBitmapFromVectorDrawable(it)
        }
    }

    private fun drawChessBoard() {
        for (row in 0..7) {
            for (col in 0..7) {
                drawSquareAt(row, col, (row + col) % 2 == 1)
            }
        }
    }

    fun getBitmapFromVectorDrawable(drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context!!, drawableId)
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
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

}