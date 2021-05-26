package app.com.chess.ai.adapters

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.com.chess.ai.R
import app.com.chess.ai._AppController
import app.com.chess.ai.adapters.PiecesAdapter.TrainingChessboardViewHolder
import app.com.chess.ai.enums.ChessPieceEnum
import app.com.chess.ai.interfaces.ChessBoardListener
import app.com.chess.ai.models.global.ChessPiece
import app.com.chess.ai.models.global.PreviouslyClickedSquare
import app.com.chess.ai.utils.KnightSteps

class PiecesAdapter(
    private val context: Context,
    val chessBoardListener: ChessBoardListener,
    val isClickable: Boolean,
    val previouslyClickedSquare: PreviouslyClickedSquare,
    val chessArray: ArrayList<ChessPiece>,
    val movementArray: ArrayList<Int>
) :
    RecyclerView.Adapter<TrainingChessboardViewHolder>() {
    private var isOrange = false
    private var count = 8
    private var alphabet = 65
    var arrayList: ArrayList<Int> = IntRange(0, 63).step(1).toList() as ArrayList<Int>
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrainingChessboardViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.board_component, parent, false)
        return TrainingChessboardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrainingChessboardViewHolder, position: Int) {
        bindSquares(holder, position)
        drawPieces(holder, position)
        /*if (position == 57)
            drawOnePiece(holder, position)*/
    }

    private fun bindSquares(holder: TrainingChessboardViewHolder, position: Int) {
        holder.item.setOnClickListener {
            if (isClickable) {
                if (movementArray.contains(position)) {
                    chessBoardListener.onChessSquareMoved(chessArray[position], position)
                } else {
                    chessBoardListener.onChessSquareSelectedFirstTime(
                        chessArray[position],
                        position
                    )
                }
            }
        }
        if (_AppController.showAlphabets) {
            if (position == 56) {
                isOrange = !isOrange
                holder.tvAlphabet.visibility = View.VISIBLE
                holder.tvAlphabet.text = count.toString()
                holder.tvAletter.text = "A"
                //alphabet.toChar().toString()
                alphabet++
                if (isOrange) {
                    isOrange = !isOrange
                    holder.tvAlphabet.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorOrange
                        )
                    )
                } else {
                    isOrange = !isOrange
                    holder.tvAlphabet.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorWhite
                        )
                    )
                    holder.tvAlphabet.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorOrange
                        )
                    )
                }
                if (position == previouslyClickedSquare.squarePosition) {
                    if (previouslyClickedSquare.isCorrect) {
                        holder.tvAlphabet.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.colorGreen
                            )
                        )
                    } else {
                        holder.tvAlphabet.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.colorRed
                            )
                        )
                    }
                }
                return
            }
            if (position % 8 == 0) {
                isOrange = !isOrange
                holder.tvAlphabet.visibility = View.VISIBLE
                holder.tvAlphabet.gravity = Gravity.LEFT or Gravity.TOP
                if (count != 0) {
                    if (isOrange) {
                        isOrange = !isOrange
                        holder.tvAlphabet.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.colorOrange
                            )
                        )
                    } else {
                        isOrange = !isOrange
                        holder.tvAlphabet.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.colorWhite
                            )
                        )
                        holder.tvAlphabet.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.colorOrange
                            )
                        )
                    }
                    holder.tvAlphabet.text = count.toString()
                }
                count--
                if (position == previouslyClickedSquare.squarePosition) {
                    if (previouslyClickedSquare.isCorrect) {
                        holder.tvAlphabet.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.colorGreen
                            )
                        )
                    } else {
                        holder.tvAlphabet.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.colorRed
                            )
                        )
                    }
                }
                return
            }
            if (position > 56) {
                holder.tvAlphabet.visibility = View.VISIBLE
                holder.tvAlphabet.text = alphabet.toChar().toString()
                holder.tvAlphabet.gravity = Gravity.BOTTOM or Gravity.RIGHT
                alphabet++
                if (isOrange) {
                    isOrange = !isOrange
                    holder.tvAlphabet.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorOrange
                        )
                    )
                } else {
                    isOrange = !isOrange
                    holder.tvAlphabet.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorWhite
                        )
                    )
                    holder.tvAlphabet.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorOrange
                        )
                    )
                }
                if (position == previouslyClickedSquare.squarePosition) {
                    if (previouslyClickedSquare.isCorrect) {
                        holder.tvAlphabet.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.colorGreen
                            )
                        )
                    } else {
                        holder.tvAlphabet.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.colorRed
                            )
                        )
                    }
                }
                return
            }
        } else {
            if (position % 8 == 0) {
                isOrange = !isOrange
            }
        }
        if (isOrange) {
            isOrange = !isOrange
            holder.tvAlphabet.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorOrange
                )
            )
        } else {
            isOrange = !isOrange
            holder.tvAlphabet.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorWhite
                )
            )
        }
        if (position == previouslyClickedSquare.squarePosition) {
            if (previouslyClickedSquare.isCorrect) {
                holder.tvAlphabet.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorGreen
                    )
                )
            } else {
                holder.tvAlphabet.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorRed
                    )
                )
            }
        }
    }

    private fun drawOnePiece(holder: TrainingChessboardViewHolder, position: Int) {
        holder.item.setOnClickListener {
        }
        holder.ivChessPiece.visibility = View.VISIBLE
        holder.ivChessPiece.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.ic_knight_white
            )
        )
    }

    private fun drawPieces(holder: TrainingChessboardViewHolder, position: Int) {
        if (movementArray.contains(position)) {
            if (chessArray[position].piece == ChessPieceEnum.EMPTY.chessPiece) {
                holder.ivDot.visibility = View.VISIBLE
            }
        }

        val chessPiece = chessArray[position]
        if (chessPiece.isBlack) {
            when (chessPiece.piece) {
                ChessPieceEnum.PAWN.chessPiece -> {
                    holder.ivChessPiece.visibility = View.VISIBLE
                    holder.ivChessPiece.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_pawn
                        )
                    )
                }
                ChessPieceEnum.ROOK.chessPiece -> {
                    holder.ivChessPiece.visibility = View.VISIBLE
                    holder.ivChessPiece.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_rook
                        )
                    )
                }
                ChessPieceEnum.BISHOP.chessPiece -> {
                    holder.ivChessPiece.visibility = View.VISIBLE
                    holder.ivChessPiece.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_bishop
                        )
                    )
                }
                ChessPieceEnum.KNIGHT.chessPiece -> {
                    holder.ivChessPiece.visibility = View.VISIBLE
                    holder.ivChessPiece.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_knight
                        )
                    )
                }
                ChessPieceEnum.QUEEN.chessPiece -> {
                    holder.ivChessPiece.visibility = View.VISIBLE
                    holder.ivChessPiece.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_queen
                        )
                    )
                }
                ChessPieceEnum.KING.chessPiece -> {
                    holder.ivChessPiece.visibility = View.VISIBLE
                    holder.ivChessPiece.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_king
                        )
                    )
                }
            }
        } else {
            when (chessPiece.piece) {
                ChessPieceEnum.PAWN.chessPiece -> {
                    holder.ivChessPiece.visibility = View.VISIBLE
                    holder.ivChessPiece.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_pawn_white
                        )
                    )
                }
                ChessPieceEnum.ROOK.chessPiece -> {
                    holder.ivChessPiece.visibility = View.VISIBLE
                    holder.ivChessPiece.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_rook_white
                        )
                    )
                }
                ChessPieceEnum.BISHOP.chessPiece -> {
                    holder.ivChessPiece.visibility = View.VISIBLE
                    holder.ivChessPiece.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_bishop_white
                        )
                    )
                }
                ChessPieceEnum.KNIGHT.chessPiece -> {
                    holder.ivChessPiece.visibility = View.VISIBLE
                    holder.ivChessPiece.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_knight_white
                        )
                    )
                }
                ChessPieceEnum.QUEEN.chessPiece -> {
                    holder.ivChessPiece.visibility = View.VISIBLE
                    holder.ivChessPiece.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_queen_white
                        )
                    )
                }
                ChessPieceEnum.KING.chessPiece -> {
                    holder.ivChessPiece.visibility = View.VISIBLE
                    holder.ivChessPiece.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_king_white
                        )
                    )
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return chessArray.size
    }

    class TrainingChessboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var item: ConstraintLayout =
            itemView.findViewById<View>(R.id.boardComponent) as ConstraintLayout
        var tvAlphabet: TextView = itemView.findViewById<View>(R.id.tv_alphabet) as TextView
        var tvAletter: TextView = itemView.findViewById<View>(R.id.tv_except_a_letter) as TextView
        var ivChessPiece: ImageView = itemView.findViewById<View>(R.id.fl_piece) as ImageView
        var ivDot: ImageView = itemView.findViewById<View>(R.id.fl_dot) as ImageView

    }
}