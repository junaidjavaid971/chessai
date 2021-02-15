package app.com.chess.ai.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.com.chess.ai.R
import app.com.chess.ai._AppController
import app.com.chess.ai.adapters.BoardComponentAdapter.BoardComponentViewHolder
import app.com.chess.ai.interfaces.ChessBoardListener

class BoardComponentAdapter(
    private val context: Context,
    val chessBoardListener: ChessBoardListener,
    val currentSquare: Int
) :
    RecyclerView.Adapter<BoardComponentViewHolder>() {
    private var isOrange = false
    private var count = 8
    private var alphabet = 65
    var isClickable = true
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardComponentViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.board_component, parent, false)
        return BoardComponentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BoardComponentViewHolder, position: Int) {
        holder.tvAlphabet.setOnClickListener {
            if (isClickable)
                chessBoardListener.onChessSquareSelected(position)
        }
        if (_AppController.showAlphabets) {
            if (position % 8 == 0) {
                isOrange = !isOrange
                holder.tvAlphabet.visibility = View.VISIBLE
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
                if (position == currentSquare) {
                    holder.tvAlphabet.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorGreen
                        )
                    )
                }
                return
            }
            if (position == 56) {
                isOrange = !isOrange
                holder.tvAlphabet.visibility = View.VISIBLE
                holder.tvAlphabet.text = count.toString() + "-" + alphabet.toChar().toString()
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
                if (position == currentSquare) {
                    holder.tvAlphabet.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorGreen
                        )
                    )
                }
                return
            }
            if (position > 56) {
                holder.tvAlphabet.visibility = View.VISIBLE
                holder.tvAlphabet.text = alphabet.toChar().toString()
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
                if (position == currentSquare) {
                    holder.tvAlphabet.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorGreen
                        )
                    )
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
        if (position == currentSquare) {
            holder.tvAlphabet.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorGreen
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return 64
    }

    class BoardComponentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvAlphabet: TextView = itemView.findViewById<View>(R.id.tv_alphabet) as TextView
    }
}