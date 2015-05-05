package org.intellimate.izou.sdk.frameworks.music.events;

import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.identification.Identifiable;
import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.events.CommonEvents;
import org.intellimate.izou.sdk.events.Event;
import org.intellimate.izou.sdk.frameworks.common.resources.SelectorResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * use this event to stop the music from playing
 * @author LeanderK
 * @version 1.0
 */
public class StopMusic extends Event {
    public static final String ID = "izou.music.events.stop";
    /**
     * Creates a new Event Object
     *
     * @param source      the source of the Event, most likely a this reference.
     * @throws IllegalArgumentException if one of the Arguments is null or empty
     */
    protected StopMusic(Identification source)
            throws IllegalArgumentException {
        super(CommonEvents.Type.RESPONSE_TYPE, source, new ArrayList<>(Arrays.asList(ID,
                CommonEvents.Descriptors.STOP_DESCRIPTOR, CommonEvents.Descriptors.NOT_INTERRUPT)));
    }

    /**
     * creates a new StartRequest
     * @param source the caller
     * @param target the target who should start playing
     * @return the optional StartMusicRequest
     */
    public static Optional<StopMusic> createStopMusic(Identification source, Identification target) {
        if (target == null || target.equals(source))
            return Optional.empty();
        try {
            StopMusic stopRequest = new StopMusic(source);
            stopRequest.addResource(new SelectorResource(source, target));
            return Optional.of(stopRequest);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    /**
     * verifies that the StopMusicRequest is correct and checks whether the you are meant to react to it
     * @param eventModel the EventModel to check against
     * @param player the identifiable
     * @return true if verified, false if not
     */
    public static boolean verify(EventModel eventModel, Identifiable player) {
        if (!eventModel.containsDescriptor(StopMusic.ID))
            return false;
        return SelectorResource.isTarget(eventModel, player)
                .orElse(true);
    }
}
