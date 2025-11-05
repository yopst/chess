package server.handler;

import io.javalin.http.Handler;
import request.RegisterRequest;
import response.RegisterResponse;
import server.exception.EndpointException;
import server.service.RegisterService;

public class RegisterHandler extends BaseHandler<RegisterRequest, RegisterResponse> implements Handler {
    @Override
    protected RegisterResponse performRequest(RegisterRequest reqObj) throws EndpointException {
        return new RegisterService().register(reqObj);
    }

    @Override
    protected Class<RegisterRequest> getRequestClassType() {
        return RegisterRequest.class;
    }
}
