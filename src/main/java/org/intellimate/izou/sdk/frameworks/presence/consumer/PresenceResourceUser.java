package org.intellimate.izou.sdk.frameworks.presence.consumer;

import org.intellimate.izou.sdk.frameworks.presence.resources.PresenceResourceHelper;
import org.intellimate.izou.sdk.util.ThreadPoolUser;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * utility class used interacting with information about presence obtained through resource.
 * For events, please use the normal Listeners.
 * @author LeanderK
 * @version 1.0
 */
public interface PresenceResourceUser extends PresenceResourceHelper, ThreadPoolUser {
    /**
     * returns a CompletableFuture containing true if present, else false.
     * if not presence-providers were found, it returns false.
     * Time-Outs every Provider after 500 milliseconds.
     * @return a future true if present, false if not
     */
    default boolean isPresent() {
        return isPresent(false, false, 500);
    }

    /**
     * returns a CompletableFuture containing true if present, else false
     * if not presence-providers were found, it returns false.
     * Time-Outs every Provider after 500 milliseconds.
     * @param strict true if only addons where it is highly likely that the user is around should be creating the result
     * @return a future true if present, false if not
     */
    default boolean isPresent(boolean strict) {
        return isPresent(strict, false, 500);
    }

    /**
     * returns a CompletableFuture containing true if present, else false.
     * if not presence-providers were found, it returns false.
     * Time-Outs every Provider after 500 milliseconds.
     * @param strict true if only addons where it is highly likely that the user is around should be creating the result
     * @param ifNotPresent the default value
     * @return a future true if present, false if not
     */
    default boolean isPresent(boolean strict, boolean ifNotPresent) {
        return isPresent(strict, ifNotPresent, 500);
    }

    /**
     * returns a CompletableFuture containing true if present, else false.
     * if not presence-providers were found, it returns false.
     * Time-Outs every Provider after 500 milliseconds.
     * @param strict true if only addons where it is highly likely that the user is around should be creating the result
     * @param ifNotPresent the default value
     * @param timeout the timeout in milliseconds
     * @return a future true if present, false if not
     */
    default boolean isPresent(boolean strict, boolean ifNotPresent, int timeout) {
        try {
            return getIsPresent(strict, ifNotPresent)
                    .get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            error("unable to get Value", e);
            return false;
        }
    }
}
