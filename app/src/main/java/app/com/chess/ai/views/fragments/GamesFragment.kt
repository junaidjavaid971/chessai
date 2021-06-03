package app.com.chess.ai.views.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.com.chess.ai.R
import app.com.chess.ai.adapters.GamesAdapter
import app.com.chess.ai.databinding.FragmentEndgameBinding
import app.com.chess.ai.databinding.FragmentGamesBinding
import app.com.chess.ai.models.dbModels.Games
import app.com.chess.ai.views.activities.BaseActivity

class GamesFragment(
    val gamesList: List<Games>,
    val gameSelectedListener: GameSelectedListener
) : Fragment(), GamesAdapter.GameItemSelected {
    lateinit var binding: FragmentGamesBinding
    var baseActivity: BaseActivity<FragmentGamesBinding>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_games, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        baseActivity = activity as BaseActivity<FragmentGamesBinding>?

        binding.rvGames.setHasFixedSize(true)
        binding.rvGames.layoutManager = LinearLayoutManager(context)
        val dividerItemDecoration = DividerItemDecoration(
            binding.rvGames.context,
            (binding.rvGames.layoutManager as LinearLayoutManager).getOrientation()
        )
        binding.rvGames.addItemDecoration(dividerItemDecoration)

        val rvAdapter = GamesAdapter(gamesList as ArrayList<Games>, context!!, this)
        binding.rvGames.adapter = rvAdapter
    }

    public interface GameSelectedListener {
        fun onGameSelected(game: Games)
    }

    override fun onGameSelected(game: Games) {
        gameSelectedListener.onGameSelected(game)
        activity?.onBackPressed()
    }
}