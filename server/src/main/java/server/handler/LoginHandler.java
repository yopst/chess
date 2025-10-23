package server.handler;

import io.javalin.http.Handler;
import request.LoginRequest;
import response.LoginResponse;
import server.service.LoginService;

public class LoginHandler extends BaseHandler<LoginRequest, LoginResponse> implements Handler {
    @Override
    protected LoginResponse performRequest(LoginRequest reqObj) throws Exception {
        return new LoginService().login(reqObj);
    }

    @Override
    protected Class<LoginRequest> getRequestClassType() {
        return LoginRequest.class;
    }
}
