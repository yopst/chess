package chess.calculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class BishopCalc implements MoveCalc {
    @Override
    public Collection<ChessMove> calcMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();

        //DIAG UP LEFT
        ChessPosition current = new ChessPosition(position.getRow(), position.getColumn());
        while(true) {
            ChessPosition diag = new ChessPosition(current.getRow() + 1, current.getColumn() - 1);
            if (board.emptySpaceOnBoard(diag)) {
                validMoves.add(new ChessMove(position, diag));
                current = diag;
            }
            //attack
            else if (board.onBoard(diag) &&
                    board.getPiece(diag).getTeamColor() != color) {
                validMoves.add(new ChessMove(position, diag));
                break;
            }
            else {
                break;
            }
        }
        //DIAG UP RIGHT
        current = new ChessPosition(position.getRow(), position.getColumn());
        while(true) {
            ChessPosition diag = new ChessPosition(current.getRow() + 1, current.getColumn() + 1);
            if (board.emptySpaceOnBoard(diag)) {
                validMoves.add(new ChessMove(position, diag));
                current = diag;
            }
            //attack
            else if (board.onBoard(diag) &&
                    board.getPiece(diag).getTeamColor() != color) {
                validMoves.add(new ChessMove(position, diag));
                break;
            }
            else {
                break;
            }
        }
        //DIAG DOWN LEFT
        current = new ChessPosition(position.getRow(), position.getColumn());
        while(true) {
            ChessPosition diag = new ChessPosition(current.getRow() - 1, current.getColumn() - 1);
            if (board.emptySpaceOnBoard(diag)) {
                validMoves.add(new ChessMove(position, diag));
                current = diag;
            }
            //attack
            else if (board.onBoard(diag) &&
                    board.getPiece(diag).getTeamColor() != color) {
                validMoves.add(new ChessMove(position, diag));
                break;
            }
            else {
                break;
            }
        }
        //DIAG DOWN RIGHT
        current = new ChessPosition(position.getRow(), position.getColumn());
        while(true) {
            ChessPosition diag = new ChessPosition(current.getRow() - 1, current.getColumn() + 1);
            if (board.emptySpaceOnBoard(diag)) {
                validMoves.add(new ChessMove(position, diag));
                current = diag;
            }
            //attack
            else if (board.onBoard(diag) &&
                    board.getPiece(diag).getTeamColor() != color) {
                validMoves.add(new ChessMove(position, diag));
                break;
            }
            else {
                break;
            }
        }

        return validMoves;
    }
}
