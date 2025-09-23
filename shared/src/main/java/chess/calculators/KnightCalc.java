package chess.calculators;


import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.ArrayList;

public class KnightCalc implements MoveCalc {

    @Override
    public Collection<ChessMove> calcMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        ArrayList<ChessPosition> positions = new ArrayList<>();

        //UP LEFT
        ChessPosition shortL = new ChessPosition(position.getRow() + 1, position.getColumn() - 2);
        ChessPosition tallL = new ChessPosition(position.getRow() + 2, position.getColumn() - 1);
        positions.add(shortL);
        positions.add(tallL);

        //UP RIGHT
        shortL = new ChessPosition(position.getRow() + 1, position.getColumn() + 2);
        tallL = new ChessPosition(position.getRow() + 2, position.getColumn() + 1);
        positions.add(shortL);
        positions.add(tallL);

        //DOWN LEFT
        shortL = new ChessPosition(position.getRow() - 1, position.getColumn() - 2);
        tallL = new ChessPosition(position.getRow() - 2, position.getColumn() - 1);
        positions.add(shortL);
        positions.add(tallL);

        //DOWN RIGHT
        shortL = new ChessPosition(position.getRow() - 1, position.getColumn() + 2);
        tallL = new ChessPosition(position.getRow() - 2, position.getColumn() + 1);
        positions.add(shortL);
        positions.add(tallL);

        for (ChessPosition pos: positions) {
            if (board.emptySpaceOnBoard(pos)) {
                validMoves.add(new ChessMove(position, pos));
            }
            else if (board.onBoard(pos) && board.getPiece(pos).getTeamColor() != color) {
                validMoves.add(new ChessMove(position,pos));
            }
        }

        return validMoves;
    }
}
