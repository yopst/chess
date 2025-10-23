package server.service;

import dataaccess.DataAccessException;
import dataaccess.MyDatabaseManager;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.UserDAO;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import request.LoginRequest;
import response.LoginResponse;
import server.exception.BadRequestException;
import server.exception.EndpointException;
import server.exception.UnauthorizedException;

public class LoginService {
    private final AuthDAO auth;
    private final UserDAO users;

    public LoginService() {
        MyDatabaseManager dbManager = MyDatabaseManager.getInstance();
        auth = dbManager.getAuth();
        users = dbManager.getUsers();
    }

    public LoginResponse login(LoginRequest loginRequest) throws EndpointException {
        try {
            if (loginRequest.username() == null ||
                    loginRequest.password() == null) {
                throw new BadRequestException("bad request");
            }
            UserData userData = users.getUser(loginRequest.username());
            if (userData == null || this.passwordUnverified(loginRequest.password(), userData.password())) {
                throw new UnauthorizedException("unauthorized");
            }
            AuthData authData = auth.createAuth(userData.username());
            return new LoginResponse(userData.username(), authData.authToken());
        }
        catch (DataAccessException e) {
            throw new EndpointException(e.getMessage(), 404);
        }
    }
    private boolean passwordUnverified(String clearTextPassword, String hashedPassword) {
        return !BCrypt.checkpw(clearTextPassword, hashedPassword);
    }
}
