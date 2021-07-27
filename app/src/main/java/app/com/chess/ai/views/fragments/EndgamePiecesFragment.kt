package app.com.chess.ai.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.com.chess.ai.R
import app.com.chess.ai.databinding.FragmentEndgameBinding
import app.com.chess.ai.views.activities.BaseActivity
import app.com.chess.ai.test.ChessDelegate
import app.com.chess.ai.test.ChessModel
import app.com.chess.ai.test.ChessPiece
import app.com.chess.ai.test.ChessView


class EndgamePiecesFragment : Fragment(), ChessDelegate {

    var chessModel = ChessModel()
    lateinit var chessView: ChessView
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

    override fun movePiece(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int) {
        chessModel.movePiece(fromCol, fromRow, toCol, toRow)
        chessView.invalidate()
    }
}