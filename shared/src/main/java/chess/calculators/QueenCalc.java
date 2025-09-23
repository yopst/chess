package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class QueenCalc implements MoveCalc {
    @Override
    public Collection<ChessMove> calcMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        Collection<ChessMove> rookMoves =  new RookCalc().calcMoves(board,position);
        Collection<ChessMove> bishopMoves = new BishopCalc().calcMoves(board,position);
        validMoves.addAll(rookMoves);
        validMoves.addAll(bishopMoves);

        return validMoves;
    }
}
