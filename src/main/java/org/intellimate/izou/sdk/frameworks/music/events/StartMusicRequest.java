package org.intellimate.izou.sdk.frameworks.music.events;

import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.identification.Identifiable;
import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.events.CommonEvents;
import org.intellimate.izou.sdk.events.Event;
import org.intellimate.izou.sdk.frameworks.common.resources.SelectorResource;
import org.intellimate.izou.sdk.frameworks.music.Capabilities;
import org.intellimate.izou.sdk.frameworks.music.player.TrackInfo;
import org.intellimate.izou.sdk.frameworks.music.resources.TrackInfoResource;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * this events request the start of the music-playing
 * @author LeanderK
 * @version 1.0
 */
public class StartMusicRequest extends Event {
    @SuppressWarnings("SpellCheckingInspection")
    public static final String ID = "izou.music.events.startrequest";

    /**
     * Creates a new Event Object
     *
     * @param source      the source of the Event, most likely a this reference.
     * @throws IllegalArgumentException if one of the Arguments is null or empty
     */
    protected StartMusicRequest(Identification source) throws IllegalArgumentException {
        super(CommonEvents.Type.RESPONSE_TYPE, source, Collections.singletonList(ID));
    }

    /**
     * creates a new StartRequest
     * @param source the caller
     * @param target the target who should start playing
     * @return the optional StartMusicRequest
     */
    public static Optional<StartMusicRequest> createStartMusicRequest(Identification source, Identification target) {
        return createStartMusicRequest(source, target, null);
    }

    /**
     * creates a new StartRequest
     * @param source the caller
     * @param target the target who should start playing
     * @param trackInfo the track to play
     * @return the optional StartMusicRequest
     */
    public static Optional<StartMusicRequest> createStartMusicRequest(Identification source, Identification target, TrackInfo trackInfo) {
        if (target.equals(source))
            return Optional.empty();
        try {
            StartMusicRequest request = new StartMusicRequest(source);
            request.addResource(new SelectorResource(source, target));
            if (trackInfo != null)
                request.addResource(new TrackInfoResource(target, trackInfo, source));
            return Optional.of(request);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    /**
     * verifies that the StartMusicRequest is correct and checks whether the you are meant to react to it
     * @param eventModel the EventModel to check against
     * @param player the identifiable
     * @param capabilities the capabilities
     * @param activators the activators which are able to start the player if it is not able to start from outside commands
     * @return true if verified, false if not
     */
    public static boolean verify(EventModel eventModel, Capabilities capabilities, Identifiable player, List<Identifiable> activators) {
        if (!eventModel.containsDescriptor(StartMusicRequest.ID))
            return false;
        if (!capabilities.handlesPlayRequestFromOutside()) {
            if (activators.stream()
                    .noneMatch(identifiable -> identifiable.isOwner(eventModel.getSource())))
                return false;
        }
        return SelectorResource.isTarget(eventModel, player)
                .orElse(false);
    }
}
