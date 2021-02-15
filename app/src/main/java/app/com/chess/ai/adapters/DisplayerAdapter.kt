package app.com.chess.ai.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.com.chess.ai.R
import app.com.chess.ai.models.global.Displayer

class DisplayerAdapter(
    val displayerList: ArrayList<Displayer>
) :
    RecyclerView.Adapter<DisplayerAdapter.DisplayerViewHolder>() {
    private val limit = 14
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisplayerViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.displayer_row, parent, false)
        return DisplayerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DisplayerViewHolder, position: Int) {
        val displayer = displayerList[position]
        when (displayer.isCorrect) {
            true -> {
                holder.llCorrect.visibility = View.VISIBLE
                holder.llWrong.visibility = View.GONE
                holder.tvCorrect.text = displayer.chessSquare
            }
            false -> {
                holder.llCorrect.visibility = View.GONE
                holder.llWrong.visibility = View.VISIBLE
                holder.tvWrong.text = displayer.chessSquare
            }
        }
    }

    override fun getItemCount(): Int {
        return if (displayerList.size > limit) {
            limit
        } else {
            displayerList.size
        }
    }

    class DisplayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvWrong: TextView = itemView.findViewById<View>(R.id.tv_wrong) as TextView
        var tvCorrect: TextView = itemView.findViewById<View>(R.id.tv_correct) as TextView
        var llCorrect: LinearLayout = itemView.findViewById<View>(R.id.ll_correct) as LinearLayout
        var llWrong: LinearLayout = itemView.findViewById<View>(R.id.ll_wrong) as LinearLayout
    }
}