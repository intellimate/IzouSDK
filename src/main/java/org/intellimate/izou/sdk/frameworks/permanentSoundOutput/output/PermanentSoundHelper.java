package org.intellimate.izou.sdk.frameworks.permanentSoundOutput.output;

import org.intellimate.izou.identification.Identifiable;
import org.intellimate.izou.identification.IdentificationManager;
import org.intellimate.izou.sdk.frameworks.permanentSoundOutput.events.EndedEvent;
import org.intellimate.izou.sdk.frameworks.permanentSoundOutput.events.StartEvent;
import org.intellimate.izou.sdk.util.ContextProvider;
import org.intellimate.izou.sdk.util.FireEvent;

import java.util.Optional;

/**
 * interface which provides
 * @author LeanderK
 * @version 1.0
 */
public interface PermanentSoundHelper extends Identifiable, FireEvent, ContextProvider {

    /**
     * fires an StartEvent
     */
    default void startedSound() {
        Optional<StartEvent> startEvent = IdentificationManager.getInstance().getIdentification(this)
                .flatMap(StartEvent::createStartEvent);
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
        Optional<EndedEvent> ended = IdentificationManager.getInstance().getIdentification(this)
                .flatMap(EndedEvent::createEndedEvent);
        if (!ended.isPresent()) {
            getContext().getLogger().error("unable to fire ended");
        } else {
            getContext().getEvents().distributor().fireEventConcurrently(ended.get());
        }
    }
}
