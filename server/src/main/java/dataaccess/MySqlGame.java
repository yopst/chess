package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.interfaces.GameDAO;
import model.GameData;
import model.GameDataListItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;


public class MySqlGame extends BaseSqlDao implements GameDAO {
    private final Gson gson = new Gson();
    private int numGames;

    public MySqlGame() throws DataAccessException {
        createGameTable();
        numGames = 0;
    }

    private void createGameTable() throws DataAccessException {
        String sql = "CREATE TABLE IF NOT EXISTS games ("
                + "game_id INT AUTO_INCREMENT PRIMARY KEY, "
                + "game_name VARCHAR(255) NOT NULL, "
                + "game_state LONGTEXT NOT NULL, "
                + "white_username VARCHAR(255), "
                + "black_username VARCHAR(255)"
                + ")";
        executeUpdate(sql);
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        if (gameName == null) {
            throw new DataAccessException("no name supplied");
        }
        int gameID = numGames + 1;
        ChessGame game = new ChessGame();
        String gameState = gson.toJson(game);

        String sql = "INSERT INTO games (game_name, game_state) VALUES (?, ?)";
        executeUpdate(sql, gameName, gameState);

        numGames++;
        return gameID;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        String sql = "SELECT game_name, game_state, black_username, white_username FROM games WHERE game_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(gameID));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String gameName = rs.getString("game_name");
                    String gameState = rs.getString("game_state");
                    String blackUser = rs.getString("black_username");
                    String whiteUser = rs.getString("white_username");

                    ChessGame game = gson.fromJson(gameState, ChessGame.class);

                    return new GameData(gameID, whiteUser, blackUser, gameName, game);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving game data: " + e.getMessage());
        }
    }

    @Override
    public void updateGame(int gameID, String username, ChessGame.TeamColor playerColor) throws DataAccessException {
        GameData currentGameData = getGame(gameID);
        if (currentGameData == null) {
            throw new DataAccessException("no such game to update");
        }
        String sql;
        if (playerColor == ChessGame.TeamColor.WHITE) {
            sql = "UPDATE games SET white_username = ? WHERE game_id = ?";
        }
        else {
            sql = "UPDATE games SET black_username = ? WHERE game_id = ?";

        }

        executeUpdate(sql, username, Integer.toString(gameID));
    }

    @Override
    public Collection<GameDataListItem> listGames() {
        ArrayList<GameDataListItem> list = new ArrayList<>();
        String sql = "SELECT game_id, game_name, white_username, black_username FROM games";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int gameID = rs.getInt("game_id");
                String whiteUser = rs.getString("white_username");
                String blackUser = rs.getString("black_username");
                String gameName = rs.getString("game_name");

                list.add(new GameDataListItem(gameID, whiteUser, blackUser, gameName));
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException("Error listing games: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "TRUNCATE TABLE games";
        executeUpdate(sql);
        numGames = 0;
    }
}
