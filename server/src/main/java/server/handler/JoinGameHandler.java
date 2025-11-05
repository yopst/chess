package server.handler;

import io.javalin.http.Handler;
import request.JoinGameRequest;
import response.JoinGameResponse;
import server.exception.EndpointException;
import server.service.JoinGameService;

public class JoinGameHandler extends BaseHandler<JoinGameRequest, JoinGameResponse> implements Handler {
    @Override
    protected JoinGameResponse performRequest(JoinGameRequest reqObj) throws EndpointException {
        return new JoinGameService().joinGame(reqObj, authToken);
    }

    @Override
    protected Class<JoinGameRequest> getRequestClassType() {
        return JoinGameRequest.class;
    }
}
