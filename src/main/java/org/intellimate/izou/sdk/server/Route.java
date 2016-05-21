package org.intellimate.izou.sdk.server;

import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.util.AddOnModule;
import org.intellimate.izou.sdk.util.FireEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * a route in the router
 * @author LeanderK
 * @version 1.0
 */
public class Route extends AddOnModule implements HandlerHelper, FireEvent {
    /**
     * the regex route
     */
    private final String route;
    private final Pattern pattern;
    private final List<Handler> handlers = new ArrayList<>();
    private final Context context;
    private final String addOnPackageName;

    public Route(Context context, String route, String addOnPackageName) {
        super(context, addOnPackageName+"."+route);
        this.context = context;
        this.route = route;
        this.pattern = Pattern.compile(route);
        this.addOnPackageName = addOnPackageName;
    }

    /**
     * maybe handles a request
     * @param request the request
     * @return the request
     */
    Optional<Response> handle(org.intellimate.izou.server.Request request) {
        Matcher matcher = pattern.matcher(request.getUrl());
        if (matcher.matches()) {
            Request internalRequest = new Request(request, matcher);
            return handlers.stream()
                    .map(handler -> handler.handle(internalRequest))
                    .filter(Optional::isPresent)
                    .findAny()
                    .orElseThrow(NotFoundException::new);
        } else {
            return Optional.empty();
        }
    }

    /**
     * returns the current context
     * @return the current context
     */
    public Context getContext() {
        return context;
    }

    /**
     * returns the active route
     * @return the route
     */
    public String getRoute() {
        return route;
    }

    /**
     * handles all the get-Requests on the route
     * @param handleFunction the function to use for the get-requests
     */
    public void get(Function<Request, Response> handleFunction) {
        handlers.add(new DefaultHandler(context, addOnPackageName, route, Method.GET, handleFunction));
    }

    /**
     * handles all the put-Requests on the route
     * @param handleFunction the function to use for the put-requests
     */
    public void put(Function<Request, Response> handleFunction) {
        handlers.add(new DefaultHandler(context, addOnPackageName, route, Method.PUT, handleFunction));
    }

    /**
     * handles all the patch-Requests on the route
     * @param handleFunction the function to use for the patch-requests
     */
    public void patch(Function<Request, Response> handleFunction) {
        handlers.add(new DefaultHandler(context, addOnPackageName, route, Method.PATCH, handleFunction));
    }

    /**
     * handles all the post-Requests on the route
     * @param handleFunction the function to use for the post-requests
     */
    public void post(Function<Request, Response> handleFunction) {
        handlers.add(new DefaultHandler(context, addOnPackageName, route, Method.POST, handleFunction));
    }

    /**
     * handles all the delete-Requests on the route
     * @param handleFunction the function to use for the delete-requests
     */
    public void delete(Function<Request, Response> handleFunction) {
        handlers.add(new DefaultHandler(context, addOnPackageName, route, Method.DELETE, handleFunction));
    }

    /**
     * handles all the Requests on the route
     * @param handleFunction the function to use for the requests
     */
    public void all(Function<Request, Response> handleFunction) {
        handlers.add(new Handler() {
            @Override
            public Optional<Response> handle(Request request) {
                Response response = handleFunction.apply(request);
                sanityCheck(response);
                return Optional.of(response);
            }

            @Override
            public Context getContext() {
                return context;
            }
        });
    }
}
