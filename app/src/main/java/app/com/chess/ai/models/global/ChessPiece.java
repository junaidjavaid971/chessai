package app.com.chess.ai.models.global;

import com.github.bhlangonijr.chesslib.Square;

public class ChessPiece {
    public boolean isBlack;
    public int piece;
    public int position;
    public Square square;

    public ChessPiece() {
    }

    public ChessPiece(int position, boolean isBlack, int piece) {
        this.isBlack = isBlack;
        this.piece = piece;
        this.position = position;
    }

    public ChessPiece(int position, boolean isBlack, int piece, Square square) {
        this.isBlack = isBlack;
        this.piece = piece;
        this.position = position;
        this.square = square;
    }
}
