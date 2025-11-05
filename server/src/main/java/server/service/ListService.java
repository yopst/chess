package server.service;

import dataaccess.DataAccessException;
import dataaccess.MyDatabaseManager;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import response.ListResponse;
import server.exception.EndpointException;
import server.exception.UnauthorizedException;

import java.util.Collection;

public class ListService {
    private final GameDAO gamesDB;
    private final AuthDAO auth;

    public ListService() {
        MyDatabaseManager dbManager = MyDatabaseManager.getInstance();
        gamesDB = dbManager.getGames();
        auth = dbManager.getAuth();
    }
    public ListResponse listGames(String authToken) throws EndpointException {
        try {
            if (auth.getAuth(authToken) == null) {
                throw new UnauthorizedException("unauthorized");
            }
            Collection<model.GameDataListItem> games = gamesDB.listGames();
            return new ListResponse(games);
        }
        catch (DataAccessException e) {
            throw new EndpointException(e.getMessage(), 500);
        }
    }
}
