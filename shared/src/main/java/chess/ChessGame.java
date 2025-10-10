package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

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
                boolean wasInCheck = this.isInCheck(turn);
                TeamColor colorInCheck = turn;
                this.makeMove(move);

                if (wasInCheck && this.isInCheck(colorInCheck)) {
                    this.undoMove();
                    throw new InvalidMoveException("Invalid Move: Move does not get you out of check.");
                }
                validMoves.add(move);
                this.undoMove();
            } catch (InvalidMoveException e) {
                //don't add
            }
        }
        return validMoves;
    }

    /**
     * Checks conditions of a legal move unrelated to the King
     * Is it your turn and is in pieceMoves for the moving piece?
     */
    public void checkWithLegal (ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece movingPiece = board.getPiece(start);

        if (movingPiece == null) {
            throw new InvalidMoveException(
                    "Invalid move: there is no piece at start position.");
        }
        if (movingPiece.getTeamColor() != turn) {
            throw new InvalidMoveException(
                    "Invalid move: The piece being moved is not of the teamcolor who's turn it is.");
        }
        //hopefully redundant
        if (!board.onBoard(end)) {
            throw new InvalidMoveException("Invalid move: End position must be on the board.");
        }
        if (!board.onBoard(start) || board.emptySpaceOnBoard(start)) {
            throw new InvalidMoveException(
                    "Invalid move: Start position must be on the board and not empty.");
        }

        Collection<ChessMove> potentialMoves = board.getPiece(start).pieceMoves(board,start);
        if (!potentialMoves.contains(move)) {
            throw new InvalidMoveException(
                    "Invalid Move: Move being attempted not contained in potential moves.");
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece movingPiece = board.getPiece(start);
        this.checkWithLegal(move);

        //physically move piece overwriting if attack
        lastBoard = board; //not sure about this copy
        board.addPiece(start, null);
        if (move.getPromotionPiece() == null) {
            board.addPiece(end, movingPiece);
        }
        else {
            board.addPiece(end, this.promotePiece(move));
        }
        movesMade.add(move);
        this.changeTurns();
    }


    public void undoMove() {
        if (movesMade.isEmpty()) {
            throw new RuntimeException("No moves have been made to undo.");
        }
        if (lastBoard == null) {
            throw new RuntimeException("Can't remember what the board looked like.");
        }
        board = lastBoard;
        lastBoard = null; //no duplicate undos
        movesMade.removeLast();
        this.changeTurns();
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKing(teamColor);
        //for each pieceType, determine if valid moves at positions of opponent piece of pieceType are null or there
        //Pawn attack (attack moves different from valid moves)
        ChessPosition diagLeft;
        ChessPosition diagRight;
        if (teamColor == TeamColor.BLACK) {
            diagLeft = new ChessPosition(kingPosition.getRow()-1, kingPosition.getColumn()-1);
            diagRight = new ChessPosition(kingPosition.getRow()-1, kingPosition.getColumn()+1);
        }
        else {
            diagLeft = new ChessPosition(kingPosition.getRow()+1, kingPosition.getColumn()-1);
            diagRight = new ChessPosition(kingPosition.getRow()+1, kingPosition.getColumn()+1);
        }
        if (board.onBoard(diagLeft) && !board.emptySpaceOnBoard(diagLeft)) {
            if (board.getPiece(diagLeft).getPieceType() == ChessPiece.PieceType.PAWN &&
                    board.getPiece(diagLeft).getTeamColor() != teamColor) {
                return true;
            }
        }
        if (board.onBoard(diagRight) && !board.emptySpaceOnBoard(diagRight)) {
            if (board.getPiece(diagRight).getPieceType() == ChessPiece.PieceType.PAWN &&
                    board.getPiece(diagRight).getTeamColor() != teamColor) {
                return true;
            }
        }

        // Imagine if there was a piece of pieceType at the kings position,
        // if they could attack a piece of the opponent of the same type then the king is inCheck
        //Knight
        ChessPiece kingKnight = new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT);
        Collection<ChessMove> knightMoves = kingKnight.pieceMoves(board, kingPosition);
        for (ChessMove move: knightMoves) {
            if (!board.emptySpaceOnBoard(move.getEndPosition()) &&
                    board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KNIGHT) {
                return true;
            }
        }


        //Rook
        ChessPiece kingRook = new ChessPiece(teamColor, ChessPiece.PieceType.ROOK);
        Collection<ChessMove> rookMoves = kingRook.pieceMoves(board,kingPosition);
        for (ChessMove move: rookMoves) {
            if (!board.emptySpaceOnBoard(move.getEndPosition()) &&
                    (board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.ROOK ||
                            board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.QUEEN)) {
                return true;
            }
        }

        //Bishop
        ChessPiece kingBishop = new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP);
        Collection<ChessMove> bishopMoves = kingBishop.pieceMoves(board,kingPosition);
        for (ChessMove move: bishopMoves) {
            if (!board.emptySpaceOnBoard(move.getEndPosition()) &&
                    (board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.BISHOP ||
                            board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.QUEEN)) {
                return true;
            }
        }

        //King
        ChessPiece king = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        Collection<ChessMove> kingMoves = king.pieceMoves(board,kingPosition);
        for (ChessMove move: kingMoves) {
            if (!board.emptySpaceOnBoard(move.getEndPosition()) &&
                    board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING) {
                return true;
            }
        }

        return false;
    }

    public ChessPosition findKing(TeamColor teamColor) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                if (piece != null &&
                        piece.getPieceType() == ChessPiece.PieceType.KING &&
                        piece.getTeamColor() == teamColor) {
                    return pos;
                }
            }
        }
        return null;
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

    private boolean noValidMoves(TeamColor teamColor) {
        ArrayList<ChessPosition> allyPositions = allyPositions(teamColor);
        for (ChessPosition ally: allyPositions) {
            Collection<ChessMove> moves = validMoves(ally);
            if (!moves.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private ArrayList<ChessPosition> allyPositions(TeamColor teamColor) {
        ArrayList<ChessPosition> allyPositions = new ArrayList<>();
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                if (allyAtPosition(pos, teamColor)) {
                    allyPositions.add(pos);
                }
            }
        }
        return allyPositions;
    }

    private boolean allyAtPosition(ChessPosition pos, TeamColor teamColor) {
        return !board.emptySpaceOnBoard(pos) &&
                board.getPiece(pos).getTeamColor() == teamColor;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return turn == chessGame.turn && Objects.equals(board, chessGame.board) && Objects.equals(movesMade, chessGame.movesMade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turn, board, movesMade);
    }
}
