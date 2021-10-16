package app.com.chess.ai.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import app.com.chess.ai.R
import app.com.chess.ai.models.global.training.Displayer

class ResultsDisplayAdapter(
    private val displayList: ArrayList<Displayer>
) :
    RecyclerView.Adapter<ResultsDisplayAdapter.DisplayViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisplayViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.displayer_row, parent, false)
        return DisplayViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DisplayViewHolder, position: Int) {
        val display = displayList[position]
        when (display.isCorrect) {
            true -> {
                holder.llCorrect.visibility = View.VISIBLE
                holder.llWrong.visibility = View.GONE
            }
            false -> {
                holder.llCorrect.visibility = View.GONE
                holder.llWrong.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int {
        return displayList.size
    }

    class DisplayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var llCorrect: LinearLayout = itemView.findViewById<View>(R.id.ll_correct) as LinearLayout
        var llWrong: LinearLayout = itemView.findViewById<View>(R.id.ll_wrong) as LinearLayout
    }
}