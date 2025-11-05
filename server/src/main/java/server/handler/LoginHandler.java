package server.handler;

import io.javalin.http.Handler;
import request.LoginRequest;
import response.LoginResponse;
import server.exception.EndpointException;
import server.service.LoginService;

public class LoginHandler extends BaseHandler<LoginRequest, LoginResponse> implements Handler {
    @Override
    protected LoginResponse performRequest(LoginRequest reqObj) throws EndpointException {
        return new LoginService().login(reqObj);
    }

    @Override
    protected Class<LoginRequest> getRequestClassType() {
        return LoginRequest.class;
    }
}
