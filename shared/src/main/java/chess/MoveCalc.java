package chess;

import java.util.Collection;

public interface MoveCalc {
    public Collection<ChessMove> calcMoves(ChessBoard board, ChessPosition position);
}
