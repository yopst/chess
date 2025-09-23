package chess.calculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class RookCalc implements MoveCalc {

    @Override
    public Collection<ChessMove> calcMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();

        //Vertical UP
        ChessPosition current = new ChessPosition(position.getRow(), position.getColumn());
        while(true) {
            ChessPosition up = new ChessPosition(current.getRow() + 1, current.getColumn());
            if (board.emptySpaceOnBoard(up)) {
                validMoves.add(new ChessMove(position, up));
                current = up;
            }
            //attack
            else if (board.onBoard(up) &&
                    board.getPiece(up).getTeamColor() != color) {
                validMoves.add(new ChessMove(position, up));
                break;
            }
            else {
                break;
            }
        }
        //Vertical down
        current = new ChessPosition(position.getRow(), position.getColumn());
        while(true) {
            ChessPosition down = new ChessPosition(current.getRow() - 1, current.getColumn());
            if (board.emptySpaceOnBoard(down)) {
                validMoves.add(new ChessMove(position, down));
                current = down;
            }
            //attack
            else if (board.onBoard(down) &&
                    board.getPiece(down).getTeamColor() != color) {
                validMoves.add(new ChessMove(position, down));
                break;
            }
            else {
                break;
            }
        }
        //LEFT
        current = new ChessPosition(position.getRow(), position.getColumn());
        while(true) {
            ChessPosition left = new ChessPosition(current.getRow(), current.getColumn() - 1);
            if (board.emptySpaceOnBoard(left)) {
                validMoves.add(new ChessMove(position, left));
                current = left;
            }
            //attack
            else if (board.onBoard(left) &&
                    board.getPiece(left).getTeamColor() != color) {
                validMoves.add(new ChessMove(position, left));
                break;
            }
            else {
                break;
            }
        }
        //RIGHT
        current = new ChessPosition(position.getRow(), position.getColumn());
        while(true) {
            ChessPosition right = new ChessPosition(current.getRow(), current.getColumn() + 1);
            if (board.emptySpaceOnBoard(right)) {
                validMoves.add(new ChessMove(position, right));
                current = right;
            }
            //attack
            else if (board.onBoard(right) &&
                    board.getPiece(right).getTeamColor() != color) {
                validMoves.add(new ChessMove(position, right));
                break;
            }
            else {
                break;
            }
        }

        return validMoves;
    }
}
