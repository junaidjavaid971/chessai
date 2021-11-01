package app.com.chess.ai.views.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import app.com.chess.ai.R
import app.com.chess.ai.adapters.PgnAdapter
import app.com.chess.ai.databinding.FragmentEndgameBinding
import app.com.chess.ai.interfaces.ChessDelegate
import app.com.chess.ai.interfaces.ChessPieceListener
import app.com.chess.ai.models.global.endgame.ChessModel
import app.com.chess.ai.models.global.endgame.ChessPiece
import app.com.chess.ai.models.global.endgame.RowCol
import app.com.chess.ai.views.ChessView
import app.com.chess.ai.views.activities.BaseActivity
import app.com.chess.ai.views.activities.endgames.SaveGameActivity
import com.github.bhlangonijr.chesslib.pgn.PgnHolder
import java.io.File
import java.io.FileWriter


class EndgamePiecesFragment : Fragment(), ChessDelegate, ChessPieceListener {

    var chessModel = ChessModel(this)
    lateinit var chessView: ChessView
    private val TAG = "EndgameFragment"
    var baseActivity: BaseActivity<FragmentEndgameBinding>? = null
    lateinit var binding: FragmentEndgameBinding
    val pgnArrayList = ArrayList<String>()
    lateinit var pgnAdapter: PgnAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater, R.layout.fragment_endgame, container, false
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        baseActivity = activity as BaseActivity<FragmentEndgameBinding>?
        chessView = view.findViewById<ChessView>(R.id.rv_chessboard)
        chessView.chessDelegate = this

        binding.ivArrowLeft.setOnClickListener {
            pgnAdapter.currentMoveIndex--
            if (pgnAdapter.currentMoveIndex < 0) {
                pgnAdapter.currentMoveIndex = 0
            }
            pgnAdapter.notifyDataSetChanged()
            chessModel.restoreLeftMove()
            chessView.invalidate()
        }

        binding.ivArrowRight.setOnClickListener {
            pgnAdapter.currentMoveIndex++
            if (pgnAdapter.currentMoveIndex >= pgnArrayList.size) {
                pgnAdapter.currentMoveIndex = pgnArrayList.size - 1
            }
            pgnAdapter.notifyDataSetChanged()
            chessModel.restoreRightMove()
            chessView.invalidate()
        }

        binding.ivSave.setOnClickListener {
            val intent = Intent(activity, SaveGameActivity::class.java)
            intent.putExtra("pgn", chessModel.generatePGN())
            startActivity(intent)
        }

        setPgnAdapter()
    }

    override fun pieceAt(col: Int, row: Int): ChessPiece? {
        return chessModel.pieceAt(col, row)
    }

    override fun possibleMovementAt(col: Int, row: Int): ChessPiece? {
        return chessModel.possiblePieceAt(col, row)
    }

    override fun makeMove(col: Int, row: Int) {
        for (i in 0..3) {
            chessModel.clearPossibleMovements()
        }
        chessModel.makeMove(col, row)
    }

    override fun movePiece(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int) {
        chessModel.movePiece(fromCol, fromRow, toCol, toRow)
        Log.d(TAG, "from ($fromCol , $fromRow) to (${toCol} , ${toCol})")
        for (i in 0..3) {
            chessModel.clearPossibleMovements()
        }
        chessView.invalidate()
    }

    override fun chessPieceClicked(fromCol: Int, fromRow: Int, col: Int, row: Int) {
        movePiece(fromCol, fromRow, col, row)
    }

    override fun showToast(string: String) {
        baseActivity?.showToast(string)
    }

    override fun drawPiece(possibleMovements: ArrayList<RowCol>) {
        chessModel.drawPossibleMovements(possibleMovements)
        chessView.invalidate()
    }

    private fun setPgnAdapter() {
        pgnAdapter = PgnAdapter(pgnArrayList, pgnArrayList.size, requireActivity())
        binding.rvPgn.layoutManager = GridLayoutManager(
            activity,
            8,
        )
        binding.rvPgn.adapter = pgnAdapter
    }

    private fun savePGN() {
        val file = File(Environment.getExternalStorageDirectory(), "updated_pgn.pgn");
        if (file.exists()) {
            file.delete()
        }
        val pgn = chessModel.generatePGN()
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
        writer.append(pgn)
        writer.append(pgn + "0-1\n")
        writer.flush()
        writer.close()
//        binding.tvPgn.text = pgn
        val pgnParser = PgnHolder("/storage/emulated/0/updated_pgn.pgn")
        pgnParser.loadPgn()
    }

    override fun onPgnUpdated(pgn: String) {
        pgnArrayList.add(pgn)
        pgnAdapter.currentMoveIndex = pgnArrayList.size - 1
        pgnAdapter.notifyDataSetChanged()
    }
}