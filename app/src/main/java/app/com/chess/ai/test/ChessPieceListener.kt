package app.com.chess.ai.test

interface ChessPieceListener {
    fun chessPieceClicked(fromCol: Int, fromRow: Int, col: Int, row: Int)
    fun showToast(message: String)
    fun drawPiece(possibleMovements: ArrayList<RowCol>)
    fun onPgnUpdated(pgn: String)
}