package app.com.chess.ai.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.com.chess.ai.R
import app.com.chess.ai._AppController
import app.com.chess.ai.adapters.BoardComponentAdapter
import app.com.chess.ai.adapters.DisplayerAdapter
import app.com.chess.ai.databinding.ActivityChessboardBinding
import app.com.chess.ai.databinding.ProgressDialogRowBinding
import app.com.chess.ai.interfaces.ChessBoardListener
import app.com.chess.ai.models.global.Displayer
import app.com.chess.ai.utils.SharePrefData
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ChessboardActivity : BaseActivity<ActivityChessboardBinding>(), ChessBoardListener {
    var chessboardSquares = HashMap<Int, String>()
    var previouslyClickedSquare = PreviouslyClickedSquare()
    var currentSquare: Int = 0
    lateinit var boardComponentAdapter: BoardComponentAdapter
    var arrayList: ArrayList<Displayer> = ArrayList()
    var hits = ""
    var missed = ""
    var score = 0
    var isClickable = false
    var clickedTime: Long = 0
    var longestDuration: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView(R.layout.activity_chessboard)
        supportActionBar?.hide()
        clickedTime = System.currentTimeMillis()
        initChessboardSquares()
        initDisplayerAdapter()
        updateCurrentSquare()

        binding?.btnStart?.setOnClickListener {
            startTraining()
        }
    }

    private fun startTraining() {
        binding?.btnStart?.isEnabled = false
        binding?.btnStart?.isClickable = false
        object : CountDownTimer(4000, 1000) {
            @SuppressLint("SetTextI18n")

            override fun onTick(millisUntilFinished: Long) {
                binding?.tvStartTimer?.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                isClickable = true
                bindBoardRecyclerView()
                binding?.flStart?.visibility = View.GONE
                startTimer()
            }
        }.start()
    }

    override fun onChessSquareSelected(position: Int) {
        val duration = System.currentTimeMillis() - clickedTime
        if (duration > longestDuration) {
            longestDuration = duration
        }
        val displayer = Displayer(chessboardSquares[position], true)
        if (position != currentSquare) {
            displayer.isCorrect = false
            missed += chessboardSquares[position] + " "
        } else {
            hits += chessboardSquares[position] + " "
            score++
        }
        previouslyClickedSquare = PreviouslyClickedSquare(position, position == currentSquare)
        arrayList.add(displayer)
        updateCurrentSquare()
        initDisplayerAdapter()
    }

    private fun initChessboardSquares() {
        var index = 0
        for (i in 8 downTo 1) {
            for (j in 65..72) {
                chessboardSquares[index] = j.toChar().toString() + i
                index++
            }
        }
    }

    private fun startTimer() {
        object : CountDownTimer(_AppController.timer, 1000) {
            @SuppressLint("SetTextI18n")

            override fun onTick(millisUntilFinished: Long) {
                binding?.tvTimer?.text = "" + String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(
                                    millisUntilFinished
                                )
                            )
                )
            }

            override fun onFinish() {
                binding?.tvTimer?.text = "00:00"
                isClickable = false
                showDialog()
            }
        }.start()


    }

    private fun initDisplayerAdapter() {
        binding?.rvDisplayer?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = DisplayerAdapter(arrayList)
        binding?.rvDisplayer?.adapter = adapter
    }

    private fun bindBoardRecyclerView() {
        binding?.rvChessboard?.layoutManager = GridLayoutManager(this, 8)
        boardComponentAdapter =
            BoardComponentAdapter(this, this, isClickable, previouslyClickedSquare)
        binding?.rvChessboard?.adapter = boardComponentAdapter
    }

    private fun updateCurrentSquare() {
        currentSquare = Random().nextInt(63 - 0 + 1) + 0
        binding?.tvCurrentSquare?.text = chessboardSquares[currentSquare]
        bindBoardRecyclerView()
    }

    fun showDialog() {
        val dialogBinding: ProgressDialogRowBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.progress_dialog_row,
            null,
            false
        )
        val dialog = Dialog(this, R.style.RoundedDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(dialogBinding.root)

        dialogBinding.tvHit.text = hits
        dialogBinding.tvMissed.text = missed
        dialogBinding.tvScore.text = score.toString()
        dialogBinding.tvTimeperhit.text = (longestDuration / 10000).toFloat().toString() + "s"
        var bestScore = SharePrefData.instance.getPrefInt(this, "bestScore")
        if (bestScore < score) {
            SharePrefData.instance.setPrefInt(this, "bestScore", score)
            bestScore = score
        }
        dialogBinding.tvBestscore.text = bestScore.toString()

        dialogBinding.ivClose.setOnClickListener {
            resetBoard()
            dialog.dismiss()
        }
        dialog.show()
    }

    inner class PreviouslyClickedSquare {
        var squarePosition = 100
        var isCorrect = false

        constructor() {}
        constructor(squarePosition: Int?, isCorrect: Boolean) {
            this.squarePosition = squarePosition!!
            this.isCorrect = isCorrect
        }
    }

    private fun resetBoard() {
        arrayList = ArrayList()
        hits = ""
        missed = ""
        score = 0
        isClickable = false
        longestDuration = 0
        previouslyClickedSquare = PreviouslyClickedSquare()
        currentSquare = 0

        binding?.flStart?.visibility = View.VISIBLE
        binding?.btnStart?.text = "Restart"
        binding?.btnStart?.isEnabled = true
        binding?.btnStart?.isClickable = true
        binding?.tvStartTimer?.text = ""

        clickedTime = System.currentTimeMillis()
        initChessboardSquares()
        initDisplayerAdapter()
        updateCurrentSquare()
    }
}