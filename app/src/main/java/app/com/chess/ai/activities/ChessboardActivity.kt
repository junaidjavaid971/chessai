package app.com.chess.ai.activities

import android.R.attr.data
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import app.com.chess.ai.R
import app.com.chess.ai.adapters.BoardComponentAdapter
import app.com.chess.ai.databinding.ActivityChessboardBinding


class ChessboardActivity : BaseActivity<ActivityChessboardBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView(R.layout.activity_chessboard)
        supportActionBar?.hide()
        binding?.rvChessboard?.layoutManager = GridLayoutManager(this, 9)
        val adapter = BoardComponentAdapter(this)
        binding?.rvChessboard?.adapter = adapter
    }
}