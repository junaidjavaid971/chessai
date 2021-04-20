package app.com.chess.ai.views.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import app.com.chess.ai.R
import app.com.chess.ai._AppController
import app.com.chess.ai.adapters.DisplayerAdapter
import app.com.chess.ai.adapters.TrainingPiecesAdapter
import app.com.chess.ai.databinding.FragmentTrainingChessboardBinding
import app.com.chess.ai.databinding.ProgressDialogRowBinding
import app.com.chess.ai.enums.ChessPieceEnum
import app.com.chess.ai.interfaces.ChessBoardListener
import app.com.chess.ai.models.global.ChessPiece
import app.com.chess.ai.models.global.Displayer
import app.com.chess.ai.utils.ChessMovements
import app.com.chess.ai.utils.KnightSteps
import app.com.chess.ai.utils.SharePrefData
import app.com.chess.ai.views.activities.BaseActivity
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class TrainingPiecesFragment : Fragment(), ChessBoardListener {

    //Global Variables
    var targetPosition: Int = 0
    var hits = ""
    var missed = ""
    var score = 0
    var isClickable = false
    var clickedTime: Long = 0
    var longestDuration: Long = 0
    var currentPosition = 100
    var noOfStepsRequired = 0
    var noOfStepsTaken = 0

    //Arrays
    var chessboardSquares = HashMap<Int, String>()
    var arrayList: ArrayList<Displayer> = ArrayList()
    var chessBoardArrayList: ArrayList<ChessPiece> = ArrayList()

    //Objects
    lateinit var binding: FragmentTrainingChessboardBinding
    var baseActivity: BaseActivity<FragmentTrainingChessboardBinding>? = null
    lateinit var TrainingPiecesAdapter: TrainingPiecesAdapter
    lateinit var countDownTimer: CountDownTimer


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_training_chessboard, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        baseActivity = activity as BaseActivity<FragmentTrainingChessboardBinding>?
        clickedTime = System.currentTimeMillis()
        currentPosition = Random().nextInt(63)
        initChessboardSquares()
        initDisplayerAdapter()
        updateCurrentSquare()
        bindBoardRecyclerView()

        binding.btnStart.setOnClickListener {
            startTraining()
        }
        binding.ivRestart.setOnClickListener {
            restartGame()
        }
    }

    private fun startTraining() {
        binding.btnStart.isEnabled = false
        binding.btnStart.isClickable = false
        object : CountDownTimer(4000, 1000) {
            @SuppressLint("SetTextI18n")

            override fun onTick(millisUntilFinished: Long) {
                binding.tvStartTimer.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                isClickable = true
                binding.flStart.visibility = View.GONE
                startTimer()
            }
        }.start()
    }

    override fun onChessSquareSelected(position: Int) {

    }

    override fun onChessSquareSelectedFirstTime(chessPiece: ChessPiece, position: Int) {
        val chessPiece = chessBoardArrayList[position];
        var movementList: ArrayList<Int> = ArrayList()
        val chessMovements = ChessMovements()
        if (chessPiece.piece == ChessPieceEnum.PAWN.chessPiece) {
            this.currentPosition = position
            if (chessPiece.isBlack) {
                movementList = chessMovements.getBlackPawnMovement(position)
            } else {
                movementList = chessMovements.getPawnMovement(position)
            }
        } else if (chessPiece.piece == ChessPieceEnum.ROOK.chessPiece) {
            this.currentPosition = position
            movementList = chessMovements.getRook(position, chessBoardArrayList)
        } else if (chessPiece.piece == ChessPieceEnum.BISHOP.chessPiece) {
            this.currentPosition = position
            movementList = chessMovements.getBishopMovement(position, chessBoardArrayList)
        } else if (chessPiece.piece == ChessPieceEnum.QUEEN.chessPiece) {
            this.currentPosition = position
            movementList = chessMovements.getQueenMovement(position, chessBoardArrayList)
        } else if (chessPiece.piece == ChessPieceEnum.KING.chessPiece) {
            this.currentPosition = position
            movementList = chessMovements.getKingMovement(position, chessBoardArrayList)
        } else if (chessPiece.piece == ChessPieceEnum.KNIGHT.chessPiece) {
            this.currentPosition = position
            movementList = chessMovements.getKnightMovement(position, chessBoardArrayList)
        }

        binding.rvChessboard.layoutManager = GridLayoutManager(requireActivity(), 8)
        TrainingPiecesAdapter =
            TrainingPiecesAdapter(
                requireActivity(),
                this,
                isClickable,
                chessBoardArrayList,
                targetPosition,
                movementList
            )
        binding.rvChessboard.adapter = TrainingPiecesAdapter
    }

    override fun onChessSquareMoved(chessPiece: ChessPiece, position: Int) {
        Collections.swap(chessBoardArrayList, position, currentPosition)
        if (position == targetPosition) {
            /*chessBoardArrayList[currentPosition].position = currentPosition
            chessBoardArrayList[position].position = currentPosition*/
            noOfStepsTaken++
            if (noOfStepsTaken <= noOfStepsRequired) {
                arrayList.add(Displayer("", true))
                binding.tvMessage.text = "Good Job!"
            } else {
                arrayList.add(Displayer("", false))
                binding.tvMessage.text = "You could have reach there in $noOfStepsRequired steps."
            }
            noOfStepsTaken = 0
            initDisplayerAdapter()
            updateCurrentSquare()
        } else {
            noOfStepsTaken++
        }
        TrainingPiecesAdapter =
            TrainingPiecesAdapter(
                requireActivity(),
                this,
                isClickable,
                chessBoardArrayList,
                targetPosition,
                ArrayList()
            )
        binding.rvChessboard.adapter = TrainingPiecesAdapter
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
        countDownTimer = object : CountDownTimer(_AppController.timer, 1000) {
            @SuppressLint("SetTextI18n")

            override fun onTick(millisUntilFinished: Long) {
                binding.tvTimer.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                binding.tvTimer.text = "00:00"
                isClickable = false
                showDialog()
            }
        }.start()
    }

    private fun initDisplayerAdapter() {
        binding.rvDisplayer.layoutManager =
            GridLayoutManager(requireActivity(), 6)
        val adapter = DisplayerAdapter(arrayList)
        binding.rvDisplayer.adapter = adapter
    }

    private fun bindBoardRecyclerView() {
        getChessArray()
        targetPosition = Random().nextInt(chessBoardArrayList.size)
        while (targetPosition == currentPosition) {
            targetPosition = Random().nextInt(chessBoardArrayList.size)
        }
        val pieceCoordinates = findCoordinates(currentPosition)
        val targetCoordinates = findCoordinates(targetPosition)
        val knightSteps = KnightSteps()
        noOfStepsRequired = knightSteps.getNumberOfSteps(
            pieceCoordinates[0],
            pieceCoordinates[1],
            targetCoordinates[0],
            targetCoordinates[1]
        )
        Log.d("StepsReq", noOfStepsRequired.toString())
        binding.rvChessboard.layoutManager = GridLayoutManager(requireActivity(), 8)
        TrainingPiecesAdapter =
            TrainingPiecesAdapter(
                requireActivity(),
                this,
                isClickable,
                chessBoardArrayList,
                targetPosition,
                ArrayList()
            )
        binding.rvChessboard.adapter = TrainingPiecesAdapter
    }

    private fun updateCurrentSquare() {
        targetPosition = Random().nextInt(63 - 0 + 1) + 0
        binding.tvCurrentSquare.text = chessboardSquares[targetPosition]
        binding.tvMessage.text =
            "Find all the possible ways to reach " + chessboardSquares[targetPosition] + ". Use the shortest path"
    }

    private fun getChessArray() {
        chessBoardArrayList.clear()
        for (i in 0 until 64) {
            val chessPiece = ChessPiece(100, false, ChessPieceEnum.EMPTY.chessPiece)
            if (i == currentPosition) {
                chessPiece.position = i
                chessPiece.piece = ChessPieceEnum.KNIGHT.chessPiece
            }
            chessBoardArrayList.add(chessPiece)
        }
    }

    fun showDialog() {
        val dialogBinding: ProgressDialogRowBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireActivity()),
            R.layout.progress_dialog_row,
            null,
            false
        )
        val dialog = Dialog(requireActivity(), R.style.RoundedDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(dialogBinding.root)

        dialogBinding.tvHit.text = hits
        dialogBinding.tvMissed.text = missed
        dialogBinding.tvScore.text = score.toString()
        dialogBinding.tvTimeperhit.text = (longestDuration / 10000).toFloat().toString() + "s"
        var bestScore = SharePrefData.instance.getPrefInt(requireActivity(), "bestScore")
        if (bestScore < score) {
            SharePrefData.instance.setPrefInt(requireActivity(), "bestScore", score)
            bestScore = score
        }
        dialogBinding.tvBestscore.text = bestScore.toString()

        dialogBinding.ivClose.setOnClickListener {
            resetBoard()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun resetBoard() {
        arrayList = ArrayList()
        hits = ""
        missed = ""
        score = 0
        isClickable = false
        longestDuration = 0
        targetPosition = 0
        binding.ivRestart.isClickable = true
        binding.tvCurrentSquare.text = "--"
        if (countDownTimer != null) {
            countDownTimer.cancel()
            binding.tvTimer.text = "--"
        }
        clickedTime = System.currentTimeMillis()
        initChessboardSquares()
        updateCurrentSquare()
        bindBoardRecyclerView()
    }

    private fun restartGame() {
        binding.ivRestart.isClickable = false
        binding.flStart.visibility = View.VISIBLE
        binding.btnStart.visibility = View.GONE
        binding.tvCurrentSquare.text = "--"
        clickedTime = System.currentTimeMillis()
        initChessboardSquares()
        initDisplayerAdapter()
        updateCurrentSquare()
        bindBoardRecyclerView()
        startTraining()
    }

    private fun findCoordinates(position: Int): Array<Int> {
        var xFound = false
        var yFound = false
        var x = 0
        var y = 0
        var initXPos = position
        var initYPos = 0
        while (!xFound) {
            if ((initXPos % 8) == 0) {
                x++
                xFound = true
                initYPos = initXPos
            } else {
                x++
                initXPos--
            }
        }
        if (initYPos == 56) {
            y++
            yFound = true
        }
        while (!yFound) {
            initYPos += 8
            if (initYPos == 56) {
                y++
                yFound = true
            }
            y++
        }
        return arrayOf(x, y);
    }

}