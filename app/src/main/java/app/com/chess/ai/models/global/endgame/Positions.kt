package app.com.chess.ai.models.global.endgame

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

class Positions : Serializable {
    var previousPosition = 0
    var currentPosition = 0

    constructor() {}
    constructor(previousPosition: Int, currentPosition: Int) {
        this.previousPosition = previousPosition
        this.currentPosition = currentPosition
    }
}