package app.com.chess.ai.views.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import app.com.chess.ai.R
import app.com.chess.ai.databinding.FragmentTrainingOptionsBinding
import app.com.chess.ai.enums.Flows
import app.com.chess.ai.views.activities.BaseActivity
import app.com.chess.ai.views.activities.training.ChessboardActivity

class TrainingOptionsFragment : Fragment() {

    lateinit var binding: FragmentTrainingOptionsBinding
    var baseActivity: BaseActivity<FragmentTrainingOptionsBinding>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_training_options, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
    }

    private fun setObservers() {
        binding.layoutSquares.setOnClickListener {
            val intent = Intent(activity, ChessboardActivity::class.java)
            intent.putExtra("flow", Flows.FLOW_TRAINING_SQUARE.flowID)
            startActivity(intent)
        }
        binding.layoutPieces.setOnClickListener {
            val intent = Intent(activity, ChessboardActivity::class.java)
            intent.putExtra("flow", Flows.FLOW_TRAINING_PIECES.flowID)
            startActivity(intent)
        }
    }
}