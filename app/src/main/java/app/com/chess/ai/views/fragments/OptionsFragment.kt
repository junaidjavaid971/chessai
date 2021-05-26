package app.com.chess.ai.views.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import app.com.chess.ai.R
import app.com.chess.ai.databinding.FragmentOptionsBinding
import app.com.chess.ai.databinding.FragmentTrainingChessboardBinding
import app.com.chess.ai.enums.Flows
import app.com.chess.ai.views.activities.BaseActivity
import app.com.chess.ai.views.activities.ChessboardActivity
import app.com.chess.ai.views.activities.LevelsActivity

class OptionsFragment : Fragment() {

    lateinit var binding: FragmentOptionsBinding
    var baseActivity: BaseActivity<FragmentOptionsBinding>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_options, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baseActivity = activity as BaseActivity<FragmentOptionsBinding>?
        setObservers()
    }

    private fun setObservers() {
        binding.layoutTraining.setOnClickListener {
            baseActivity?.replaceFragment(TrainingOptionsFragment())
        }
        binding.layoutLevels.setOnClickListener {
            baseActivity?.addActivity(LevelsActivity::class.java , 0)
        }
    }
}