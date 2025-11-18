package repl;

import chess.ChessBoard;
import chess.ChessGame;
import client.Client;
import client.State;
import ui.ChessBoardUI;
import websocket.NotificationHandler;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

import static ui.EscapeSequences.SET_BG_COLOR_RED;

public class GameRepl extends Repl implements NotificationHandler {
    private final Client client;
    public final String visitorUsername;
    public final Integer gameID;
    private final ChessGame.TeamColor color;
    private ChessBoard currentBoard;

    private boolean boardHastBeenSet;

    public GameRepl(Client client, String visitorUsername, Integer gameID, ChessGame.TeamColor color) {
        this.client = client;
        this.gameID = gameID;
        this.visitorUsername = visitorUsername;
        this.color = color;
        this.boardHastBeenSet = true;

        if (client.state == State.GAMING) {
            String strColor = (color == ChessGame.TeamColor.WHITE) ? "White": "Black";
            System.out.println("You are playing as " + strColor);
        }
        else {
            System.out.println("Observing game number " + gameID);
        }
    }

    @Override
    public String help() {
        if (client.state == State.GAMING) {
            return """
                - highlight
                - resign
                - move
                - leave
                - redraw
                - help
                """;
        }
        else {
            return """
                - leave
                - redraw
                - help
                """;
        }
    }

    @Override
    public void notify(ServerMessage notification) {
        System.out.println("Current Board:");
        printCurrentBoard();
        System.out.println(SET_BG_COLOR_RED + notification.toString());
    }

    public void notify(LoadGameMessage loadGameMessage) {
        ChessGame chessGame = loadGameMessage.deserializeChessGame();
        currentBoard = chessGame.getBoard();
        boardHastBeenSet = false;
    }

    private void printInitBoard() {
        System.out.print(new ChessBoardUI().createChessBoard(color, client.blankInitializedBoard));
    }

    public void printCurrentBoard() {
        if (boardHastBeenSet) {
            printInitBoard();
        }
        else {
            System.out.println(new ChessBoardUI().createChessBoard(color, currentBoard));
        }
    }
}
