package app.com.chess.ai.views.activities

import android.os.Bundle
import app.com.chess.ai.R
import app.com.chess.ai.activities.FragmentTrainingChessboardSquares
import app.com.chess.ai.databinding.ActivityChessboardBinding
import app.com.chess.ai.enums.Flows
import app.com.chess.ai.views.fragments.FragmentLevelChessboard
import app.com.chess.ai.views.fragments.PiecesFragment
import app.com.chess.ai.views.fragments.TrainingPiecesFragment


class ChessboardActivity : BaseActivity<ActivityChessboardBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView(R.layout.activity_chessboard)
        supportActionBar?.hide()

        val currentFlow = intent.getIntExtra("flow", 0)
        if (currentFlow == Flows.FLOW_TRAINING_SQUARE.flowID) {
            replaceFragment(FragmentTrainingChessboardSquares())
        } else if (currentFlow == Flows.FLOW_TRAINING_PIECES.flowID) {
            replaceFragment(TrainingPiecesFragment())
        } else {
            replaceFragment(FragmentLevelChessboard(intent.getIntExtra("level", 1)))
        }
    }
}