package org.intellimate.izou.sdk.frameworks.permanentSoundOutput.events;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.events.CommonEvents;
import org.intellimate.izou.sdk.events.Event;
import org.intellimate.izou.system.sound.SoundIDs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * this event indicates that the sound ended
 * @author LeanderK
 * @version 1.0
 */
public class EndedEvent extends Event {
    public static final String ID = SoundIDs.EndedEvent.descriptor;
    /**
     * Creates a new Event Object
     *
     * @param source      the source of the Event, most likely a this reference.
     * @throws IllegalArgumentException if one of the Arguments is null or empty
     */
    protected EndedEvent(Identification source)
            throws IllegalArgumentException {
        super(CommonEvents.Type.RESPONSE_TYPE, source, new ArrayList<>(Arrays.asList(ID,
                CommonEvents.Descriptors.NOT_INTERRUPT)));
    }

    /**
     * creates a new EndedEvent
     * @param source the caller
     * @return the optional StartMusicRequest
     */
    public static Optional<EndedEvent> createEndedEvent(Identification source) {
        try {
            EndedEvent stopRequest = new EndedEvent(source);
            return Optional.of(stopRequest);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
