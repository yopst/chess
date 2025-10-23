package server.handler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import server.exception.EndpointException;


public abstract class BaseHandler<T, R> implements Handler {
    private final Gson gson = new Gson();
    protected record ErrorResponse(int status, String message) {}

    protected String authToken;

    @Override
    public void handle(@NotNull Context ctx) {
        try {
            // Deserialize JSON body (if any)
            T requestObj = deserialize(ctx, getRequestClassType());

            // Perform the logic
            R successResponse = performRequest(requestObj);

            // Send success response
            String jsonResponse = gson.toJson(successResponse);
            ctx.status(200)
                    .contentType("application/json")
                    .result(jsonResponse);

        } catch (EndpointException e) {
            sendError(ctx, e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            sendError(ctx, 500, e.getMessage());
        }
    }

    protected T deserialize(Context ctx, Class<T> clazz) {
        this.authToken = ctx.header("authorization");
        if (clazz == Void.class) {
            return null;
        }
        return gson.fromJson(ctx.body(), clazz);
    }

    protected void sendError(Context ctx, int status, String message) {
        ErrorResponse errorResponse = new ErrorResponse(status, "Error: " + message);
        String jsonError = gson.toJson(errorResponse);
        ctx.status(status)
                .contentType("application/json")
                .result(jsonError);
    }

    // Subclasses must define these
    protected abstract R performRequest(T reqObj) throws Exception;
    protected abstract Class<T> getRequestClassType();
}
