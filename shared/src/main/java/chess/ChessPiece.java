package chess;

import chess.calculators.*;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor color;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        color = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return getCalc().calcMoves(board, myPosition);
    }

    @Override
    public String toString() {
        switch(type) {
            case BISHOP:
                if (color == ChessGame.TeamColor.BLACK) {
                    return "b";
                }
                return "B";
            case KNIGHT:
                if (color == ChessGame.TeamColor.BLACK) {
                    return "n";
                }
                return "N";
            case ROOK:
                if (color == ChessGame.TeamColor.BLACK) {
                    return "r";
                }
                return "R";
            case QUEEN:
                if (color == ChessGame.TeamColor.BLACK) {
                    return "q";
                }
                return "Q";
            case PAWN:
                if (color == ChessGame.TeamColor.BLACK) {
                    return "p";
                }
                return "P";
            case KING:
                if (color == ChessGame.TeamColor.BLACK) {
                    return "k";
                }
                return "K";
            default:
                throw new IllegalArgumentException("Unknown Piece Type: " + type);
        }
    }

    private MoveCalc getCalc() {
        switch (type) {
            case KING:
                return new KingCalc();
            case QUEEN:
                return new QueenCalc();
            case BISHOP:
                return new BishopCalc();
            case KNIGHT:
                return new KnightCalc();
            case ROOK:
                return new RookCalc();
            case PAWN:
                return new PawnCalc();
            default:
                throw new RuntimeException("Unexpected Type in getCalc");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) obj;
        return Objects.equals(this.toString(), that.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }
}
