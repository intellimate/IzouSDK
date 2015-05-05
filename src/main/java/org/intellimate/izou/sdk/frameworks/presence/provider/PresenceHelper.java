package org.intellimate.izou.sdk.frameworks.presence.provider;

import org.intellimate.izou.identification.IdentificationManager;
import org.intellimate.izou.sdk.frameworks.presence.events.LeavingEvent;
import org.intellimate.izou.sdk.frameworks.presence.events.PresenceEvent;
import org.intellimate.izou.sdk.util.FireEvent;

import java.util.ArrayList;
import java.util.Optional;

/**
 * util functions for providing presence
 * @author LeanderK
 * @version 1.0
 */
public interface PresenceHelper extends PresenceProvider, FireEvent {
    /**
     * fires the presence-Event
     */
    default void firePresence() {
        Optional<PresenceEvent> startEvent = IdentificationManager.getInstance().getIdentification(this)
                .flatMap(id -> PresenceEvent.createPresenceEvent(id, isStrict(), new ArrayList<>()));
        if (!startEvent.isPresent()) {
            getContext().getLogger().error("unable to fire startEvent");
        } else {
            fire(startEvent.get(), 5);
        }
    }
    /**
     * fires the leaving-Event
     */
    default void fireLeaving() {
        Optional<LeavingEvent> startEvent = IdentificationManager.getInstance().getIdentification(this)
                .flatMap(id -> LeavingEvent.createLeavingEvent(id, isStrict(), new ArrayList<>()));
        if (!startEvent.isPresent()) {
            getContext().getLogger().error("unable to fire startEvent");
        } else {
            fire(startEvent.get(), 5);
        }
    }
}
