package app.com.chess.ai.views.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import app.com.chess.ai.R
import app.com.chess.ai._AppController
import app.com.chess.ai.adapters.DisplayerAdapter
import app.com.chess.ai.adapters.PiecesAdapter
import app.com.chess.ai.databinding.FragmentTrainingChessboardBinding
import app.com.chess.ai.databinding.ProgressDialogRowBinding
import app.com.chess.ai.enums.ChessPieceEnum
import app.com.chess.ai.interfaces.ChessBoardListener
import app.com.chess.ai.models.global.ChessPiece
import app.com.chess.ai.models.global.Displayer
import app.com.chess.ai.models.global.PreviouslyClickedSquare
import app.com.chess.ai.utils.ChessMovements
import app.com.chess.ai.utils.SharePrefData
import app.com.chess.ai.views.activities.BaseActivity
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class PiecesFragment : Fragment(), ChessBoardListener {

    //Global Variables
    var currentSquare: Int = 0
    var hits = ""
    var missed = ""
    var score = 0
    var isClickable = false
    var clickedTime: Long = 0
    var longestDuration: Long = 0
    var currentPosition = 0;

    //Arrays
    var chessboardSquares = HashMap<Int, String>()
    var arrayList: ArrayList<Displayer> = ArrayList()
    var chessBoardArrayList: ArrayList<ChessPiece> = ArrayList()

    //Objects
    lateinit var binding: FragmentTrainingChessboardBinding
    var baseActivity: BaseActivity<FragmentTrainingChessboardBinding>? = null
    lateinit var piecesAdapter: PiecesAdapter
    var previouslyClickedSquare = PreviouslyClickedSquare()
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
        initChessboardSquares()
        initDisplayerAdapter()
        updateCurrentSquare()

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
                bindBoardRecyclerView()
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
        piecesAdapter =
            PiecesAdapter(
                requireActivity(),
                this,
                isClickable,
                previouslyClickedSquare,
                chessBoardArrayList,
                movementList
            )
        binding.rvChessboard.adapter = piecesAdapter
    }

    override fun onChessSquareMoved(chessPiece: ChessPiece, position: Int) {
        Collections.swap(chessBoardArrayList, position, currentPosition)

        piecesAdapter =
            PiecesAdapter(
                requireActivity(),
                this,
                isClickable,
                previouslyClickedSquare,
                chessBoardArrayList,
                ArrayList()
            )
        binding.rvChessboard.adapter = piecesAdapter
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
                binding.tvTimer.text = "" + String.format(
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
        binding.rvChessboard.layoutManager = GridLayoutManager(requireActivity(), 8)
        piecesAdapter =
            PiecesAdapter(
                requireActivity(),
                this,
                isClickable,
                previouslyClickedSquare,
                chessBoardArrayList,
                ArrayList()
            )
        binding.rvChessboard.adapter = piecesAdapter
    }

    private fun updateCurrentSquare() {
        currentSquare = Random().nextInt(63 - 0 + 1) + 0
        binding.tvCurrentSquare.text = chessboardSquares[currentSquare]
        bindBoardRecyclerView()
    }

    private fun getChessArray() {
        chessBoardArrayList.clear()
        for (i in 0 until 64) {
            when (i) {
                0, 7 -> {
                    val chessPiece = ChessPiece(i, true, ChessPieceEnum.ROOK.chessPiece)
                    chessBoardArrayList.add(chessPiece)
                }
                1, 6 -> {
                    val chessPiece = ChessPiece(i, true, ChessPieceEnum.KNIGHT.chessPiece)
                    chessBoardArrayList.add(chessPiece)
                }
                2, 5 -> {
                    val chessPiece = ChessPiece(i, true, ChessPieceEnum.BISHOP.chessPiece)
                    chessBoardArrayList.add(chessPiece)
                }
                3 -> {
                    val chessPiece = ChessPiece(i, true, ChessPieceEnum.QUEEN.chessPiece)
                    chessBoardArrayList.add(chessPiece)
                }
                4 -> {
                    val chessPiece = ChessPiece(i, true, ChessPieceEnum.KING.chessPiece)
                    chessBoardArrayList.add(chessPiece)
                }
                56, 63 -> {
                    val chessPiece = ChessPiece(i, false, ChessPieceEnum.ROOK.chessPiece)
                    chessBoardArrayList.add(chessPiece)
                }
                57, 62 -> {
                    val chessPiece = ChessPiece(i, false, ChessPieceEnum.KNIGHT.chessPiece)
                    chessBoardArrayList.add(chessPiece)
                }
                58, 61 -> {
                    val chessPiece = ChessPiece(i, false, ChessPieceEnum.BISHOP.chessPiece)
                    chessBoardArrayList.add(chessPiece)
                }
                59 -> {
                    val chessPiece = ChessPiece(i, false, ChessPieceEnum.QUEEN.chessPiece)
                    chessBoardArrayList.add(chessPiece)
                }
                60 -> {
                    val chessPiece = ChessPiece(i, false, ChessPieceEnum.KING.chessPiece)
                    chessBoardArrayList.add(chessPiece)
                }
                in 8..15 -> {
                    val chessPiece = ChessPiece(i, true, ChessPieceEnum.PAWN.chessPiece)
                    chessBoardArrayList.add(chessPiece)
                }
                in 48..55 -> {
                    val chessPiece = ChessPiece(i, false, ChessPieceEnum.PAWN.chessPiece)
                    chessBoardArrayList.add(chessPiece)
                }
                else -> {
                    val chessPiece = ChessPiece(i, false, ChessPieceEnum.EMPTY.chessPiece)
                    chessBoardArrayList.add(chessPiece)
                }
            }
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
        previouslyClickedSquare = PreviouslyClickedSquare()
        currentSquare = 0
        binding.ivRestart.isClickable = true
        binding.tvCurrentSquare.text = "--"
        if (countDownTimer != null) {
            countDownTimer.cancel()
            binding.tvTimer.text = "--"
        }
        clickedTime = System.currentTimeMillis()
        initChessboardSquares()
        updateCurrentSquare()
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
        startTraining()
    }

    private fun setObservers() {
        val oldPos = IntArray(1)
        val newPos = IntArray(1)

        val simpleItemTouchCallback =
            object : ItemTouchHelper.SimpleCallback(
                // [1] The allowed directions for moving (drag-and-drop) items
                UP or DOWN or START or END,
                // [2] The allowed directions for swiping items
                0
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    // [3] Do something when an item is moved

                    val adapter = recyclerView.adapter
                    oldPos[0] = viewHolder.adapterPosition
                    newPos[0] = target.adapterPosition

                    // [4] Keep up-to-date the underlying data set
                    Collections.swap(piecesAdapter.arrayList, oldPos[0], newPos[0])
                    // [5] Tell the adapter to switch the 2 items
                    adapter?.notifyItemMoved(oldPos[0], newPos[0])

                    return true
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    // [6] Do something when an item is swiped
                }

                override fun clearView(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ) {
                    super.clearView(recyclerView, viewHolder);
                    moveItem(oldPos[0], newPos[0])
                }
            }
        ItemTouchHelper(simpleItemTouchCallback).attachToRecyclerView(binding.rvChessboard)

    }

    private fun moveItem(oldPos: Int, newPos: Int) {
        val temp: Int = piecesAdapter.arrayList.get(oldPos)
        piecesAdapter.arrayList[oldPos] =
            piecesAdapter.arrayList[newPos]
        piecesAdapter.arrayList[newPos] = temp
        piecesAdapter.notifyItemChanged(oldPos)
        piecesAdapter.notifyItemChanged(newPos)
    }
}