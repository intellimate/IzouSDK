package org.intellimate.izou.sdk.frameworks.permanentSoundOutput.events;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.events.CommonEvents;
import org.intellimate.izou.sdk.events.Event;
import org.intellimate.izou.sdk.frameworks.common.resources.SelectorResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * signals that every sound-output should stop
 * @author LeanderK
 * @version 1.0
 */
public class StopEvent extends Event {
    public static final String ID = "izou.sound.events.stop";
    /**
     * Creates a new Event Object
     *
     * @param source      the source of the Event, most likely a this reference.
     * @throws IllegalArgumentException if one of the Arguments is null or empty
     */
    protected StopEvent(Identification source)
            throws IllegalArgumentException {
        super(CommonEvents.Type.RESPONSE_TYPE, source, new ArrayList<>(Arrays.asList(ID,
                CommonEvents.Descriptors.STOP_DESCRIPTOR)));
    }

    /**
     * creates a new StopEvent
     * @param source the caller
     * @param target the target who should start playing
     * @return the optional StartMusicRequest
     */
    public static Optional<StopEvent> createStopEvent(Identification source, Identification target) {
        if (target == null || target.equals(source))
            return Optional.empty();
        try {
            StopEvent stopRequest = new StopEvent(source);
            stopRequest.addResource(new SelectorResource(source, target));
            return Optional.of(stopRequest);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    /**
     * creates a new StopEvent
     * @param source the caller
     * @return the optional StartMusicRequest
     */
    public static Optional<StopEvent> createStopEvent(Identification source) {
        try {
            StopEvent stopRequest = new StopEvent(source);
            return Optional.of(stopRequest);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
