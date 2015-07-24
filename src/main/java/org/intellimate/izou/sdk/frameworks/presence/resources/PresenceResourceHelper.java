package org.intellimate.izou.sdk.frameworks.presence.resources;

import org.intellimate.izou.sdk.frameworks.presence.provider.Presence;
import org.intellimate.izou.sdk.util.ResourceUser;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * provides basic methods to interact with the Presence Resource
 * @author LeanderK
 * @version 1.0
 */
public interface PresenceResourceHelper extends ResourceUser {
    /**
     * updates the boolean a non Strict provider is available
     */
    default CompletableFuture<Boolean> nonStrictAvailable() {
        return generateResource(PresenceResource.ID)
                .orElse(CompletableFuture.completedFuture(new ArrayList<>()))
                .thenApply(list -> list.stream()
                        .map(Presence::importPresence)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .noneMatch(Presence::isStrict));
    }

    /**
     * returns a CompletableFuture containing true if present, else false.
     * if not presence-providers were found, it returns false.
     * @param strict true if only addons where it is highly likely that the user is around should be creating the result
     * @param ifNotPresent the default value
     * @return a future true if present, false if not
     */
    default CompletableFuture<Boolean> getIsPresent(boolean strict, boolean ifNotPresent) {
        return generateResource(PresenceResource.ID)
                .map(future -> future.thenApply(
                        list -> list.stream()
                                .map(Presence::importPresence)
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .filter(Presence::isKnown)
                                .filter(presence -> !strict || presence.isStrict())
                                .collect(Collectors.toList()))
                        .thenApply(list -> {
                            if (list.isEmpty()) {
                                return ifNotPresent;
                            } else {
                                return list.stream()
                                        .anyMatch(Presence::isPresent);
                            }
                        })
                )
                .orElse(CompletableFuture.completedFuture(ifNotPresent));
    }
}
