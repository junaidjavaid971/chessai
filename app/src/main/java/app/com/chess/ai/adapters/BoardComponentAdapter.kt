package app.com.chess.ai.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.com.chess.ai.R
import app.com.chess.ai.adapters.BoardComponentAdapter.BoardComponentViewHolder

class BoardComponentAdapter(private val context: Context) :
    RecyclerView.Adapter<BoardComponentViewHolder>() {
    private var isOrange = false
    private var count = 8
    private var alphabet = 65
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardComponentViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.board_component, parent, false)
        return BoardComponentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BoardComponentViewHolder, position: Int) {
        if (position % 9 == 0) {
            isOrange = !isOrange
            holder.tvAlphabet.visibility = View.VISIBLE
            if (count != 0) {
                holder.tvAlphabet.text = count.toString()
            } else {
                holder.tvAlphabet.text = ""
            }
            holder.view.visibility = View.GONE
            count--
            return
        }
        if (position > 72) {
            holder.tvAlphabet.visibility = View.VISIBLE
            holder.tvAlphabet.setText(alphabet.toChar().toString())
            holder.view.visibility = View.GONE
            alphabet++
            return
        }
        if (isOrange) {
            isOrange = !isOrange
            holder.view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOrange))
        } else {
            isOrange = !isOrange
            holder.view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))
        }
    }

    override fun getItemCount(): Int {
        return 81
    }

    class BoardComponentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var view: View
        var tvAlphabet: TextView

        init {
            view = itemView.findViewById(R.id.cell_view) as View
            tvAlphabet = itemView.findViewById<View>(R.id.tv_alphabet) as TextView
        }
    }
}