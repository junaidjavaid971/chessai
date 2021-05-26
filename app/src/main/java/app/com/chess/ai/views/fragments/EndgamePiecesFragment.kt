package app.com.chess.ai.views.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
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
import app.com.chess.ai.adapters.PiecesAdapter
import app.com.chess.ai.databinding.FragmentEndgameBinding
import app.com.chess.ai.databinding.ProgressDialogRowBinding
import app.com.chess.ai.enums.ChessPieceEnum
import app.com.chess.ai.interfaces.ChessBoardListener
import app.com.chess.ai.models.global.ChessPiece
import app.com.chess.ai.models.global.Displayer
import app.com.chess.ai.models.global.PreviouslyClickedSquare
import app.com.chess.ai.utils.ChessMovements
import app.com.chess.ai.utils.SharePrefData
import app.com.chess.ai.views.activities.BaseActivity
import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.Piece
import com.github.bhlangonijr.chesslib.Side
import com.github.bhlangonijr.chesslib.Square
import com.github.bhlangonijr.chesslib.move.Move
import com.github.bhlangonijr.chesslib.pgn.PgnHolder
import java.io.File
import java.io.FileWriter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class EndgamePiecesFragment : Fragment(), ChessBoardListener {

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
    lateinit var binding: FragmentEndgameBinding
    var baseActivity: BaseActivity<FragmentEndgameBinding>? = null
    lateinit var piecesAdapter: PiecesAdapter
    var previouslyClickedSquare = PreviouslyClickedSquare()
    lateinit var countDownTimer: CountDownTimer
    val board = Board()
    var fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 4"
    var pgn = ""
    var moveCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_endgame, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        baseActivity = activity as BaseActivity<FragmentEndgameBinding>?
        clickedTime = System.currentTimeMillis()
        initChessboardSquares()
        updateCurrentSquare()

        val board = Board()
        board.loadFromFen(fen)
        val moves = board.legalMoves()
        print("Legal Moves $moves")

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
        if (board.sideToMove.value().equals("WHITE") && chessPiece.isBlack) {
            baseActivity?.showToast("It is white's turn to make a move")
            return
        } else if (board.sideToMove.value().equals("BLACK") && !chessPiece.isBlack) {
            baseActivity?.showToast("It is black's turn to make a move")
            return
        }
        val chessPiece = chessBoardArrayList[position];
        var movementList: ArrayList<Int> = ArrayList()
        val chessMovements = ChessMovements()
        if (chessPiece.piece == ChessPieceEnum.PAWN.chessPiece) {
            this.currentPosition = position

            if (chessPiece.isBlack) {
                movementList = chessMovements.getPawnMovement(position)
            } else {
                movementList = chessMovements.getBlackPawnMovement(position)
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
        var moveString = ""
        if (chessPiece.isBlack) {
            val ms =
                chessboardSquares[currentPosition]?.toLowerCase() + "" + chessboardSquares[position]?.toLowerCase()
            val move = Move(ms, Side.BLACK)
            if (board.isMoveLegal(move, false)) {
                board.doMove(move)
            }
        } else {
            val ms = chessboardSquares[currentPosition] + "" + chessboardSquares[position]
            val move = Move(ms, Side.WHITE)
            if (board.isMoveLegal(move, false)) {
                board.doMove(move)
            }
        }
        moveString =
            chessboardSquares[currentPosition]?.toLowerCase() + " " + chessboardSquares[position]?.toLowerCase()
        moveCount++
        pgn += "$moveCount. $moveString "

        getChessArray()
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
        for (i in 1..8) {
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
//                showDialog()
                val file = File(Environment.getExternalStorageDirectory(), "pgnfile.pgn");
                if (file.exists()) {
                    file.delete()
                }
                file.createNewFile()
                val writer = FileWriter(file)
                writer.append("[Event \"Canadian Open\"]\n")
                writer.append("[Site \"Edmonton CAN\"]\n")
                writer.append("[Date \"2005.07.16\"]\n")
                writer.append("[EventDate \"2005.07.09\"]\n")
                writer.append("[Result \"0-1\"]\n")
                writer.append("[Round \"1\"]\n")
                writer.append("[White \"Alexander Shabalov\"]\n")
                writer.append("[Black \"Alexey Shirov\"]\n")
                writer.append("[ECO \"C02\"]\n")
                writer.append("[WhiteElo \"2593\"]\n")
                writer.append("[BlackElo \"2705\"]\n")
                writer.append("[PlyCount \"60\"]\n")
                /*writer.append(
                    "1. e4 e6 2. d4 d5 3. e5 c5 4. c3 Nc6 5. Nf3 Bd7 6. Be2 Nge7\n" +
                            "7. Na3 cxd4 8. cxd4 Nf5 9. Nc2 Qb6 10. O-O Na5 11. g4 Ne7\n" +
                            "12. Nfe1 Bb5 13. Nd3 h5 14. gxh5 Nf5 15. Be3 Nc6 16. a4 Bc4\n" +
                            "17. b4 Qd8 18. Bg4 Nxe3 19. fxe3 Qg5 20. h3 Rxh5 21. Qf3 O-O-O\n" +
                            "22. Qxf7 Rxh3 23. Qxe6+ Kb8 24. Rxf8 Rg3+ 25. Kf2 Rxg4\n" +
                            "26. Qd6+ Ka8 27. Rxd8+ Nxd8 28. Qd7 Rg2+ 29. Ke1 Qg3+ 30. Kd1\n" +
                            "Qf3+ 0-1"
                )*/
                writer.append(pgn + "0-1\n")
                writer.flush()
                writer.close()
                binding.tvPgn.text = pgn

                val pgnParser = PgnHolder("/storage/emulated/0/pgnfile.pgn")
                pgnParser.loadPgn()
            }
        }.start()
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
        for (n in 63 downTo 0) {
            val chessPiece =
                ChessPiece(n, false, ChessPieceEnum.EMPTY.chessPiece, Square.squareAt(n))
            chessBoardArrayList.add(chessPiece)
        }
        board.loadFromFen(board.fen)

        val blackBishopSquares: List<Square> =
            board.getPieceLocation(Piece.BLACK_BISHOP)
        val whiteBishopSquares: List<Square> =
            board.getPieceLocation(Piece.WHITE_BISHOP)
        val blackRookSquares: List<Square> =
            board.getPieceLocation(Piece.BLACK_ROOK)
        val whiteRookSquares: List<Square> =
            board.getPieceLocation(Piece.WHITE_ROOK)
        val blackKnightSquares: List<Square> =
            board.getPieceLocation(Piece.BLACK_KNIGHT)
        val whiteKnightSquares: List<Square> =
            board.getPieceLocation(Piece.WHITE_KNIGHT)
        val blackPawnSquares: List<Square> =
            board.getPieceLocation(Piece.BLACK_PAWN)
        val whitePawnSquares: List<Square> =
            board.getPieceLocation(Piece.WHITE_PAWN)
        val whiteKingSquares: List<Square> =
            board.getPieceLocation(Piece.WHITE_KING)
        val blackKingSquares: List<Square> =
            board.getPieceLocation(Piece.BLACK_KING)
        val whiteQueenSquares: List<Square> =
            board.getPieceLocation(Piece.WHITE_QUEEN)
        val blackQueenSquares: List<Square> =
            board.getPieceLocation(Piece.BLACK_QUEEN)

        for (sqare in blackBishopSquares) {
            val chessPiece = ChessPiece(sqare.ordinal, true, ChessPieceEnum.BISHOP.chessPiece)
            chessBoardArrayList[sqare.ordinal] = chessPiece
        }
        for (sqare in whiteBishopSquares) {
            val chessPiece = ChessPiece(sqare.ordinal, false, ChessPieceEnum.BISHOP.chessPiece)
            chessBoardArrayList[sqare.ordinal] = chessPiece
        }
        for (sqare in blackRookSquares) {
            val chessPiece = ChessPiece(sqare.ordinal, true, ChessPieceEnum.ROOK.chessPiece)
            chessBoardArrayList[sqare.ordinal] = chessPiece
        }
        for (sqare in whiteRookSquares) {
            val chessPiece = ChessPiece(sqare.ordinal, false, ChessPieceEnum.ROOK.chessPiece)
            chessBoardArrayList[sqare.ordinal] = chessPiece
        }
        for (sqare in blackKnightSquares) {
            val chessPiece = ChessPiece(sqare.ordinal, true, ChessPieceEnum.KNIGHT.chessPiece)
            chessBoardArrayList[sqare.ordinal] = chessPiece
        }
        for (sqare in whiteKnightSquares) {
            val chessPiece = ChessPiece(sqare.ordinal, false, ChessPieceEnum.KNIGHT.chessPiece)
            chessBoardArrayList[sqare.ordinal] = chessPiece
        }
        for (sqare in whitePawnSquares) {
            val chessPiece = ChessPiece(sqare.ordinal, false, ChessPieceEnum.PAWN.chessPiece)
            chessBoardArrayList[sqare.ordinal] = chessPiece
        }
        for (sqare in blackPawnSquares) {
            val chessPiece = ChessPiece(sqare.ordinal, true, ChessPieceEnum.PAWN.chessPiece)
            chessBoardArrayList[sqare.ordinal] = chessPiece
        }
        for (sqare in blackKingSquares) {
            val chessPiece = ChessPiece(sqare.ordinal, true, ChessPieceEnum.KING.chessPiece)
            chessBoardArrayList[sqare.ordinal] = chessPiece
        }
        for (sqare in whiteKingSquares) {
            val chessPiece = ChessPiece(sqare.ordinal, false, ChessPieceEnum.KING.chessPiece)
            chessBoardArrayList[sqare.ordinal] = chessPiece
        }
        for (sqare in blackQueenSquares) {
            val chessPiece = ChessPiece(sqare.ordinal, true, ChessPieceEnum.QUEEN.chessPiece)
            chessBoardArrayList[sqare.ordinal] = chessPiece
        }
        for (sqare in whiteQueenSquares) {
            val chessPiece = ChessPiece(sqare.ordinal, false, ChessPieceEnum.QUEEN.chessPiece)
            chessBoardArrayList[sqare.ordinal] = chessPiece
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