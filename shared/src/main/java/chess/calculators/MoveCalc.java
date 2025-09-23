package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public interface MoveCalc {
    public Collection<ChessMove> calcMoves(ChessBoard board, ChessPosition position);
}
