package model;
import java.util.UUID;

public record AuthData(String authToken, String username) {

    public static AuthData createWithRandomToken(String username) {
        return new AuthData(UUID.randomUUID().toString(), username);
    }
}
