package dataaccess.interfaces;

import dataaccess.DataAccessException;
import model.AuthData;

public interface AuthDAO {
    public AuthData createAuth(String username) throws DataAccessException; // returns
    public AuthData getAuth(String authToken) throws DataAccessException;
    public void deleteAuth(String authToken) throws DataAccessException;
    public void clear() throws DataAccessException;
}
