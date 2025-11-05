package server.handler;

import io.javalin.http.Handler;
import request.ListRequest;
import response.ListResponse;
import server.exception.EndpointException;
import server.service.ListService;

public class ListHandler extends BaseHandler<ListRequest, ListResponse> implements Handler {
    @Override
    protected ListResponse performRequest(ListRequest reqObj) throws EndpointException {
        return new ListService().listGames(authToken);
    }

    @Override
    protected Class<ListRequest> getRequestClassType() {
        return ListRequest.class;
    }
}
