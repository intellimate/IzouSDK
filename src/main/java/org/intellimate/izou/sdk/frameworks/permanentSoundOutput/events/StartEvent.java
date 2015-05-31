package org.intellimate.izou.sdk.frameworks.permanentSoundOutput.events;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.events.CommonEvents;
import org.intellimate.izou.sdk.events.Event;
import org.intellimate.izou.system.sound.SoundIDs;

import java.util.Collections;
import java.util.Optional;

/**
 * signals that an output started
 * @author LeanderK
 * @version 1.0
 */
public class StartEvent extends Event {
    public static final String ID = SoundIDs.StartEvent.descriptor;
    /**
     * Creates a new Event Object
     *
     * @param source      the source of the Event, most likely a this reference.
     * @throws IllegalArgumentException if one of the Arguments is null or empty
     */
    protected StartEvent(Identification source)
            throws IllegalArgumentException {
        super(CommonEvents.Type.RESPONSE_TYPE, source, Collections.singletonList(ID));
    }

    /**
     * creates a new StartEvent
     * @param source the caller
     * @return the optional StartMusicRequest
     */
    public static Optional<StartEvent> createStartEvent(Identification source) {
        try {
            StartEvent startRequest = new StartEvent(source);
            return Optional.of(startRequest);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
