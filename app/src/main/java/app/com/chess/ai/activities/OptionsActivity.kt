package app.com.chess.ai.activities

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import app.com.chess.ai.R
import app.com.chess.ai.databinding.ActivityOptionsBinding
import app.com.chess.ai.enums.Flows
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
                    val intent = Intent(this, ChessboardActivity::class.java)
                    intent.putExtra("flow", Flows.FLOW_TRAINING.flowID)
                    startActivity(intent)
                }
                R.id.layout_levels -> {
                    addActivity(LevelsActivity::class.java)
                }
            }
        })
    }
}