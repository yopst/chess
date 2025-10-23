package dataaccess.memory;

import dataaccess.DataAccessException;
import dataaccess.interfaces.AuthDAO;
import model.AuthData;

import java.util.HashMap;

public class MemoryAuth implements AuthDAO {
    private static final HashMap<String, AuthData> AUTHS = new HashMap<>();

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        if (username == null) {
            throw new DataAccessException("no username supplied");
        }
        AuthData authData = AuthData.createWithRandomToken(username);
        AUTHS.put(authData.authToken(), authData);
        return authData;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        if (authToken == null) {
            throw new DataAccessException("no authToken supplied");
        }
        return AUTHS.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        if (getAuth(authToken) == null) {
            throw new DataAccessException("no such authData to remove");
        }
        AUTHS.remove(authToken);
    }

    @Override
    public void clear() throws DataAccessException {
        AUTHS.clear();
    }
}
