package org.intellimate.izou.sdk.server;

import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.util.AddOnModule;
import org.intellimate.izou.sdk.util.FireEvent;
import org.intellimate.izou.util.IzouModule;

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * handles a request on a specific route
 * @author LeanderK
 * @version 1.0
 */
public class DefaultHandler extends AddOnModule implements Handler, FireEvent {
    private final Method method;
    private final Function<Request, Response> function;

    /**
     * initializes the Module
     * @param context the current context
     * @param method the registered method
     * @param function the function to execute
     */
    public DefaultHandler(Context context, String addOnPackageName, String route, Method method, Function<Request, Response> function) {
        super(context, addOnPackageName+"."+route+method.name());
        this.method = method;
        this.function = function;
    }

    @Override
    public Optional<Response> handle(Request request) {
        if (!request.getMethod().toLowerCase().equals(method.name().toLowerCase())) {
            return Optional.empty();
        }
        Response response = function.apply(request);
        if (response == null  || !response.isValidResponse()) {
            throw new InternalServerErrorException("App returned illegal response");
        }
        return Optional.of(response);
    }
}
