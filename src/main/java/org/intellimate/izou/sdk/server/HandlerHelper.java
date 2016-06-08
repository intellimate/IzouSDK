package org.intellimate.izou.sdk.server;

import org.intellimate.izou.identification.AddOnInformation;
import org.intellimate.izou.sdk.Context;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

/**
 * @author LeanderK
 * @version 1.0
 */
public interface HandlerHelper {
    MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();

    /**
     * creates a permanent 308 redirect
     * @param url the url to redirecto to
     * @return a redirect
     */
    default Response createRedirect(String url) {
        Map<String, List<String>> header = new HashMap<>();
        LinkedList<String> list = new LinkedList<>();
        list.add(url);
        header.put("Location", list);
        String body = "This page is located <a href=\""+url+"\">here</a>.";
        return new Response(308, header, "text/html", body);
    }

    /**
     * constructs a link, falling back to localhost port 80 if no valid izou-server link was found
     * @param route the route to add to the link, e.g. apps/1
     * @return a Link
     */
    @SuppressWarnings("unused")
    default String constructLinkToServer(String route) {
        String id = getContext().getAddOns().getAddOn().getID();
        Optional<AddOnInformation> addOnInformation = getContext().getAddOns().getAddOnInformation(id);
        String base = getContext().getServerInformation()
                .getIzouServerURL()
                .map(url -> {
                    if (url.endsWith("/")) {
                        return url.substring(0, url.length() - 1);
                    } else {
                        return url;
                    }
                }).orElse("locahost:80");
        if (!route.startsWith("/")) {
            base = base + "/";
        }
        return base + route;
    }

    /**
     * constructs a link, falling back to localhost port 80 if no valid izou-server link was found
     * @param addOnInformation the addon to link to
     * @param route the route to add to the link, e.g. apps/1
     * @return a Link
     */
    @SuppressWarnings("unused")
    default String constructLinkToAddon(AddOnInformation addOnInformation, String route) {
        String base = getContext().getServerInformation()
                .getIzouServerURL()
                .map(url -> {
                    if (url.endsWith("/")) {
                        return url.substring(0, url.length() - 1);
                    } else {
                        return url;
                    }
                })
                .flatMap(url ->
                        getContext().getServerInformation().getIzouRoute()
                                .map(izouRoute1 -> {
                                    if (izouRoute1.startsWith("/")) {
                                        return izouRoute1;
                                    } else {
                                        return "/" + izouRoute1;
                                    }
                                })
                                .map(izouRoute1 -> url + izouRoute1)
                )
                .map(url -> {
                    if (url.endsWith("/")) {
                        return url.substring(0, url.length() - 1);
                    } else {
                        return url;
                    }
                })
                .orElse("locahost:80/users/1/izou/1/instance/");
        String urlWithAddon;
        Optional<Integer> serverID = addOnInformation.getServerID();
        if (serverID.isPresent()) {
            urlWithAddon = base + "apps/" + serverID.get();
        } else {
            urlWithAddon = base + "apps/dev/" + addOnInformation.getArtifactID();
        }
        if (!route.startsWith("/")) {
            urlWithAddon = urlWithAddon + "/";
        }
        return urlWithAddon + route;
    }

    /**
     * constructs the response for a String
     * @param message the message to print
     * @param status the status of the message
     * @return the response
     */
    default Response stringResponse(String message, int status) {
        return new Response(status, new HashMap<>(), "text/plain", message);
    }

    /**
     * returns the context
     * @return the context
     */
    Context getContext();

    /**
     * checks if the response is null or not valid, throws an InternalServerErrorException if thats the case
     * @param response the response to check
     */
    default void sanityCheck(Response response) {
        if (response == null  || !response.isValidResponse()) {
            throw new InternalServerErrorException("App returned illegal response");
        }
    }

    /**
     * sends the file, or throws an {@link NotFoundException} if not existing
     * @param request the request to answer
     * @param file the file to send
     * @return an response
     */
    default Response sendFile(Request request, URL file) throws NotFoundException {
        String path = file.getFile();
        if (path == null) {
            throw new InternalServerErrorException("requested file for url: " + request.getUrl() + " not found");
        }
        return sendFile(request, new File(path));
    }

    /**
     * sends the file, or throws an {@link NotFoundException} if not existing
     * @param request the request to answer
     * @param file the file to send
     * @return an response
     */
    default Response sendFile(Request request, File file) throws NotFoundException {
        if (!file.exists()) {
            throw new InternalServerErrorException("requested file for url: " + request.getUrl() + " not found");
        }

        String contentType = mimetypesFileTypeMap.getContentType(file);
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new NotFoundException(request.getUrl() + " not found");
        }
        return new Response(200, new HashMap<>(), contentType, file.length(), fileInputStream);
    }
}
