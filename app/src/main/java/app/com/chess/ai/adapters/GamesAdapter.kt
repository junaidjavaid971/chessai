package app.com.chess.ai.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.com.chess.ai.R
import app.com.chess.ai.models.dbModels.Games
import app.com.chess.ai.models.global.Displayer
import com.github.bhlangonijr.chesslib.game.Game

class GamesAdapter(
    val gamesList: ArrayList<Games>,
    val context: Context,
    val gameItemSelected: GameItemSelected
) :
    RecyclerView.Adapter<GamesAdapter.DisplayerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisplayerViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.games_dialog_row, parent, false)
        return DisplayerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DisplayerViewHolder, position: Int) {
        val game = gamesList[position]
        holder.tvTitle.text = game.tournamentName
        holder.tvDate.text = game.eventDate

        holder.itemView.setOnClickListener {
            gameItemSelected.onGameSelected(game)
        }
    }

    override fun getItemCount(): Int {
        return gamesList.size
    }

    public interface GameItemSelected {
        fun onGameSelected(game: Games)
    }

    class DisplayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById<View>(R.id.tv_title) as TextView
        var tvDate: TextView = itemView.findViewById<View>(R.id.tv_date) as TextView
    }
}