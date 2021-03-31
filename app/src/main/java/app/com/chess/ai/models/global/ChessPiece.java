package app.com.chess.ai.models.global;

public class ChessPiece {
    public boolean isBlack;
    public int piece;

    public ChessPiece() {
    }

    public ChessPiece(int position, boolean isBlack, int piece) {
        this.isBlack = isBlack;
        this.piece = piece;
    }
}
