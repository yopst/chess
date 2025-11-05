package dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class BaseSqlDao {
    protected void executeUpdate(String sql, String... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setString(i + 1, params[i]);
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error executing update SQL: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new DataAccessException("Error establishing connection to SQL db: " + e.getMessage());
        }
    }
}
