package app.com.chess.ai.activities

import android.os.Bundle
import app.com.chess.ai.R
import app.com.chess.ai.databinding.ActivityChessboardBinding
import app.com.chess.ai.enums.Flows


class ChessboardActivity : BaseActivity<ActivityChessboardBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView(R.layout.activity_chessboard)
        supportActionBar?.hide()

        val currentFlow = intent.getIntExtra("flow", 0)
        if (currentFlow == Flows.FLOW_TRAINING.flowID) {
            replaceFragment(FragmentTrainingChessboard())
        }else {
            replaceFragment(FragmentLevelChessboard())
        }
    }
}