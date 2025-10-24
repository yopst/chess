package chess.calculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KingCalc extends MoveCalc {
    @Override
    public Collection<ChessMove> calcMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        ArrayList<ChessPosition> positions = new ArrayList<>();

        //UP AND DOWN
        ChessPosition up = new ChessPosition(position.getRow() + 1, position.getColumn());
        ChessPosition down = new ChessPosition(position.getRow() - 1, position.getColumn());
        positions.add(up);
        positions.add(down);

        //LEFT AND RIGHT
        ChessPosition left = new ChessPosition(position.getRow(), position.getColumn() - 1);
        ChessPosition right = new ChessPosition(position.getRow(), position.getColumn() + 1);
        positions.add(left);
        positions.add(right);

        //UP DIAGS
        left = new ChessPosition(position.getRow() + 1, position.getColumn() - 1);
        right = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
        positions.add(left);
        positions.add(right);

        //DOWN DIAGS
        left = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
        right = new ChessPosition(position.getRow() - 1, position.getColumn() + 1);
        positions.add(left);
        positions.add(right);

        addValidMoves(validMoves, positions, board, position, color);

        return validMoves;
    }
}
