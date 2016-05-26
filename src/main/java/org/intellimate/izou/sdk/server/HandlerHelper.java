package org.intellimate.izou.sdk.server;

import org.intellimate.izou.identification.AddOnInformation;
import org.intellimate.izou.sdk.Context;

import java.util.HashMap;
import java.util.Optional;

/**
 * @author LeanderK
 * @version 1.0
 */
public interface HandlerHelper {
    /**
     * constructs a link, falling back to localhost port 80 if no valid izou-server link was found
     * @param route the route to add to the link, e.g. apps/1
     * @return a Link
     */
    @SuppressWarnings("unused")
    default String constructLinkToServer(String route) {
        return getContext().getIzouServerURL().orElse("locahost:80")+"/"+route;
    }

    /**
     * constructs a link, falling back to localhost port 80 if no valid izou-server link was found
     * @param route the route to add to the link, e.g. apps/1
     * @return a Link
     */
    @SuppressWarnings("unused")
    default String constructLink(String route) {
        String id = getContext().getAddOns().getAddOn().getID();
        Optional<AddOnInformation> addOnInformation = getContext().getAddOns().getAddOnInformation(id);
        return getContext().getIzouServerURL().orElse("locahost:80")+"/"+route;
    }

    /**
     * constructs a link, falling back to localhost port 80 if no valid izou-server link was found
     * @param addOnInformation the addon to link to
     * @param route the route to add to the link, e.g. apps/1
     * @return a Link
     */
    @SuppressWarnings("unused")
    default String constructLinkToAddon(AddOnInformation addOnInformation, String route) {
        //TODO getIzouServerURL() when implemented!
        String base = getContext().getIzouServerURL().orElse("locahost:80") + "/users/1/izou/1/instance/";
        String urlWithAddon;
        Optional<Integer> serverID = addOnInformation.getServerID();
        if (serverID.isPresent()) {
            urlWithAddon = base + "apps/" + serverID.get();
        } else {
            urlWithAddon = base + "apps/dev/" + addOnInformation.getName();
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
}
