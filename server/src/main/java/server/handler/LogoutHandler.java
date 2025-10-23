package server.handler;

import io.javalin.http.Handler;
import request.LogoutRequest;
import response.LogoutResponse;
import server.service.LogoutService;

public class LogoutHandler extends BaseHandler<LogoutRequest, LogoutResponse> implements Handler {
    @Override
    protected LogoutResponse performRequest(LogoutRequest reqObj) throws Exception {
        return new LogoutService().logout(authToken);
    }

    @Override
    protected Class<LogoutRequest> getRequestClassType() {
        return LogoutRequest.class;
    }
}
