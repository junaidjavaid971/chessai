package app.com.chess.ai.adapters

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.com.chess.ai.R
import app.com.chess.ai._AppController
import app.com.chess.ai.activities.FragmentLevelChessboard
import app.com.chess.ai.interfaces.ChessBoardListener
import app.com.chess.ai.models.global.ChessSquare
import java.util.*

class LevelChessboardAdapter(
    private val context: Context,
    val chessBoardListener: ChessBoardListener,
    val isClickable: Boolean,
    val arrayList: ArrayList<ChessSquare.ChessObject>,
    val previouslyClickedSquare: FragmentLevelChessboard.PreviouslyClickedSquare
) :
    RecyclerView.Adapter<LevelChessboardAdapter.LevelChessboardViewHolder>() {
    private var isOrange = false
    private var count = 8
    private var alphabet = 65
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelChessboardViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.board_component, parent, false)
        return LevelChessboardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LevelChessboardViewHolder, position: Int) {
        holder.tvAlphabet.setOnClickListener {
            if (isClickable && arrayList[position].isActive)
                chessBoardListener.onChessSquareSelected(position)
        }
        if (_AppController.showAlphabets) {
            if (position == 56) {
                isOrange = !isOrange
                holder.tvAlphabet.visibility = View.VISIBLE
                holder.tvAlphabet.text = count.toString()
                holder.tvAletter.text = "A"
                //alphabet.toChar().toString()
                alphabet++
                drawChessSquare(holder, position)
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
                    drawChessSquare(holder, position)
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
                drawChessSquare(holder, position)
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
        drawChessSquare(holder, position)
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

    private fun drawChessSquare(holder: LevelChessboardViewHolder, position: Int) {
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
        if (!arrayList[position].isActive) {
            holder.frameLayout.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class LevelChessboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvAlphabet: TextView = itemView.findViewById<View>(R.id.tv_alphabet) as TextView
        var tvAletter: TextView = itemView.findViewById<View>(R.id.tv_except_a_letter) as TextView
        var frameLayout: FrameLayout =
            itemView.findViewById<View>(R.id.fl_disable) as FrameLayout
    }
}