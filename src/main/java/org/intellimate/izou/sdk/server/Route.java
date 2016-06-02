package org.intellimate.izou.sdk.server;

import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.util.AddOnModule;
import org.intellimate.izou.sdk.util.FireEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

/**
 * a route in the router
 * @author LeanderK
 * @version 1.0
 */
//TODO automatically add things for Ajax
@SuppressWarnings("WeakerAccess")
public class Route extends AddOnModule implements HandlerHelper, FireEvent {
    /**
     * the regex route
     */
    private final String route;
    private final Pattern pattern;
    private final List<Handler> handlers = new ArrayList<>();
    private final Context context;
    private final String addOnPackageName;
    private final boolean internal;

    public Route(Context context, String route, String addOnPackageName, boolean internal) {
        super(context, addOnPackageName+"."+route);
        this.context = context;
        String realRoute = route;
        if (route.equals("/")) {
            realRoute = "";
        }
        this.route = realRoute;
        this.pattern = Pattern.compile(route);
        this.addOnPackageName = addOnPackageName;
        this.internal = internal;
    }

    /**
     * maybe handles a request
     * @param request the request
     * @return the request
     */
    Optional<Response> handle(Request request) {
        if (internal && !request.getToken().isPresent()) {
            return Optional.empty();
        }
        Matcher matcher = pattern.matcher(request.getShortUrl());
        if (matcher.matches()) {
            Request internalRequest = request.setMatcher(matcher);
            return handlers.stream()
                    .map(handler -> handler.handle(internalRequest))
                    .filter(Optional::isPresent)
                    .findAny()
                    .orElseThrow(() -> new NotFoundException(request));
        } else {
            return Optional.empty();
        }
    }

    /**
     * returns the current context
     * @return the current context
     */
    @SuppressWarnings("unused")
    public Context getContext() {
        return context;
    }

    /**
     * returns the active route
     * @return the route
     */
    @SuppressWarnings("unused")
    public String getRoute() {
        return route;
    }

    /**
     * handles all the get-Requests on the route
     * @param handleFunction the function to use for the get-requests
     */
    @SuppressWarnings("unused")
    public void get(Function<Request, Response> handleFunction) {
        handlers.add(new DefaultHandler(context, addOnPackageName, route, Method.GET, false, handleFunction));
    }

    /**
     * handles all the get-Requests on the route with the authentication of this app
     * @param handleFunction the function to use for the get-requests
     */
    @SuppressWarnings("unused")
    public void getInternal(Function<Request, Response> handleFunction) {
        handlers.add(new DefaultHandler(context, addOnPackageName, route, Method.GET, true, handleFunction));
    }

    /**
     * handles all the put-Requests on the route
     * @param handleFunction the function to use for the put-requests
     */
    @SuppressWarnings("unused")
    public void put(Function<Request, Response> handleFunction) {
        handlers.add(new DefaultHandler(context, addOnPackageName, route, Method.PUT, false, handleFunction));
    }

    /**
     * handles all the put-Requests on the route with the authentication of this app
     * @param handleFunction the function to use for the put-requests
     */
    @SuppressWarnings("unused")
    public void putInternal(Function<Request, Response> handleFunction) {
        handlers.add(new DefaultHandler(context, addOnPackageName, route, Method.PUT, true, handleFunction));
    }

    /**
     * handles all the patch-Requests on the route
     * @param handleFunction the function to use for the patch-requests
     */
    @SuppressWarnings("unused")
    public void patch(Function<Request, Response> handleFunction) {
        handlers.add(new DefaultHandler(context, addOnPackageName, route, Method.PATCH, false, handleFunction));
    }

    /**
     * handles all the patch-Requests on the route with the authentication of this app
     * @param handleFunction the function to use for the patch-requests
     */
    @SuppressWarnings("unused")
    public void patchInternal(Function<Request, Response> handleFunction) {
        handlers.add(new DefaultHandler(context, addOnPackageName, route, Method.PATCH, true, handleFunction));
    }

    /**
     * handles all the post-Requests on the route
     * @param handleFunction the function to use for the post-requests
     */
    @SuppressWarnings("unused")
    public void post(Function<Request, Response> handleFunction) {
        handlers.add(new DefaultHandler(context, addOnPackageName, route, Method.POST, false, handleFunction));
    }

    /**
     * handles all the post-Requests on the route with the authentication of this app
     * @param handleFunction the function to use for the post-requests
     */
    @SuppressWarnings("unused")
    public void postInternal(Function<Request, Response> handleFunction) {
        handlers.add(new DefaultHandler(context, addOnPackageName, route, Method.POST, true, handleFunction));
    }

    /**
     * handles all the delete-Requests on the route
     * @param handleFunction the function to use for the delete-requests
     */
    @SuppressWarnings("unused")
    public void delete(Function<Request, Response> handleFunction) {
        handlers.add(new DefaultHandler(context, addOnPackageName, route, Method.DELETE, false, handleFunction));
    }

    /**
     * handles all the delete-Requests on the route with the authentication of this app
     * @param handleFunction the function to use for the delete-requests
     */
    @SuppressWarnings("unused")
    public void deleteInternal(Function<Request, Response> handleFunction) {
        handlers.add(new DefaultHandler(context, addOnPackageName, route, Method.DELETE, true, handleFunction));
    }

    /**
     * handles all the options-Requests on the route
     * @param handleFunction the function to use for the options-requests
     */
    public void options(Function<Request, Response> handleFunction) {
        handlers.add(new DefaultHandler(context, addOnPackageName, route, Method.OPTIONS, true, handleFunction));
    }

    /**
     * serves the files at path for the get-requests, this method allows querying subdirectories.
     * @param path the path (must be a directory)
     */
    @SuppressWarnings("unused")
    public void files(String path) {
        files(path, true);
    }

    /**
     * serves the files at path for the get-requests, this is public to the other addons
     * @param path the path (must be a directory)
     * @param subdirectory whether to allow querying subdirectories
     */
    @SuppressWarnings("unused")
    public void files(String path, boolean subdirectory) {
        handlers.add(new DefaultHandler(context, addOnPackageName, route, Method.GET, false, request -> {
            String[] split = request.getShortUrl().split("\\\\");
            if (!subdirectory && split.length != 1) {
                throw new NotFoundException(request.getUrl() + " not found");
            }
            String subpath = Arrays.stream(split).collect(Collectors.joining(File.separator));
            File file = new File(path + File.separator + subpath);
            return sendFile(request, file);
        }));
    }

    /**
     * handles all the Requests on the route
     * @param handleFunction the function to use for the requests
     */
    @SuppressWarnings("unused")
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

    /**
     * adds an handler
     * @param handler the handler to add
     */
    @SuppressWarnings("unused")
    public void addHandler(Handler handler) {
        handlers.add(handler);
    }
}
