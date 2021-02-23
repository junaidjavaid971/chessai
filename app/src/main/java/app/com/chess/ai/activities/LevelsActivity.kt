package app.com.chess.ai.activities

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import app.com.chess.ai.R
import app.com.chess.ai.adapters.LevelsAdapter
import app.com.chess.ai.databinding.ActivityLevelsBinding
import app.com.chess.ai.models.global.Level

class LevelsActivity : BaseActivity<ActivityLevelsBinding>() {
    var arrayList: ArrayList<Level> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView(R.layout.activity_levels)
        supportActionBar?.hide()
        prepareLevels()
        initLevelsAdapter()
    }

    private fun initLevelsAdapter() {
        binding?.rvLevels?.layoutManager =
            GridLayoutManager(this, 4)
        val adapter = LevelsAdapter(arrayList, this)
        binding?.rvLevels?.adapter = adapter
    }

    private fun prepareLevels() {
        var isLocked = false
        for (i in 1..16) {
            val level = Level(i.toString(), 0, isLocked)
            arrayList.add(level)
            isLocked = true
        }
    }
}