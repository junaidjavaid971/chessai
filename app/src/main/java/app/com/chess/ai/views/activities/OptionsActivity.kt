package app.com.chess.ai.views.activities

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import app.com.chess.ai.R
import app.com.chess.ai.databinding.ActivityOptionsBinding
import app.com.chess.ai.enums.Flows
import app.com.chess.ai.viewmodels.MainViewmodel
import app.com.chess.ai.views.fragments.EndgamePiecesFragment
import app.com.chess.ai.views.fragments.OptionsFragment

class OptionsActivity : BaseActivity<ActivityOptionsBinding>() {
    lateinit var viewModel: MainViewmodel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView(R.layout.activity_options)
        viewModel = ViewModelProvider(this).get(MainViewmodel::class.java)
        binding?.optionsActivity = viewModel
        supportActionBar?.hide()

        val id = intent.getIntExtra("ID" , 0)
        if(id == 1) {
            replaceFragment(OptionsFragment())
        }else {
            replaceFragment(EndgamePiecesFragment())
        }
    }
}