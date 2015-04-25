package org.intellimate.izou.sdk.frameworks.permanentSoundOutput.events;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.events.CommonEvents;
import org.intellimate.izou.sdk.events.Event;
import org.intellimate.izou.sdk.util.AddOnModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * signals that an output started
 * @author LeanderK
 * @version 1.0
 */
public class StartEvent extends Event {
    public static final String ID = "izou.sound.events.start";
    /**
     * Creates a new Event Object
     *
     * @param source      the source of the Event, most likely a this reference.
     * @throws IllegalArgumentException if one of the Arguments is null or empty
     */
    protected StartEvent(AddOnModule addOnModule, Identification source)
            throws IllegalArgumentException {
        super(CommonEvents.get(addOnModule).getType().responseType(), source, new ArrayList<>(Arrays.asList(ID)));
    }

    /**
     * creates a new StartEvent
     * @param addOnModule a reference to the module which created this Event
     * @param source the caller
     * @return the optional StartMusicRequest
     */
    public static Optional<StartEvent> createStartEvent(AddOnModule addOnModule, Identification source) {
        try {
            StartEvent startRequest = new StartEvent(addOnModule, source);
            return Optional.of(startRequest);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
