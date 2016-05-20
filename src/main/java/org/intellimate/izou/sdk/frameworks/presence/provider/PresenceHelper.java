package org.intellimate.izou.sdk.frameworks.presence.provider;

import org.intellimate.izou.identification.IdentificationManager;
import org.intellimate.izou.sdk.frameworks.presence.events.LeavingEvent;
import org.intellimate.izou.sdk.frameworks.presence.events.PresenceEvent;
import org.intellimate.izou.sdk.util.FireEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * util functions for providing presence
 * @author LeanderK
 * @version 1.0
 */
public interface PresenceHelper extends PresenceProvider, FireEvent {
    /**
     * fires the presence-Event
     * @param known whether it is highly likely that the user cause the event and not a random person
    */
    default void firePresence(boolean known) {
        firePresence(new ArrayList<>(), known);
    }

    /**
     * fires the presence-Event
     * @param descriptors the descriptors
     * @param known whether it is highly likely that the user cause the event and not a random person
     */
    default void firePresence(List<String> descriptors, boolean known) {
        Optional<PresenceEvent> startEvent = IdentificationManagerM.getInstance().getIdentification(this)
                .flatMap(id -> PresenceEvent.createPresenceEvent(id, isStrict(), known, isFirstEncountering(), descriptors));
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
        Optional<LeavingEvent> startEvent = IdentificationManagerM.getInstance().getIdentification(this)
                .flatMap(id -> LeavingEvent.createLeavingEvent(id, isStrict(), new ArrayList<>()));
        if (!startEvent.isPresent()) {
            getContext().getLogger().error("unable to fire startEvent");
        } else {
            fire(startEvent.get(), 5);
        }
    }
}
