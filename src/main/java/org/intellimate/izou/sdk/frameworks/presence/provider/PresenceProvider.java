package org.intellimate.izou.sdk.frameworks.presence.provider;

import org.intellimate.izou.events.EventListenerModel;
import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.identification.Identifiable;
import org.intellimate.izou.sdk.frameworks.presence.events.LeavingEvent;
import org.intellimate.izou.sdk.frameworks.presence.events.PresenceEvent;
import org.intellimate.izou.sdk.frameworks.presence.resources.PresenceResource;
import org.intellimate.izou.sdk.util.ResourceUser;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * util class to provide information about the type of presence this addon can guarantee.
 * You have to register the class implementing this interface as a EventListenerModel
 * @author LeanderK
 * @version 1.0
 */
public interface PresenceProvider extends Identifiable, EventListenerModel, ResourceUser {
    /**
     * true if the addon can guarantee that the user is around, false if not
     * @return true if it can guarantee it, false if not
     */
    boolean isStrict();

    /**
     * gets the PresenceIndicatorLevel of the addon (mainly used for communication between presence-providing addons)
     * @return the Level
     */
    PresenceIndicatorLevel getLevel();

    /**
     * returns true if the user might be/is present
     * @return true if present
     */
    boolean isPresent();

    /**
     * when this method is called the present-status was changed
     * @param present true if present, false if not
     */
    void setGlobalPresent(boolean present);

    /**
     * returns true if the user is first encountered in the current mode
     */
    boolean isFirstEncountering();

    /**
     * when this method is called, the strict-present status was changed
     * @param present true if present, false if not
     */
    void setGlobalStrictPresent(boolean present);

    /**
     * Invoked when an activator-event occurs.
     *
     * @param event an instance of Event
     */
    @Override
    default void eventFired(EventModel event) {
        if (event.containsDescriptor(LeavingEvent.ID) || event.containsDescriptor(PresenceEvent.ID)) {
            if (event.containsDescriptor(LeavingEvent.ID)) {
                if (event.containsDescriptor(LeavingEvent.GENERAL_DESCRIPTOR)) {
                    setGlobalPresent(false);
                    setGlobalStrictPresent(false);
                } else if (event.containsDescriptor(LeavingEvent.STRICT_DESCRIPTOR)) {
                    nonStrictAvailable().thenAccept(available -> {
                        if (!available)
                            setGlobalPresent(false);
                        setGlobalStrictPresent(false);
                    });
                }
            } else {
                setGlobalPresent(true);
                if (event.containsDescriptor(PresenceEvent.STRICT_DESCRIPTOR))
                    setGlobalStrictPresent(true);
            }
        }
    }

    /**
     * updates the boolean whether it is the mode vague
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
}
