package dataaccess;

import chess.ChessGame;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import dataaccess.interfaces.UserDAO;
import model.AuthData;
import model.GameData;
import model.GameDataListItem;
import model.UserData;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.List;

public class DAOTests {
    static AuthDAO auths;
    static GameDAO games;
    static UserDAO users;

    private static String validAuth;
    private static Integer validGameID;
    private static final String VALID_USERNAME = "valid_user";
    private static final String VALID_PASSWORD = "valid_password";
    private static final String VALID_GAME_NAME = "valid_game_name";



    @FunctionalInterface
    public interface DAOFunction<R, T> {
        T apply(R request) throws DataAccessException;
    }

    private static <T, R> T exceptionWrapper(R input, DAOFunction<R, T> function) {
        try {
            return function.apply(input);
        } catch (DataAccessException e) {
            Assertions.fail("DAO operation failed with exception: " + e.getMessage());
            return null;
        }
    }

    @BeforeAll
    public static void getDB() {
        MyDatabaseManager db = MyDatabaseManager.getInstance();
        auths = db.getAuth();
        games = db.getGames();
        users = db.getUsers();
    }

    @BeforeEach
    public void init() {
        try {
            games.clear();
            users.clear();
            auths.clear();

            validAuth = auths.createAuth(VALID_USERNAME).authToken();
            users.createUser(new UserData(VALID_USERNAME,VALID_PASSWORD,""));
            validGameID = games.createGame(VALID_GAME_NAME);

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    //positive clears
    @Test
    @DisplayName("Clear Auth")
    public void clearAuth(){
        Assertions.assertDoesNotThrow(() -> auths.clear());
    }

    @Test
    @DisplayName("Clear User")
    public void clearUser(){
        Assertions.assertDoesNotThrow(() -> users.clear());
    }

    @Test
    @DisplayName("Clear Game")
    public void clearGame(){
        Assertions.assertDoesNotThrow(() -> games.clear());
    }

    //AuthDAO
    @Test
    @DisplayName("Delete Auth")
    public void deleteAuth(){
        Assertions.assertDoesNotThrow(() -> auths.deleteAuth(validAuth));
    }

    @Test
    @DisplayName("Negative Delete Auth")
    public void negativeDeleteAuth(){
        Assertions.assertThrows(DataAccessException.class, () -> auths.deleteAuth("invalid_auth"));
        Assertions.assertThrows(DataAccessException.class, () -> auths.deleteAuth(""));
    }

    @Test
    @DisplayName("Create Auth")
    public void createAuth(){
        AuthData authData = exceptionWrapper(VALID_USERNAME, auths::createAuth);
        AuthData authData1 = exceptionWrapper(VALID_USERNAME, auths::createAuth);
        Assertions.assertNotNull(authData);
        Assertions.assertNotNull(authData1);
        Assertions.assertEquals(authData1.username(), authData.username());
        Assertions.assertNotEquals(authData1.authToken(), authData.authToken());
    }

    @Test
    @DisplayName("Negative Create Auth")
    public void negativeCreateAuth(){
        Assertions.assertThrows(DataAccessException.class, () -> auths.createAuth(null));
    }

    @Test
    @DisplayName("Get Auth")
    public void getAuth(){
        AuthData authData = exceptionWrapper(validAuth, auths::getAuth);
        Assertions.assertNotNull(authData);
    }

    @Test
    @DisplayName("Negative Get Auth")
    public void negativeGetAuth(){
        Assertions.assertThrows(DataAccessException.class, () -> auths.getAuth(null));

        AuthData authData = exceptionWrapper("invalidAuth", auths::getAuth);
        Assertions.assertNull(authData);
    }

    //UserDAO
    @Test
    @DisplayName("Get User")
    public void getUser() {
        UserData userData = exceptionWrapper(VALID_USERNAME, users::getUser);
        Assertions.assertNotNull(userData);
        Assertions.assertEquals(VALID_USERNAME, userData.username());
    }

    @Test
    @DisplayName("Negative Get User")
    public void negativeGetUser(){
        Assertions.assertThrows(DataAccessException.class, () -> users.getUser(null));

        UserData userData = exceptionWrapper("new_user", users::getUser);
        Assertions.assertNull(userData);
    }

    @Test
    @DisplayName("Create User")
    public void createUser() {
        UserData newUser = new UserData("valid", "valid", "valid");
        Assertions.assertDoesNotThrow(() -> users.createUser(newUser));
    }

    @Test
    @DisplayName("Negative Create User")
    public void negativeCreateUser(){
        Assertions.assertThrows(DataAccessException.class, () -> users.createUser(null));

        UserData invalidUser = new UserData(null, VALID_PASSWORD, "");
        Assertions.assertThrows(DataAccessException.class, () -> users.createUser(invalidUser));

        UserData invalidUser1 = new UserData(VALID_USERNAME, null, "");
        Assertions.assertThrows(DataAccessException.class, () -> users.createUser(invalidUser1));
    }

    //GameDAO
    @Test
    @DisplayName("Create Game")
    public void createGame() {
        Integer gameID = exceptionWrapper("valid_game_name", games::createGame);
        Assertions.assertNotNull(gameID);
        Integer gameID1 = exceptionWrapper("valid_game_name", games::createGame);
        Assertions.assertNotNull(gameID1);
        Assertions.assertEquals(gameID, gameID1 - 1);
    }

    @Test
    @DisplayName("Negative Create Game")
    public void negativeCreateGame(){
        Assertions.assertThrows(DataAccessException.class, () -> games.createGame(null));
    }

    @Test
    @DisplayName("Get Game")
    public void getGame() {
        GameData gameData = exceptionWrapper(validGameID, games::getGame);
        Assertions.assertNotNull(gameData);
        Assertions.assertEquals(1,gameData.gameID());
    }

    @Test
    @DisplayName("Negative Get Game")
    public void negativeGetGame() {
        GameData gameData = exceptionWrapper(42, games::getGame);
        Assertions.assertNull(gameData);
    }

    @Test
    @DisplayName("Update Game")
    public void updateGame() {
        Assertions.assertDoesNotThrow(() ->
                games.updateGame(1, VALID_USERNAME, ChessGame.TeamColor.BLACK));
        Assertions.assertDoesNotThrow(() ->
                games.updateGame(1, VALID_USERNAME, ChessGame.TeamColor.WHITE));
    }

    @Test
    @DisplayName("Negative Update Game")
    public void negativeUpdateGame() {
        Assertions.assertThrows(DataAccessException.class,
                () -> games.updateGame(42, VALID_USERNAME, ChessGame.TeamColor.WHITE));
    }

    @Test
    @DisplayName("List Games")
    public void listGames() {
        Collection<GameDataListItem> list = games.listGames();
        GameDataListItem item = new GameDataListItem(1,null,null, VALID_GAME_NAME);
        Collection<GameDataListItem> expectedList = List.of(item);
        Assertions.assertEquals(expectedList.contains(item),list.contains(item));
    }


}
