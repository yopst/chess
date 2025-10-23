package service;

import chess.ChessGame;
import model.GameDataListItem;
import org.junit.jupiter.api.*;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.ListResponse;
import response.RegisterResponse;
import server.exception.EndpointException;
import server.service.*;

import java.util.ArrayList;
import java.util.Collection;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceClassTests {
    private static ClearService clearService;
    private static CreateGameService createGameService;
    private static JoinGameService joinGameService;
    private static ListService listService;
    private static LoginService loginService;
    private static LogoutService logoutService;
    private static RegisterService registerService;

    private static String authToken;
    private static String authToken2;
    private static final String BAD_AUTH = "evil Auth";
    private static final String FIRST_USERNAME = "first_user";
    private static final String SECOND_USERNAME = "second_user";
    private static final String FIRST_GAME_NAME = "first game";
    private static final String SHARED_VALID_PASSWORD = "password";




    @BeforeAll
    public static void init() {
        clearService = new ClearService();
        createGameService = new CreateGameService();
        joinGameService = new JoinGameService();
        listService = new ListService();
        loginService = new LoginService();
        logoutService = new LogoutService();
        registerService = new RegisterService();

        clearDB();
    }

    @AfterAll
    static void clearDB() {
        try {
            clearService.clear();
        } catch (EndpointException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(1)
    @DisplayName("Clear Service")
    public void clearDatabaseTest() throws Exception {
        Assertions.assertDoesNotThrow(() -> clearService.clear());
    }

    @Test
    @Order(2)
    @DisplayName("Register Service")
    public void positiveRegisterTest() throws Exception {
        RegisterRequest positiveRequest =
                new RegisterRequest(FIRST_USERNAME, SHARED_VALID_PASSWORD, "fu@email.com");
        RegisterRequest positiveRequest2 =
                new RegisterRequest(SECOND_USERNAME, SHARED_VALID_PASSWORD, "fu@email.com");

        RegisterResponse actualResponse =
                Assertions.assertDoesNotThrow(() -> registerService.register(positiveRequest));
        authToken = actualResponse.authToken();
        authToken2 = Assertions.assertDoesNotThrow(() -> registerService.register(positiveRequest2).authToken());

        Assertions.assertNotEquals(authToken, authToken2);
    }
    @Test
    @Order(2)
    @DisplayName("Negative Register Service")
    public void negativeRegisterTest() throws Exception {
        RegisterRequest negativeRequest =
                new RegisterRequest(SECOND_USERNAME,"new_password", "su@email.com");
        Assertions.assertThrows(EndpointException.class, () -> registerService.register(negativeRequest));
    }

    @Test
    @Order(3)
    @DisplayName("Create Service")
    public void createTest() throws Exception {
        CreateGameRequest positiveRequest = new CreateGameRequest(FIRST_GAME_NAME);

        Assertions.assertEquals(1, createGameService.createGame(positiveRequest,authToken).gameID());

        //can have multiple games of the same name
        int gameID = Assertions.assertDoesNotThrow(() ->
                createGameService.createGame(positiveRequest,authToken).gameID());
        Assertions.assertNotEquals(1,gameID);

        clearDB();
        positiveRegisterTest();
        Assertions.assertEquals(1, createGameService.createGame(positiveRequest,authToken).gameID());
    }

    @Test
    @Order(3)
    @DisplayName("Negative Create Service")
    public void negativeCreateTest() throws Exception {
        CreateGameRequest positiveRequest = new CreateGameRequest(FIRST_GAME_NAME);
        Assertions.assertThrows(EndpointException.class, () -> createGameService.createGame(positiveRequest,BAD_AUTH));
    }

    @Test
    @Order(4)
    @DisplayName("List Service")
    public void listTest() throws Exception {
        GameDataListItem listItem =
                new GameDataListItem(1,null,null,FIRST_GAME_NAME);
        Collection<GameDataListItem> list = new ArrayList<>();
        list.add(listItem);
        ListResponse positiveResponse = new ListResponse(list);
        Assertions.assertEquals(positiveResponse,listService.listGames(authToken));
    }

    @Test
    @Order(4)
    @DisplayName("Negative List Service")
    public void negativeListTest() throws Exception {
        Assertions.assertThrows(EndpointException.class, () -> listService.listGames(BAD_AUTH));
    }

    @Test
    @Order(5)
    @DisplayName("Join Service")
    public void joinTest() throws Exception {
        GameDataListItem listItem = new GameDataListItem(1,FIRST_USERNAME,SECOND_USERNAME,FIRST_GAME_NAME);
        Collection<GameDataListItem> list = new ArrayList<>();
        list.add(listItem);
        ListResponse positiveListResponse = new ListResponse(list);

        JoinGameRequest positiveRequest = new JoinGameRequest(ChessGame.TeamColor.WHITE,1);
        JoinGameRequest positiveRequest2 = new JoinGameRequest(ChessGame.TeamColor.BLACK,1);

        Assertions.assertDoesNotThrow(() -> joinGameService.joinGame(positiveRequest,authToken));
        Assertions.assertDoesNotThrow(() -> joinGameService.joinGame(positiveRequest2,authToken2));
        Assertions.assertEquals(positiveListResponse,listService.listGames(authToken));

    }

    @Test
    @Order(5)
    @DisplayName("Negative Join Service")
    public void negativeJoinTest() throws Exception {
        JoinGameRequest negativeRequest = new JoinGameRequest(ChessGame.TeamColor.WHITE, -1);
        JoinGameRequest negativeRequest2 = new JoinGameRequest(ChessGame.TeamColor.WHITE, 1);

        Assertions.assertThrows(EndpointException.class, () -> joinGameService.joinGame(negativeRequest, authToken));
        Assertions.assertThrows(EndpointException.class, () -> joinGameService.joinGame(negativeRequest2, authToken2));
    }

    @Test
    @Order(6)
    @DisplayName("Logout Service")
    public void logoutTest() throws Exception {
        //first user logs out
        Assertions.assertDoesNotThrow(() -> logoutService.logout(authToken));
    }
    @Test
    @Order(6)
    @DisplayName("Negative Logout Service")
    public void negativeLogoutTest() throws Exception {
        //authToken deleted after logout
        Assertions.assertThrows(EndpointException.class, () -> logoutService.logout(authToken));
        Assertions.assertThrows(EndpointException.class, () -> logoutService.logout(BAD_AUTH));
    }

    @Test
    @Order(7)
    @DisplayName("Login Service")
    public void loginTest() throws Exception {
        //first user logs back in after logging out
        LoginRequest positiveRequest = new LoginRequest(FIRST_USERNAME, SHARED_VALID_PASSWORD);
        Assertions.assertDoesNotThrow(() -> loginService.login(positiveRequest));

        //second user can log in multiple times without logging out
        LoginRequest positiveRequest2 = new LoginRequest(SECOND_USERNAME, SHARED_VALID_PASSWORD);
        Assertions.assertDoesNotThrow(() -> loginService.login(positiveRequest2));
        Assertions.assertNotEquals(loginService.login(positiveRequest2).authToken(),
                loginService.login(positiveRequest2).authToken());

    }
    @Test
    @Order(7)
    @DisplayName("Negative Login Service")
    public void negativeLoginTest() throws Exception {

        Assertions.assertThrows(EndpointException.class,
                () -> loginService.login(new LoginRequest("new_user", SHARED_VALID_PASSWORD)));

        Assertions.assertThrows(EndpointException.class,
                () -> loginService.login(new LoginRequest(FIRST_USERNAME, "new_password")));

    }
}
