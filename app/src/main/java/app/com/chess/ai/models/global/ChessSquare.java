package app.com.chess.ai.models.global;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ChessSquare {
    @SerializedName("list")
    public ArrayList<ChessObject> list;

    static public class ChessObject {
        @SerializedName("position")
        public int position;
        @SerializedName("isActive")
        public boolean isActive;
    }

    public ArrayList<ChessObject> getList() {
        return list;
    }
}
