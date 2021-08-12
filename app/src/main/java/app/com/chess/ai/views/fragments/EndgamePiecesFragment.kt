package app.com.chess.ai.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.com.chess.ai.R
import app.com.chess.ai.databinding.FragmentEndgameBinding
import app.com.chess.ai.test.*
import app.com.chess.ai.views.activities.BaseActivity


class EndgamePiecesFragment : Fragment(), ChessDelegate, ChessPieceListener {

    var chessModel = ChessModel(this)
    lateinit var chessView: ChessView
    private val TAG = "EndgameFragment"
    var baseActivity: BaseActivity<FragmentEndgameBinding>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_endgame, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        baseActivity = activity as BaseActivity<FragmentEndgameBinding>?
        chessView = view.findViewById<ChessView>(R.id.rv_chessboard)
        chessView.chessDelegate = this
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
}