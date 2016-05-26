package org.intellimate.izou.sdk.server;

import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.util.AddOnModule;
import org.intellimate.izou.sdk.util.FireEvent;
import org.intellimate.izou.system.context.System;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    private final MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();

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
    Optional<Response> handle(Request request) {
        Matcher matcher = pattern.matcher(request.getUrl());
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
     * serves the files at path for the get-requests, this method allows querying subdirectories.
     * @param path the path (must be a directory)
     */
    public void files(String path) {
        files(path, true);
    }

    /**
     * serves the files at path for the get-requests
     * @param path the path (must be a directory)
     * @param subdirectory whether to allow querying subdirectories
     */
    public void files(String path, boolean subdirectory) {
        handlers.add(new DefaultHandler(context, addOnPackageName, route, Method.GET, request -> {
            String[] split = request.getShortUrl().split("\\\\");
            if (!subdirectory && split.length != 1) {
                throw new NotFoundException(request.getUrl() + " not found");
            }
            String subpath = Arrays.stream(split).collect(Collectors.joining(File.separator));
            File file = new File(path + File.separator + subpath);
            if (file.exists()) {
                throw new NotFoundException(request.getUrl() + " not found");
            }
            String contentType = mimetypesFileTypeMap.getContentType(file);
            FileInputStream fileInputStream;
            try {
                fileInputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new NotFoundException(request.getUrl() + " not found");
            }
            return new Response(200, new HashMap<>(), contentType, file.length(), fileInputStream);
        }));
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
