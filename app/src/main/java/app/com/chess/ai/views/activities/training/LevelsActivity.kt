package app.com.chess.ai.views.activities.training

import android.os.Bundle
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import app.com.chess.ai.R
import app.com.chess.ai._AppController
import app.com.chess.ai.databinding.ActivityLevelsBinding
import app.com.chess.ai.models.dbModels.ChessSquare
import app.com.chess.ai.models.global.training.Level
import app.com.chess.ai.views.activities.BaseActivity
import app.com.chess.ai.views.fragments.LevelsFragment
import com.google.gson.Gson
import kotlin.math.ceil


class LevelsActivity : BaseActivity<ActivityLevelsBinding>() {
    var arrayList: ArrayList<Level> = ArrayList()
    lateinit var chessSquare: ChessSquare
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView(R.layout.activity_levels)
        supportActionBar?.hide()
        val levelsJson = decodeJson(
            resources.openRawResource(R.raw.chess_squares)
        )
        chessSquare = Gson().fromJson(levelsJson, ChessSquare::class.java)
        prepareLevels()
        init()
    }

    private fun init() {
        supportActionBar!!.elevation = 0f
        binding!!.viewPager.adapter = ViewPagerFragmentAdapter(this)
        binding?.pagerDots?.setViewPager2(binding!!.viewPager)
    }

    private fun prepareLevels() {
        for (i in 0 until chessSquare.list?.size!!) {
            val level = Level(i.toString(), 0, chessSquare.list?.get(i)?.isUnlocked!!)
            arrayList.add(level)
        }
    }

    inner class ViewPagerFragmentAdapter(@NonNull fragmentActivity: FragmentActivity?) :
        FragmentStateAdapter(fragmentActivity!!) {
        @NonNull
        override fun createFragment(position: Int): Fragment {
            val count = ceil(arrayList.size.toFloat() / _AppController.levelsPerPage).toInt()
            for (i in 0..count) {
                if (i == position) {
                    val startingIndex = i * _AppController.levelsPerPage
                    if (arrayList.size - startingIndex >= _AppController.levelsPerPage) {

                        val levelsArrayList = arrayList.subList(
                            startingIndex,
                            startingIndex + _AppController.levelsPerPage
                        )

                        return LevelsFragment(levelsArrayList)
                    } else {
                        val levelsArrayList =
                            arrayList.subList(startingIndex, arrayList.size)
                        return LevelsFragment(levelsArrayList)
                    }
                }
            }
            return Fragment()
        }

        override fun getItemCount(): Int {
            return ceil(arrayList.size.toFloat() / _AppController.levelsPerPage).toInt()
        }
    }
}