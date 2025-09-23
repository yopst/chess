package chess;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] board = new ChessPiece[8][8];

    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        board = new ChessPiece[8][8];

        //set PAWNS
        for (int column = 1; column <= 8; column++) {
            ChessPiece whitePawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            ChessPiece blackPawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            addPiece(new ChessPosition(2, column), whitePawn);
            addPiece(new ChessPosition(7, column), blackPawn);
        }
        //set ROOKS
        for (int column = 1; column <= 8; column +=7) {
            ChessPiece whiteRook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
            ChessPiece blackRook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
            addPiece(new ChessPosition(1, column), whiteRook);
            addPiece(new ChessPosition(8, column), blackRook);
        }

        //set KNIGHTS
        for (int column = 2; column <= 7; column +=5) {
            ChessPiece whiteKnight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
            ChessPiece blackKnight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
            addPiece(new ChessPosition(1, column), whiteKnight);
            addPiece(new ChessPosition(8, column), blackKnight);
        }
        // set BISHOPS
        for (int column = 3; column <= 6; column += 3) {
            ChessPiece whiteBishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
            ChessPiece blackBishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
            addPiece(new ChessPosition(1, column), whiteBishop);
            addPiece(new ChessPosition(8, column), blackBishop);
        }

        //set QUEENS
        ChessPiece whiteQueen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        ChessPiece blackQueen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);

        ChessPosition whiteQueenPosition = new ChessPosition(1,4);
        ChessPosition blackQueenPosition = new ChessPosition(8,4);

        addPiece(whiteQueenPosition, whiteQueen);
        addPiece(blackQueenPosition, blackQueen);

        //set KINGS
        ChessPiece whiteKing = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        ChessPiece blackKing = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);

        ChessPosition whiteKingPosition = new ChessPosition(1, 5);
        ChessPosition blackKingPosition = new ChessPosition(8,5);

        addPiece(whiteKingPosition, whiteKing);
        addPiece(blackKingPosition, blackKing);
    }

    public String toString() { //prints from blacks perspective
        StringBuilder sb = new StringBuilder();
        for (ChessPiece[] row : board) {
            for (ChessPiece piece: row) {
                sb.append("|");
                if (piece != null) {
                    sb.append(piece);
                }
                else {
                    sb.append(" ");
                }
                sb.append("|");
            }
            sb.append("\n");
        }
        return sb.toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.equals(this.toString(), that.toString());
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    public boolean emptySpaceOnBoard(ChessPosition position) {
        if (!this.onBoard(position)) {
            return false;
        }
        return this.getPiece(position) == null;
    }

    public boolean onBoard(ChessPosition position) {
        return position.getRow() <= 8
                && position.getRow() >= 1
                && position.getColumn() <= 8
                && position.getColumn() >= 1;
    }
}
