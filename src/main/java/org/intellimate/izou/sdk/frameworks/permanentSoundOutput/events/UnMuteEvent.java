package org.intellimate.izou.sdk.frameworks.permanentSoundOutput.events;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.events.CommonEvents;
import org.intellimate.izou.sdk.events.Event;
import org.intellimate.izou.sdk.frameworks.common.resources.SelectorResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * signals that every sound-output should unmute
 * @author LeanderK
 * @version 1.0
 */
public class UnMuteEvent extends Event {
    @SuppressWarnings("SpellCheckingInspection")
    public static final String ID = "izou.sound.events.unmute";
    /**
     * Creates a new Event Object
     *
     * @param source      the source of the Event, most likely a this reference.
     * @throws IllegalArgumentException if one of the Arguments is null or empty
     */
    protected UnMuteEvent(Identification source)
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
    public static Optional<UnMuteEvent> createUnMuteEvent(Identification source, Identification target) {
        if (target == null || target.equals(source))
            return Optional.empty();
        try {
            UnMuteEvent unmuteRequest = new UnMuteEvent(source);
            unmuteRequest.addResource(new SelectorResource(source, target));
            return Optional.of(unmuteRequest);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
