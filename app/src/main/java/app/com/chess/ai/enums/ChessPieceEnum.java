package app.com.chess.ai.enums;

public enum ChessPieceEnum {
    EMPTY(0),
    PAWN(1),
    ROOK(2),
    BISHOP(3),
    KNIGHT(4),
    QUEEN(5),
    KING(6);

    ChessPieceEnum(
            int chessPiece) {
        this.chessPiece = chessPiece;
    }

    private final int chessPiece;

    public int getChessPiece() {
        return chessPiece;
    }
}