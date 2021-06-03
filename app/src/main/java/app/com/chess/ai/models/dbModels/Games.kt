package app.com.chess.ai.models.dbModels

import androidx.room.*
import app.com.chess.ai.models.global.Positions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

@Entity
class Games : Serializable {

    @PrimaryKey(autoGenerate = true)
    var id = 0

    @ColumnInfo(name = "tournamentName")
    var tournamentName: String? = null

    @ColumnInfo(name = "eventDate")
    var eventDate: String? = null

    @ColumnInfo(name = "pgn")
    var pgn: String? = null

    @TypeConverters(FENConverter::class)
    @ColumnInfo(name = "fenList")
    var fenList: ArrayList<String>? = null

    @TypeConverters(PositionsConverter::class)
    @ColumnInfo(name = "positionsList")
    var positionsList: ArrayList<Positions>? = null

    class PositionsConverter {
        @TypeConverter
        fun fromPositionsList(value: ArrayList<Positions>): String {
            val gson = Gson()
            val type = object : TypeToken<ArrayList<Positions>>() {}.type
            return gson.toJson(value, type)
        }

        @TypeConverter
        fun toPositionsList(value: String): ArrayList<Positions> {
            val gson = Gson()
            val type = object : TypeToken<ArrayList<Positions>>() {}.type
            return gson.fromJson(value, type)
        }
    }

    class FENConverter {
        @TypeConverter
        fun fromPositionsList(value: ArrayList<String>): String {
            val gson = Gson()
            val type = object : TypeToken<ArrayList<String>>() {}.type
            return gson.toJson(value, type)
        }

        @TypeConverter
        fun toPositionsList(value: String): ArrayList<String> {
            val gson = Gson()
            val type = object : TypeToken<ArrayList<String>>() {}.type
            return gson.fromJson(value, type)
        }
    }
}