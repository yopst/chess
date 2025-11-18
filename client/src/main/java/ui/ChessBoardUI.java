package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;

public class ChessBoardUI {

    public String createChessBoard(ChessGame.TeamColor teamColor, ChessBoard chessBoard) {
        boolean whitePerspective = isWhitePerspective(teamColor);
        StringBuilder board = new StringBuilder();

        // Reset sequence to clear all styles
        final String resetStyles = EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.RESET_BG_COLOR;
        board.append(resetStyles);

        // Column labels
        final String columnsWhite = "    a  b  c  d  e  f  g  h\n";
        final String columnsBlack = "    h  g  f  e  d  c  b  a\n";

        // Add column labels at the top (adjust based on perspective)
        board.append(whitePerspective ? columnsWhite : columnsBlack);

        // Determine row order based on perspective
        int start = whitePerspective ? 8 : 1;
        int end = whitePerspective ? 0 : 9;
        int step = whitePerspective ? -1 : 1;

        // Generate the board row by row
        for (int row = start; row != end; row += step) {

            // Add row label at the end of the line
            board.append(" ").append(row).append(" ");

            for (int col = 1; col <= 8; col += 1) {
                boolean isLightTile = whitePerspective == ((row + col) % 2 != 0);
                int adjustedColumn = whitePerspective ? col: 9 - col;

                // Choose background color based on tile
                String bgColor = isLightTile ?
                        EscapeSequences.SET_BG_COLOR_LIGHT_GREY:
                        EscapeSequences.SET_BG_COLOR_DARK_GREY;

                // Add piece and reset styling
                board.append(bgColor)
                        .append(' ')  // Padding
                        .append(positionToChessPieceString(chessBoard,new ChessPosition(row, adjustedColumn)))  // Middle character
                        .append(' ')  // Padding
                        .append(resetStyles);  // Reset to default colors
            }

            // Add row label at the end of the line
            board.append(" ").append(row).append("\n");
        }

        // Add column labels at the bottom (adjust based on perspective)
        board.append(whitePerspective ? columnsWhite : columnsBlack);

        return board.toString();
    }

    private boolean isWhitePerspective(ChessGame.TeamColor teamColor) {
        return teamColor == ChessGame.TeamColor.WHITE;
    }

    private String positionToChessPieceString(ChessBoard board,ChessPosition pos) {
        return (board.getPiece(pos) != null) ? board.getPiece(pos).toString(): " ";
    }
}
