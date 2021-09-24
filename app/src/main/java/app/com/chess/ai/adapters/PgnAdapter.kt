package app.com.chess.ai.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import app.com.chess.ai.R
import app.com.chess.ai.models.global.Displayer

class PgnAdapter(
    val pgnArrayList: ArrayList<String>,
    var currentMoveIndex: Int,
    var context: Context
) :
    RecyclerView.Adapter<PgnAdapter.PgnViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PgnViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.pgn_layout, parent, false)
        return PgnViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PgnViewHolder, position: Int) {
        val pgn = pgnArrayList[position]
        if (currentMoveIndex == position) {
            holder.tvPgn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOrange))
        } else {
            holder.tvPgn.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorPrimaryDark
                )
            )
        }
        if (isOdd(position)) {
            holder.tvPgn.text = "$position . $pgn "
        } else {
            holder.tvPgn.text = " $pgn "
        }
    }

    override fun getItemCount(): Int {
        return pgnArrayList.size
    }

    private fun isOdd(position: Int): Boolean {
        return (position % 2) == 0
    }

    class PgnViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvPgn: TextView = itemView.findViewById<View>(R.id.tvPgn) as TextView
    }
}