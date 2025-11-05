package server.handler;

import io.javalin.http.Handler;
import request.CreateGameRequest;
import response.CreateGameResponse;
import server.exception.EndpointException;
import server.service.CreateGameService;

public class CreateGameHandler extends BaseHandler<CreateGameRequest, CreateGameResponse> implements Handler {
    @Override
    protected CreateGameResponse performRequest(CreateGameRequest reqObj) throws EndpointException {
        return new CreateGameService().createGame(reqObj, authToken);
    }
    @Override
    protected Class<CreateGameRequest> getRequestClassType() {
        return CreateGameRequest.class;
    }
}
