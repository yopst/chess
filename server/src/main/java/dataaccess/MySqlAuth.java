package dataaccess;

import dataaccess.interfaces.AuthDAO;
import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlAuth extends BaseSqlDao implements AuthDAO {

    public MySqlAuth() throws DataAccessException {
        createAuthTable();
    }

    private void createAuthTable() throws DataAccessException {
        String sql = "CREATE TABLE IF NOT EXISTS auth_tokens ("
                + "token VARCHAR(255) PRIMARY KEY, "
                + "username VARCHAR(255) NOT NULL)";
        executeUpdate(sql);
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        if (username == null) {
            throw new DataAccessException("no authToken supplied");
        }

        AuthData authData = AuthData.createWithRandomToken(username);
        String sql = "INSERT INTO auth_tokens (token, username) VALUES (?, ?)";
        executeUpdate(sql, authData.authToken(), username);

        return authData;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        if (authToken == null) {
            throw new DataAccessException("no authToken supplied");
        }

        String sql = "SELECT token, username FROM auth_tokens WHERE token = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String token = rs.getString("token");
                    String username = rs.getString("username");
                    return new AuthData(token, username);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving auth data: " + e.getMessage());
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        if (getAuth(authToken) == null) {
            throw new DataAccessException("no such authData to remove");
        }

        String sql = "DELETE FROM auth_tokens WHERE token = ?";
        executeUpdate(sql, authToken);
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "TRUNCATE TABLE auth_tokens";
        executeUpdate(sql);
    }

}
