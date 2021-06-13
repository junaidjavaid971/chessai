package app.com.chess.ai.adapters

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.com.chess.ai.R
import app.com.chess.ai._AppController
import app.com.chess.ai.interfaces.ChessBoardListener
import app.com.chess.ai.models.global.PreviouslyClickedSquare

class TrainingChessboardSquaresAdapter(
    private val context: Context,
    val chessBoardListener: ChessBoardListener,
    val isClickable: Boolean,
    val previouslyClickedSquare: PreviouslyClickedSquare
) :
    RecyclerView.Adapter<TrainingChessboardSquaresAdapter.TrainingChessboardViewHolder>() {
    private var isOrange = true
    private var count = 8
    private var alphabet = 65
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrainingChessboardViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.board_component, parent, false)
        return TrainingChessboardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrainingChessboardViewHolder, position: Int) {
        holder.tvAlphabet.setOnClickListener {
            if (isClickable)
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

    override fun getItemCount(): Int {
        return 64
    }

    class TrainingChessboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvAlphabet: TextView = itemView.findViewById<View>(R.id.tv_alphabet) as TextView
        var tvAletter: TextView = itemView.findViewById<View>(R.id.tv_except_a_letter) as TextView
    }
}