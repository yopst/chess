package server.handler;

import io.javalin.http.Handler;
import request.ClearRequest;
import response.ClearResponse;
import server.service.ClearService;

public class ClearHandler extends BaseHandler<ClearRequest, ClearResponse> implements Handler {

    @Override
    protected ClearResponse performRequest(ClearRequest reqObj) throws Exception {
        return new ClearService().clear();
    }

    @Override
    protected Class<ClearRequest> getRequestClassType() {
        return ClearRequest.class;
    }
}
