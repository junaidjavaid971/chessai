package app.com.chess.ai.views.activities

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import app.com.chess.ai.R
import app.com.chess.ai.databinding.ActivityMainBinding
import app.com.chess.ai.viewmodels.MainViewmodel
import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.Square


class MainActivity : BaseActivity<ActivityMainBinding>() {
    lateinit var viewModel: MainViewmodel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewmodel::class.java)
        binding?.mainActivity = viewModel
        supportActionBar?.hide()
        setObservers(viewModel, this)
        setObservers()
    }

    private fun setObservers() {
        viewModel.viewClickedLiveData.observe(this, {
            when (it) {
                R.id.layout_visualization -> {
                    addActivity(OptionsActivity::class.java, 1)
                }
                R.id.layout_endGames -> {
                    addActivity(OptionsActivity::class.java, 2)
                }
                R.id.layout_openings -> {
                    showToast("Openings")
                }
                R.id.layout_tactics -> {
                    showToast("Tactics")
                }
            }
        })
    }
}