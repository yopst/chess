package client;

import client.exceptions.ResponseException;
import com.google.gson.Gson;
import request.*;
import response.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Objects;

public class ServerFacade {
    public String authToken;
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public ClearResponse clear(ClearRequest req) throws ResponseException {
        var path = "/db";
        return this.makeRequest("DELETE", path, req, null, ClearResponse.class);
    }

    public RegisterResponse register(RegisterRequest req) throws ResponseException {
        var path = "/user";
        RegisterResponse response = this.makeRequest("POST", path, req, null, RegisterResponse.class);
        this.authToken = response.authToken();
        return response;
    }

    public LoginResponse login(LoginRequest req) throws ResponseException {
        var path = "/session";
        LoginResponse response = this.makeRequest("POST", path, req, null, LoginResponse.class);
        this.authToken = response.authToken();
        return response;
    }

    public LogoutResponse logout(LogoutRequest req) throws ResponseException {
        var path = "/session";
        LogoutResponse response = this.makeRequest("DELETE", path, req, this.authToken, LogoutResponse.class);
        this.authToken = null;
        return response;
    }

    public ListResponse list(ListRequest req) throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, req, this.authToken, ListResponse.class);
    }

    public CreateGameResponse createGame(CreateGameRequest req) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, req, this.authToken, CreateGameResponse.class);
    }

    public JoinGameResponse joinGame(JoinGameRequest req) throws ResponseException {
        var path = "/game";
        return this.makeRequest("PUT", path, req, this.authToken, JoinGameResponse.class);
    }

    private <T> T makeRequest(String method, String path, Object request, String authToken, Class<T> responseClass)
            throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            if (authToken != null) {
                http.addRequestProperty("authorization", authToken);
            }

            if (Objects.equals(method, "GET")) {
                http.setDoOutput(false);
            }
            else {
                http.setDoOutput(true);
                writeBody(request, http);
            }

            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException e) {
            throw e;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
