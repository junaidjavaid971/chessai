package app.com.chess.ai.views.activities

import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import app.com.chess.ai.R
import app.com.chess.ai.databinding.LayoutSaveGameBinding
import app.com.chess.ai.models.dbModels.Games
import app.com.chess.ai.models.global.Positions
import app.com.chess.ai.utils.DatabaseClient
import com.github.bhlangonijr.chesslib.pgn.PgnHolder
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SaveGameActivity : BaseActivity<LayoutSaveGameBinding>() {
    private var pgn = ""
    private var tournamentName = ""
    private var black = ""
    private var white = ""
    private var result = ""
    var fenArrayList: ArrayList<String> = ArrayList()
    var positionArrayList: ArrayList<Positions> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView(R.layout.layout_save_game)

        pgn = intent?.getStringExtra("pgn").toString()
//        fenArrayList = intent?.getStringArrayListExtra("fenArray") as ArrayList<String>
//        positionArrayList = (intent?.getSerializableExtra("positionArray") as ArrayList<Positions>)

        binding?.btnSave?.setOnClickListener {
            if (validateInfo()) {
                savePgn()
            }
        }
    }

    private fun validateInfo(): Boolean {
        tournamentName = binding?.edName?.text.toString()
        black = binding?.edBlack?.text.toString()
        white = binding?.edWhite?.text.toString()
        result = binding?.spinnerResult?.selectedItem.toString()

        if (tournamentName.isEmpty()) {
            showToast("Tournament name cannot be empty!")
            return false
        } else if (black.isEmpty()) {
            showToast("Black cannot be empty!")
            return false
        } else if (white.isEmpty()) {
            showToast("White cannot be empty!")
            return false
        }
        return true
    }

    private fun savePgn() {
        val file = File(Environment.getExternalStorageDirectory(), "pgnFile.pgn");
        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()
        val writer = FileWriter(file)
        writer.append("[Event \"$tournamentName\"]\n")
        writer.append("[Site \"Edmonton CAN\"]\n")
        writer.append("[Date \"${getTodayDate()}\"]\n")
        writer.append("[EventDate \"${getTodayDate()}\"]\n")
        writer.append("[Result \"$result\"]\n")
        writer.append("[Round \"1\"]\n")
        writer.append("[White \"$black\"]\n")
        writer.append("[Black \"$white\"]\n")
        writer.append("[ECO \"C02\"]\n")
        writer.append("[WhiteElo \"2593\"]\n")
        writer.append("[BlackElo \"2705\"]\n")
        writer.append("[PlyCount \"60\"]\n")
        writer.append(pgn + result + "\n")
        writer.flush()
        writer.close()

        val pgnParser = PgnHolder("/storage/emulated/0/pgnFile.pgn")
        pgnParser.loadPgn()

        /*val game = Games()
        game.tournamentName = tournamentName
        game.eventDate = getTodayDate()
        game.pgn = pgn

        game.fenList = fenArrayList
        game.positionsList = positionArrayList

        val saveGame = SaveGame()
        saveGame.execute(game)*/
    }

    private fun getTodayDate(): String {
        val c: Date = Calendar.getInstance().getTime()
        println("Current time => $c")

        val df = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        val formattedDate: String = df.format(c)

        return formattedDate
    }

    inner class SaveGame : AsyncTask<Games?, Void?, Void?>() {

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            finish()
            Toast.makeText(applicationContext, "Saved", Toast.LENGTH_LONG).show()
            finish()
        }

        override fun doInBackground(vararg params: Games?): Void? {
            val game = params[0] as Games

            DatabaseClient.getInstance(applicationContext).appDatabase
                .gameDao()
                .insert(game)
            return null
        }
    }
}