package dataaccess.interfaces;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    public int createGame(String gameName) throws DataAccessException; //returns gameID
    public GameData getGame(int gameID) throws DataAccessException;
    public void updateGame(int gameID, String username, ChessGame.TeamColor playerColor) throws DataAccessException;
    public Collection<model.GameDataListItem> listGames();
    public void clear() throws DataAccessException;
}
