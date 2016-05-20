package org.intellimate.izou.sdk.frameworks.permanentSoundOutput.output;

import org.intellimate.izou.identification.Identifiable;
import org.intellimate.izou.identification.IdentificationManagerM;
import org.intellimate.izou.sdk.frameworks.permanentSoundOutput.events.EndedEvent;
import org.intellimate.izou.sdk.frameworks.permanentSoundOutput.events.StartEvent;
import org.intellimate.izou.sdk.util.ContextProvider;
import org.intellimate.izou.sdk.util.FireEvent;

import java.util.Optional;

/**
 * interface which provides various methods to
 * @author LeanderK
 * @version 1.0
 */
public interface PermanentSoundHelper extends Identifiable, FireEvent, ContextProvider {

    /**
     * fires an StartEvent
     * @param isUsingJava true if using java, false if not (and for example a C-library)
     */
    default void startedSound(boolean isUsingJava) {
        Optional<StartEvent> startEvent = IdentificationManagerM.getInstance().getIdentification(this)
                .flatMap(id -> StartEvent.createStartEvent(id, isUsingJava));
        if (!startEvent.isPresent()) {
            getContext().getLogger().error("unable to fire startEvent");
        } else {
            getContext().getEvents().distributor().fireEventConcurrently(startEvent.get());
        }
    }

    /**
     * fires an EndedEvent
     */
    default void endedSound() {
        Optional<EndedEvent> ended = IdentificationManagerM.getInstance().getIdentification(this)
                .flatMap(EndedEvent::createEndedEvent);
        if (!ended.isPresent()) {
            getContext().getLogger().error("unable to fire ended");
        } else {
            getContext().getEvents().distributor().fireEventConcurrently(ended.get());
        }
    }
}
