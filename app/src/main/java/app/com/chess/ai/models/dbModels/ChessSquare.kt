package app.com.chess.ai.models.dbModels

import com.google.gson.annotations.SerializedName
import java.util.*

class ChessSquare {
    @SerializedName("list")
    var list: ArrayList<ChessObject>? = null

    class ChessObject {
        @SerializedName("level")
        var level = 0

        @SerializedName("activeSquares")
        var activeSquares: String? = null

        @SerializedName("isUnlocked")
        var isUnlocked = false
    }
}