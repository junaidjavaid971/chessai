package app.com.chess.ai.activities

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import app.com.chess.ai.R
import app.com.chess.ai.databinding.ActivityOptionsBinding
import app.com.chess.ai.viewmodels.MainViewmodel

class OptionsActivity : BaseActivity<ActivityOptionsBinding>() {
    lateinit var viewModel: MainViewmodel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView(R.layout.activity_options)
        viewModel = ViewModelProvider(this).get(MainViewmodel::class.java)
        binding?.optionsActivity = viewModel
        supportActionBar?.hide()
        setObservers(viewModel, this)
        setObservers()
    }

    private fun setObservers() {
        viewModel.viewClickedLiveData.observe(this, {
            when (it) {
                R.id.layout_training -> {
                    addActivity(ChessboardActivity::class.java)
                }
                R.id.layout_levels -> {
                    showToast("Levels")
                }
            }
        })
    }
}