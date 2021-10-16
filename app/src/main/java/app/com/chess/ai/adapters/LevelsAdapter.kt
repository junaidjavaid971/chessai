package app.com.chess.ai.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.com.chess.ai.R
import app.com.chess.ai.views.activities.training.ChessboardActivity
import app.com.chess.ai.enums.Flows
import app.com.chess.ai.models.global.training.Level

class LevelsAdapter(
    val arrayList: MutableList<Level>,
    val context: Context
) :
    RecyclerView.Adapter<LevelsAdapter.LevelsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.level_item_row, parent, false)
        return LevelsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LevelsViewHolder, position: Int) {
        val level = arrayList[position]
        if (!level.isUnlocked) {
            holder.flLocked.visibility = View.VISIBLE
        }
        holder.tvLevel.text = (level.level?.toInt()?.plus(1)).toString()
        holder.itemView.setOnClickListener {
            if (!level.isUnlocked)
                return@setOnClickListener
            val intent = Intent(context, ChessboardActivity::class.java)
            intent.putExtra("flow", Flows.FLOW_LEVEL.flowID)
            intent.putExtra("level", position + 1)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class LevelsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvLevel: TextView = itemView.findViewById<View>(R.id.tv_level) as TextView
        var rating: RatingBar = itemView.findViewById<View>(R.id.ratingBar) as RatingBar
        var flLocked: FrameLayout = itemView.findViewById<View>(R.id.fl_lockedLevel) as FrameLayout
    }
}