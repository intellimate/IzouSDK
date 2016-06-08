package org.intellimate.izou.sdk.server;

import java.util.Optional;

/**
 * handles a request on a specific route
 * @author LeanderK
 * @version 1.0
 */
public interface Handler extends HandlerHelper {
    /**
     * maybe handles a request
     * @param request the request
     * @return empty if not responsible for the request or response
     */
    Optional<Response> handle(Request request);
}
