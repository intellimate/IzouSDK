package org.intellimate.izou.sdk.frameworks.presence.consumer;

import org.intellimate.izou.sdk.frameworks.presence.provider.Presence;
import org.intellimate.izou.sdk.frameworks.presence.resources.PresenceResource;
import org.intellimate.izou.sdk.util.ResourceUser;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * utility class used interacting with information about presence obtained through resource.
 * For events, please use the normal Listeners.
 * @author LeanderK
 * @version 1.0
 */
public interface PresenceResourceUser extends ResourceUser {
    /**
     * returns a CompletableFuture containing true if present, else false
     * @return a future true if present, false if not
     */
    default CompletableFuture<Boolean> isPresent() {
        return isPresent(false);
    }

    /**
     * returns a CompletableFuture containing true if present, else false
     * @param strict true if only addons where it is highly likely that the user is around should be creating the result
     * @return a future true if present, false if not
     */
    default CompletableFuture<Boolean> isPresent(boolean strict) {
        return generateResource(PresenceResource.ID)
                .orElse(CompletableFuture.completedFuture(new ArrayList<>()))
                .thenApply(list -> list.stream()
                        .map(Presence::importPresence)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .filter(presence -> !strict || presence.isStrict())
                        .anyMatch(Presence::isPresent));
    }
}
