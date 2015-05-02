package org.intellimate.izou.sdk.frameworks.permanentSoundOutput.events;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.events.CommonEvents;
import org.intellimate.izou.sdk.events.Event;
import org.intellimate.izou.sdk.frameworks.common.resources.SelectorResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * signals that every Sound-Output should mute
 * @author LeanderK
 * @version 1.0
 */
public class MuteEvent extends Event {
    public static final String ID = "izou.sound.events.mute";
    /**
     * Creates a new Event Object
     *
     * @param source      the source of the Event, most likely a this reference.
     * @throws IllegalArgumentException if one of the Arguments is null or empty
     */
    protected MuteEvent(Identification source)
            throws IllegalArgumentException {
        super(CommonEvents.Type.RESPONSE_TYPE, source, new ArrayList<>(Arrays.asList(ID,
                CommonEvents.Descriptors.STOP_DESCRIPTOR)));
    }

    /**
     * creates a new MuteEvent
     * @param source the caller
     * @param target the target who should start playing
     * @return the optional StartMusicRequest
     */
    public static Optional<MuteEvent> createStopMusic(Identification source, Identification target) {
        if (target == null || target.equals(source))
            return Optional.empty();
        try {
            MuteEvent muteRequest = new MuteEvent(source);
            muteRequest.addResource(new SelectorResource(source, target));
            return Optional.of(muteRequest);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
