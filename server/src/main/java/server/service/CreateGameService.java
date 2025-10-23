package server.service;

import dataaccess.DataAccessException;
import dataaccess.MyDatabaseManager;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import request.CreateGameRequest;
import response.CreateGameResponse;
import server.exception.BadRequestException;
import server.exception.EndpointException;
import server.exception.UnauthorizedException;


public class CreateGameService {
    private final GameDAO gamesDB;
    private final AuthDAO auth;

    public CreateGameService() {
        MyDatabaseManager dbManager = MyDatabaseManager.getInstance();
        gamesDB = dbManager.getGames();
        auth = dbManager.getAuth();
    }

    public CreateGameResponse createGame(CreateGameRequest createGameRequest, String authToken) throws EndpointException {
        try {
            if (auth.getAuth(authToken) == null) {
                throw new UnauthorizedException("unauthorized");
            }
            if (createGameRequest.gameName() == null) {
                throw new BadRequestException("bad request");
            }
            //Allows multiple games of the same name.
            return new CreateGameResponse(gamesDB.createGame(createGameRequest.gameName()));
        }
        catch (DataAccessException e) {
            throw new EndpointException(e.getMessage(), 404);
        }
    }


}