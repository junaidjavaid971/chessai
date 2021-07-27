package app.com.chess.ai.views.fragments

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper.*
import app.com.chess.ai.R
import app.com.chess.ai.adapters.EndgamePiecesAdapter
import app.com.chess.ai.databinding.FragmentEndgameBinding
import app.com.chess.ai.enums.ChessPieceEnum
import app.com.chess.ai.interfaces.ChessBoardListener
import app.com.chess.ai.models.dbModels.Games
import app.com.chess.ai.models.global.*
import app.com.chess.ai.utils.ChessMovements
import app.com.chess.ai.utils.DatabaseClient
import app.com.chess.ai.views.activities.BaseActivity
import app.com.chess.ai.views.activities.SaveGameActivity
import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.Piece
import com.github.bhlangonijr.chesslib.Side
import com.github.bhlangonijr.chesslib.Square
import com.github.bhlangonijr.chesslib.move.Move
import com.github.bhlangonijr.chesslib.pgn.PgnHolder
import java.io.File
import java.io.FileWriter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class EndgamePiecesFragment : Fragment(), ChessBoardListener, GamesFragment.GameSelectedListener {

    //Global Variables
    var isClickable = false
    var isFirstTime = true
    var clickedTime: Long = 0
    var previousPosition = 100
    var currentFenIndex = 100
    var pgn = "1. "
    var moveCount = 0
    var fen = "PPPPPPPP/RNBQKBNR/8/8/8/8/rnbqkbnr/pppppppp w KQkq - 0 1"
    var isFenLoaded = false

    //Arrays
    var chessboardSquares = HashMap<Int, String>()
    var chessSquareMappings = HashMap<String, String>()
    var chessBoardArrayList: ArrayList<ChessPiece> = ArrayList()
    var fenArrayList: ArrayList<String> = ArrayList()
    var positionsArrayList: ArrayList<Positions> = ArrayList()

    //Objects
    lateinit var binding: FragmentEndgameBinding
    var baseActivity: BaseActivity<FragmentEndgameBinding>? = null
    lateinit var piecesAdapter: EndgamePiecesAdapter
    var previouslyClickedSquare = PreviouslyClickedSquare()
    var pgnArraylist: ArrayList<PGN> = ArrayList()
    val board = Board()
    lateinit var move: Move

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
        isClickable = true
        bindBoardRecyclerView(false, false)

        binding.ivSave.setOnClickListener {
            val intent = Intent(activity, SaveGameActivity::class.java)
            intent.putExtra("pgn", pgn)
            intent.putStringArrayListExtra("fenArray", fenArrayList)
            intent.putExtra("positionArray", positionsArrayList)
            activity?.startActivity(intent)
        }

        binding.ivLoad.setOnClickListener {
            val getGames = GetAllGames()
            getGames.execute()
        }


        binding.ivArrowLeft.setOnClickListener {
            if (fenArrayList.isEmpty() || currentFenIndex < 0) {
                return@setOnClickListener
            } else {
                currentFenIndex--
                if (currentFenIndex != 99 && currentFenIndex > fenArrayList.size) {
                    currentFenIndex = fenArrayList.size - 1
                }
                if (currentFenIndex == fenArrayList.size) {
                    currentFenIndex--
                }
                if (currentFenIndex < 0) {
                    currentFenIndex = 0
                    return@setOnClickListener
                }
                if (currentFenIndex == 99) {
                    currentFenIndex = fenArrayList.size - 1
                    board.loadFromFen(fenArrayList[currentFenIndex])
                    piecesAdapter.setPreviousandCurrentIndexes(
                        positionsArrayList[currentFenIndex].previousPosition,
                        positionsArrayList[currentFenIndex].currentPosition
                    )
                    bindBoardRecyclerView(true, false)
                } else {
                    if (currentFenIndex == 0) {
                        board.loadFromFen(fen)
                    } else {
                        board.loadFromFen(fenArrayList[currentFenIndex])
                    }
                    piecesAdapter.setPreviousandCurrentIndexes(
                        positionsArrayList[currentFenIndex].previousPosition,
                        positionsArrayList[currentFenIndex].currentPosition
                    )
                    bindBoardRecyclerView(true, false)
                }
            }
        }

        binding.ivArrowRight.setOnClickListener {
            if (fenArrayList.isEmpty() || currentFenIndex < 0) {
                return@setOnClickListener
            } else {
                currentFenIndex++
                if (currentFenIndex != 100 && currentFenIndex != 101 && currentFenIndex > fenArrayList.size) {
                    currentFenIndex = fenArrayList.size - 1
                }
                if (currentFenIndex == 100 || currentFenIndex == 101) {
                    currentFenIndex = fenArrayList.size - 1
                    board.loadFromFen(fenArrayList[currentFenIndex])
                    piecesAdapter.setPreviousandCurrentIndexes(
                        positionsArrayList[currentFenIndex].previousPosition,
                        positionsArrayList[currentFenIndex].currentPosition
                    )
                    bindBoardRecyclerView(true, isLeftKey = true)
                } else {
                    if (currentFenIndex < fenArrayList.size) {
                        board.loadFromFen(fenArrayList[currentFenIndex])
                        piecesAdapter.setPreviousandCurrentIndexes(
                            positionsArrayList[currentFenIndex].previousPosition,
                            positionsArrayList[currentFenIndex].currentPosition
                        )
                        bindBoardRecyclerView(true, true)
                    }
                }
            }
        }
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

        this.previousPosition = chessPiece.position

        if (chessPiece.piece == ChessPieceEnum.PAWN.chessPiece) {
            if (chessPiece.isBlack) {
                movementList = chessMovements.getBlackPawnMovement(position)
            } else {
                movementList = chessMovements.getPawnMovement(position)
            }
        } else if (chessPiece.piece == ChessPieceEnum.ROOK.chessPiece) {
            movementList = chessMovements.getRook(position, chessBoardArrayList)
        } else if (chessPiece.piece == ChessPieceEnum.BISHOP.chessPiece) {
            movementList = chessMovements.getBishopMovement(position, chessBoardArrayList)
        } else if (chessPiece.piece == ChessPieceEnum.QUEEN.chessPiece) {
            movementList = chessMovements.getQueenMovement(position, chessBoardArrayList)
        } else if (chessPiece.piece == ChessPieceEnum.KING.chessPiece) {
            movementList = chessMovements.getKingMovement(position, chessBoardArrayList)
        } else if (chessPiece.piece == ChessPieceEnum.KNIGHT.chessPiece) {
            movementList = chessMovements.getKnightMovement(position, chessBoardArrayList)
        }

        binding.rvChessboard.layoutManager = GridLayoutManager(requireActivity(), 8)
        piecesAdapter =
            EndgamePiecesAdapter(
                requireActivity(),
                this,
                previouslyClickedSquare,
                chessBoardArrayList,
                movementList
            )
        binding.rvChessboard.adapter = piecesAdapter
    }

    override fun onChessSquareMoved(chessPiece: ChessPiece, position: Int) {
        val positions = Positions(previousPosition, position)
        positionsArrayList.add(positions)
        if (board.sideToMove.name == "BLACK") {
            val ms =
                chessboardSquares[previousPosition]?.toLowerCase() + "" + chessboardSquares[chessPiece.position]?.toLowerCase()
            move = Move(ms, Side.BLACK)
            if (board.isMoveLegal(move, false)) {

                board.doMove(move)

                fenArrayList.add(board.fen)

                val pgnObj = pgnArraylist[moveCount]
                var targetMove =
                    chessSquareMappings[chessboardSquares[position]].toString().toLowerCase()
                if (chessBoardArrayList[previousPosition].piece == ChessPieceEnum.BISHOP.chessPiece) {
                    targetMove = "B$targetMove"
                } else if (chessBoardArrayList[previousPosition].piece == ChessPieceEnum.KING.chessPiece) {
                    targetMove = "K$targetMove"
                } else if (chessBoardArrayList[previousPosition].piece == ChessPieceEnum.ROOK.chessPiece) {
                    targetMove = "R$targetMove"
                } else if (chessBoardArrayList[previousPosition].piece == ChessPieceEnum.KNIGHT.chessPiece) {
                    targetMove = "N$targetMove"
                } else if (chessBoardArrayList[previousPosition].piece == ChessPieceEnum.QUEEN.chessPiece) {
                    targetMove = "Q$targetMove"
                }
                pgnObj.blackMove = targetMove
                pgnArraylist[moveCount] = pgnObj
                moveCount++
            }
        } else {
            val ms = chessboardSquares[previousPosition] + "" + chessboardSquares[position]
            move = Move(ms, Side.WHITE)
            if (board.isMoveLegal(move, false)) {

                board.doMove(move)
                fenArrayList.add(board.fen)

                val pgn = PGN()
                var targetMove =
                    chessSquareMappings[chessboardSquares[position]].toString().toLowerCase()
                if (chessBoardArrayList[previousPosition].piece == ChessPieceEnum.BISHOP.chessPiece) {
                    targetMove = "B$targetMove"
                } else if (chessBoardArrayList[previousPosition].piece == ChessPieceEnum.KING.chessPiece) {
                    targetMove = "K$targetMove"
                } else if (chessBoardArrayList[previousPosition].piece == ChessPieceEnum.ROOK.chessPiece) {
                    targetMove = "R$targetMove"
                } else if (chessBoardArrayList[previousPosition].piece == ChessPieceEnum.KNIGHT.chessPiece) {
                    targetMove = "N$targetMove"
                } else if (chessBoardArrayList[previousPosition].piece == ChessPieceEnum.QUEEN.chessPiece) {
                    targetMove = "Q$targetMove"
                }
                pgn.whiteMove = targetMove
                pgnArraylist.add(pgn)
            }
        }


        pgn = ""
        for (i in 0 until pgnArraylist.size) {
            val pgnObj = pgnArraylist[i]
            if (pgnObj.blackMove == null) {
                pgn += (i + 1).toString() + ". " + pgnArraylist[i].whiteMove + " "
            } else {
                pgn += (i + 1).toString() + ". " + pgnArraylist[i].whiteMove + " " + pgnArraylist[i].blackMove + " "
            }
        }

        savePgn()
        binding.tvPgn.text = pgn


        getChessArray()
        piecesAdapter =
            EndgamePiecesAdapter(
                requireActivity(),
                this,
                previouslyClickedSquare,
                chessBoardArrayList,
                ArrayList()
            )
        binding.rvChessboard.adapter = piecesAdapter
    }

    private fun savePgn() {
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
        writer.append(pgn + "0-1\n")
        writer.flush()
        writer.close()
        binding.tvPgn.text = pgn

        val pgnParser = PgnHolder("/storage/emulated/0/pgnfile.pgn")
        pgnParser.loadPgn()
    }

    private fun initChessboardSquares() {
        var index = 0
        for (i in 1..8) {
            for (j in 65..72) {
                chessboardSquares[index] = j.toChar().toString() + i
                index++

                if (i == 8 && j == 72) {
                    mapChessSquares()
                }
            }
        }
    }


    private fun mapChessSquares() {
        if (chessboardSquares.size == 64) {
            for (i in 0 until chessboardSquares.size) {
                val alphabet =
                    chessboardSquares[i].toString()
                        .substring(0, chessboardSquares[i].toString().length - 1);
                val digit = chessboardSquares[i].toString().substring(1).toInt()
                chessSquareMappings[chessboardSquares[i].toString()] = alphabet + (9 - digit)
            }
        } else {
            baseActivity?.showToast("Not ready yet. ")
        }
    }

    private fun bindBoardRecyclerView(sendPositions: Boolean, isLeftKey: Boolean) {
        getChessArray()
        binding.rvChessboard.layoutManager = GridLayoutManager(requireActivity(), 8)
        piecesAdapter =
            EndgamePiecesAdapter(
                requireActivity(),
                this,
                previouslyClickedSquare,
                chessBoardArrayList,
                ArrayList()
            )
        if (sendPositions) {
            piecesAdapter.setPreviousandCurrentIndexes(
                positionsArrayList[currentFenIndex].previousPosition,
                positionsArrayList[currentFenIndex].currentPosition
            )
            if (!isLeftKey) {
                if (currentFenIndex < 0) {
                    currentFenIndex = 0
                }
            } else {
                if (currentFenIndex == positionsArrayList.size) {
                    currentFenIndex--
                }
            }
        }
        binding.rvChessboard.adapter = piecesAdapter
    }

    private fun getChessArray() {
        chessBoardArrayList.clear()
        for (n in 0..63) {
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
            val chessPiece = ChessPiece(
                sqare.ordinal,
                true,
                ChessPieceEnum.BISHOP.chessPiece,
                Square.squareAt(sqare.ordinal)
            )
            chessBoardArrayList[sqare.ordinal] = chessPiece
        }
        for (sqare in whiteBishopSquares) {
            val chessPiece = ChessPiece(
                sqare.ordinal, false, ChessPieceEnum.BISHOP.chessPiece,
                Square.squareAt(sqare.ordinal)
            )
            chessBoardArrayList[sqare.ordinal] = chessPiece
        }
        for (sqare in blackRookSquares) {
            val chessPiece = ChessPiece(
                sqare.ordinal, true, ChessPieceEnum.ROOK.chessPiece,
                Square.squareAt(sqare.ordinal)
            )
            chessBoardArrayList[sqare.ordinal] = chessPiece
        }
        for (sqare in whiteRookSquares) {
            val chessPiece = ChessPiece(
                sqare.ordinal, false, ChessPieceEnum.ROOK.chessPiece,
                Square.squareAt(sqare.ordinal)
            )
            chessBoardArrayList[sqare.ordinal] = chessPiece
        }
        for (sqare in blackKnightSquares) {
            val chessPiece = ChessPiece(
                sqare.ordinal, true, ChessPieceEnum.KNIGHT.chessPiece,
                Square.squareAt(sqare.ordinal)
            )
            chessBoardArrayList[sqare.ordinal] = chessPiece
        }
        for (sqare in whiteKnightSquares) {
            val chessPiece = ChessPiece(
                sqare.ordinal, false, ChessPieceEnum.KNIGHT.chessPiece,
                Square.squareAt(sqare.ordinal)
            )
            chessBoardArrayList[sqare.ordinal] = chessPiece
        }
        for (sqare in whitePawnSquares) {
            val chessPiece = ChessPiece(
                sqare.ordinal, false, ChessPieceEnum.PAWN.chessPiece,
                Square.squareAt(sqare.ordinal)
            )
            chessBoardArrayList[sqare.ordinal] = chessPiece
        }
        for (sqare in blackPawnSquares) {
            val chessPiece = ChessPiece(
                sqare.ordinal, true, ChessPieceEnum.PAWN.chessPiece,
                Square.squareAt(sqare.ordinal)
            )
            chessBoardArrayList[sqare.ordinal] = chessPiece
        }
        for (sqare in blackKingSquares) {
            val chessPiece = ChessPiece(
                sqare.ordinal, true, ChessPieceEnum.KING.chessPiece,
                Square.squareAt(sqare.ordinal)
            )
            chessBoardArrayList[sqare.ordinal] = chessPiece
        }
        for (sqare in whiteKingSquares) {
            val chessPiece = ChessPiece(
                sqare.ordinal, false, ChessPieceEnum.KING.chessPiece,
                Square.squareAt(sqare.ordinal)
            )
            chessBoardArrayList[sqare.ordinal] = chessPiece
        }
        for (sqare in blackQueenSquares) {
            val chessPiece = ChessPiece(
                sqare.ordinal, true, ChessPieceEnum.QUEEN.chessPiece,
                Square.squareAt(sqare.ordinal)
            )
            chessBoardArrayList[sqare.ordinal] = chessPiece
        }
        for (sqare in whiteQueenSquares) {
            val chessPiece = ChessPiece(
                sqare.ordinal, false, ChessPieceEnum.QUEEN.chessPiece,
                Square.squareAt(sqare.ordinal)
            )
            chessBoardArrayList[sqare.ordinal] = chessPiece
        }
        reverseArray(chessBoardArrayList)
    }

    private fun reverseArray(chessPiecesList: ArrayList<ChessPiece>) {
//        chessPiecesList.reverse()
        var arrayList = ArrayList<ChessPiece>()
        for (n in 0..63) {
            val chessPiece =
                ChessPiece(n, false, ChessPieceEnum.EMPTY.chessPiece, Square.squareAt(n))
            arrayList.add(chessPiece)
        }
        for (i in 0 until chessPiecesList.size) {
            val pos = chessPiecesList[i].position
            var temp = chessPiecesList[i].position
            temp -= 7
            if (temp < 0)
                temp *= (-1)

            arrayList[i].square = chessBoardArrayList[63 - temp].square
            arrayList[i].piece = chessBoardArrayList[63 - temp].piece
            arrayList[i].isBlack = chessBoardArrayList[63 - temp].isBlack
            arrayList[i].position = i
        }
        chessBoardArrayList = arrayList
    }

    inner class GetAllGames : AsyncTask<Void?, Void?, List<Games>>() {
        override fun onPostExecute(tasks: List<Games>) {
            super.onPostExecute(tasks)
            baseActivity?.replaceFragment(GamesFragment(tasks, this@EndgamePiecesFragment))
        }

        override fun doInBackground(vararg params: Void?): List<Games> {
            return DatabaseClient
                .getInstance(activity?.applicationContext)
                .appDatabase
                .gameDao()
                .allGames
        }
    }

    override fun onGameSelected(game: Games) {
        fenArrayList = game.fenList!!
        positionsArrayList = game.positionsList!!
        pgn = game.pgn.toString()
        isFenLoaded = false
        if (fenArrayList.isNotEmpty()) {
            fen = fenArrayList[fenArrayList.size - 1]
        }
        binding.tvPgn.text = game.pgn

        bindBoardRecyclerView(false, false)
    }
}