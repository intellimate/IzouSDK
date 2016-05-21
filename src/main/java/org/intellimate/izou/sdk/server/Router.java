package org.intellimate.izou.sdk.server;

import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.server.*;
import org.intellimate.izou.server.Request;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * a simple router
 * @author LeanderK
 * @version 1.0
 */
public class Router implements HandlerHelper {
    private final String addOnPackageName;
    private List<Route> routes = new ArrayList<>();
    private Map<Class<? extends Exception>, BiFunction<Request, ? extends Exception, Response>> exceptionHandlers = new HashMap<>();
    private final Context context;

    /**
     * creates a new Router
     * @param context the context to use
     */
    public Router(Context context, String addOnPackageName) {
        this.context = context;
        this.addOnPackageName = addOnPackageName;
    }

    public Response handle(org.intellimate.izou.server.Request request) {
        Response response = routes.stream()
                .map(route -> {
                    try {
                        return route.handle(request);
                    } catch (Exception e) {
                        return Optional.of(handleException(e, request));
                    }
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny()
                .orElseGet(() -> stringResponse("no matching routes found", 404));
        sanityCheck(response);
        return response;
    }

    /**
     * handles the thrown exceptions while generating content
     * @param e the exception
     * @return a response
     */
    private Response handleException(Exception e, org.intellimate.izou.server.Request request) {
        Response response = exceptionHandlers.entrySet().stream()
                .filter(entry -> entry.getKey().isAssignableFrom(e.getClass()))
                .map(entry -> {
                    BiFunction value = entry.getValue();
                    return (Response) value.apply(request, e);
                })
                .findAny()
                .orElseGet(() -> {
                    context.getLogger().error("en error occured while trying to server request for: " + request.getUrl(), e);
                    return stringResponse("unknown internalServerError", 500);
                });
        if (response == null || !response.isValidResponse()) {
            return stringResponse("error handling returned illegal response", 500);
        } else {
            return response;
        }
    }

    /**
     * returns the instance of Context
     *
     * @return the instance of Context
     */
    public Context getContext() {
        return context;
    }

    /**
     * adds a new route
     * @param regex the regex-route to match
     * @param consumer the consumer used to initialize the route
     */
    public void route(String regex, Consumer<Route> consumer) {
        Route route = new Route(context, regex, addOnPackageName);
        consumer.accept(route);
        routes.add(route);
    }

    /**
     * registers an Exception to catch and handle
     * @param handleFunction the function to call when an exception was triggered
     */
    public <T extends Exception> void exception(Class<T> clazz, BiFunction<Request, T, Response> handleFunction) {
        exceptionHandlers.put(clazz, handleFunction);
    }
}
