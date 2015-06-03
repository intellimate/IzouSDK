package org.intellimate.izou.sdk.frameworks.permanentSoundOutput.events;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.events.CommonEvents;
import org.intellimate.izou.sdk.events.Event;
import org.intellimate.izou.system.sound.SoundIDs;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

/**
 * signals that an output started
 * @author LeanderK
 * @version 1.0
 */
public class StartEvent extends Event {
    public static final String ID = SoundIDs.StartEvent.descriptor;
    public static final String IS_USING_NON_JAVA_OUTPUT = SoundIDs.StartEvent.isUsingNonJava;

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
     * Creates a new Event Object
     *
     * @param source      the source of the Event, most likely a this reference.
     * @param descriptor the descriptor to add (used for IS_USING_NON_JAVA_OUTPUT)
     * @throws IllegalArgumentException if one of the Arguments is null or empty
     */
    protected StartEvent(Identification source, String descriptor)
            throws IllegalArgumentException {
        super(CommonEvents.Type.RESPONSE_TYPE, source, Arrays.asList(ID, descriptor));
    }


    /**
     * creates a new StartEvent.
     * Assumes the Output is using the java-sound output.
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

    /**
     * creates a new StartEvent
     * @param source the caller
     * @param isUsingJava true if using java, false if not (and for example a C-library)
     * @return the optional StartMusicRequest
     */
    public static Optional<StartEvent> createStartEvent(Identification source, boolean isUsingJava) {
        try {
            StartEvent startEvent;
            if (isUsingJava) {
                startEvent  = new StartEvent(source);
            } else {
                startEvent =  new StartEvent(source, IS_USING_NON_JAVA_OUTPUT);
            }
            return Optional.of(startEvent);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
