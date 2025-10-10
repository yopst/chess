package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor turn;
    private ChessBoard board;
    private ChessBoard lastBoard;
    private ArrayList<ChessMove> movesMade;

    public boolean finished;

    public ChessGame() {
        setTeamTurn(TeamColor.WHITE);
        board = new ChessBoard();
        board.resetBoard();
        movesMade = new ArrayList<>();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    private void changeTurns() {
        if (turn == TeamColor.WHITE) {
            turn = TeamColor.BLACK;
        }
        else {
            turn = TeamColor.WHITE;
        }
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        Collection<ChessMove> potentialMoves = board.getPiece(startPosition).pieceMoves(board,startPosition);
        for (ChessMove move: potentialMoves) {
            try {
                this.makeMove(move);
                this.undoMove(move);

            } catch (InvalidMoveException e) {
                //don't add
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    public void undoMove(ChessMove move) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && noValidMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (teamColor != turn) {
            return false;
        }
        return !isInCheck(teamColor) && noValidMoves(teamColor);
    }

    public boolean noValidMoves(TeamColor color) {



        throw new RuntimeException("Not implemented");
    }


    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    public ChessPiece promotePiece(ChessMove move) {
        if (move.getPromotionPiece() != null) {
            ChessPiece newPiece;
            TeamColor pieceColor = turn;
            ChessPiece.PieceType type = move.getPromotionPiece();
            newPiece = switch (type) {
                case PAWN -> new ChessPiece(pieceColor, ChessPiece.PieceType.PAWN);
                case ROOK -> new ChessPiece(pieceColor, ChessPiece.PieceType.ROOK);
                case BISHOP -> new ChessPiece(pieceColor, ChessPiece.PieceType.BISHOP);
                case KNIGHT -> new ChessPiece(pieceColor, ChessPiece.PieceType.KNIGHT);
                case QUEEN -> new ChessPiece(pieceColor, ChessPiece.PieceType.QUEEN);
                case KING -> new ChessPiece(pieceColor, ChessPiece.PieceType.KING);
            };
            return newPiece;
        }
        return null;
    }
}
