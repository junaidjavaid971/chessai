package app.com.chess.ai.models.global;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ChessSquare {
    @SerializedName("list")
    public ArrayList<ChessObject> list;

    static public class ChessObject {
        @SerializedName("level")
        public int level;
        @SerializedName("activeSquares")
        public String activeSquares;
        @SerializedName("isUnlocked")
        public boolean isUnlocked;
    }

    public ArrayList<ChessObject> getList() {
        return list;
    }
}
