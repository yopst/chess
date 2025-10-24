package chess.calculators;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PawnCalc extends MoveCalc {

    @Override
    public Collection<ChessMove> calcMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        ArrayList<ChessPosition> positions = new ArrayList<>();
        //System.out.print(board.toString());

        //VERTICAL ONE
        if (color == ChessGame.TeamColor.WHITE) {
            ChessPosition vertOne = new ChessPosition(position.getRow() + 1, position.getColumn());
            positions.add(vertOne);
        } else { // Black
            ChessPosition vertOne = new ChessPosition(position.getRow() - 1, position.getColumn());
            positions.add(vertOne);
        }

        for (ChessPosition pos : positions) {
            if (board.emptySpaceOnBoard(pos)) {
                if (pos.getRow() != 1 && pos.getRow() != 8) { //not edge of board
                    validMoves.add(new ChessMove(position, pos));
                }
                //pawn has reached other side of board
                else if (reachedEdge(pos, color)) {
                    addPromotionMoves(validMoves, position, pos);
                }
            }
        }
        positions = new ArrayList<>();

        //STARTING VERTICAL TWO
        if (((position.getRow() == 2 && color == ChessGame.TeamColor.WHITE)
                || (position.getRow() == 7 && color == ChessGame.TeamColor.BLACK)) //at starting positions for respective color
                && !validMoves.isEmpty()) { //not blocked on vertOne
            ChessPosition vertTwo;
            if (color == ChessGame.TeamColor.WHITE) {
                vertTwo = new ChessPosition(4, position.getColumn());
            } else {
                vertTwo = new ChessPosition(5, position.getColumn());
            }
            if (board.emptySpaceOnBoard(vertTwo)) { //not blocked on vertTwo
                validMoves.add(new ChessMove(position, vertTwo));
            }
        }

        //SIMPLE ATTACK
        ChessPosition diagLeftW = new ChessPosition(position.getRow() + 1, position.getColumn() - 1);
        ChessPosition diagRightW = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);

        ChessPosition diagLeftB = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
        ChessPosition diagRightB = new ChessPosition(position.getRow() - 1, position.getColumn() + 1);

        if (color == ChessGame.TeamColor.WHITE) {
            positions.add(diagLeftW);
            positions.add(diagRightW);

        } else {
            positions.add(diagLeftB);
            positions.add(diagRightB);
        }

        for (ChessPosition pos : positions) {
            if (!board.emptySpaceOnBoard(pos)
                    && board.onBoard(pos)
                    && color != board.getPiece(pos).getTeamColor()) { //must be an attack on opponent
                //pawn has reached other side of board
                if (reachedEdge(pos, color)) {
                    addPromotionMoves(validMoves, position, pos);
                } else {
                    validMoves.add(new ChessMove(position, pos));
                }
            }
        }

        return validMoves;
    }


    private void addPromotionMoves(ArrayList<ChessMove> validMoves, ChessPosition start, ChessPosition end) {
        validMoves.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
        validMoves.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
        validMoves.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
        validMoves.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));
    }

    private boolean reachedEdge(ChessPosition pos, ChessGame.TeamColor color) {
        return ((pos.getRow() == 1 && color == ChessGame.TeamColor.BLACK)
                || (pos.getRow() == 8 && color == ChessGame.TeamColor.WHITE));

    }
}





