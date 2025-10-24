package chess.calculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public abstract class MoveCalc {
    public abstract Collection<ChessMove> calcMoves(ChessBoard board, ChessPosition position);
    protected void addValidMoves(ArrayList<ChessMove> validMoves, ArrayList<ChessPosition> positions,
                                 ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        for (ChessPosition pos: positions) {
            if (board.emptySpaceOnBoard(pos)) {
                validMoves.add(new ChessMove(position, pos));
            }
            else if (board.onBoard(pos) && board.getPiece(pos).getTeamColor() != color) {
                validMoves.add(new ChessMove(position,pos));
            }
        }
    }

}
