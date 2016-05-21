package org.intellimate.izou.sdk.server;

import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.util.ContextProvider;

import java.nio.charset.Charset;
import java.util.HashMap;

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
    default String constructLink(String route) {
        return getContext().getIzouServerURL().orElse("locahost:80")+"/"+route;
    }

    /**
     * constructs the response for a String
     * @param message the message to print
     * @param status the status of the message
     * @return the response
     */
    default Response stringResponse(String message, int status) {
        return new Response(status, new HashMap<>(), "text", message.getBytes(Charset.forName("UTF-8")));
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
