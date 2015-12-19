package org.intellimate.izou.sdk.frameworks.music.events;

import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.identification.Identifiable;
import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.events.CommonEvents;
import org.intellimate.izou.sdk.events.Event;
import org.intellimate.izou.sdk.frameworks.common.resources.SelectorResource;
import org.intellimate.izou.sdk.frameworks.music.Capabilities;
import org.intellimate.izou.sdk.frameworks.music.player.Playlist;
import org.intellimate.izou.sdk.frameworks.music.player.TrackInfo;
import org.intellimate.izou.sdk.frameworks.music.resources.PlaylistResource;
import org.intellimate.izou.sdk.frameworks.music.resources.TrackInfoResource;
import org.intellimate.izou.system.sound.SoundIDs;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * this events request the start of the music-playing
 * @author LeanderK
 * @version 1.0
 */
public class StartMusicRequest extends Event {
    //TODO in izou
    @SuppressWarnings("SpellCheckingInspection")
    public static final String ID = "izou.music.events.startrequest";

    /**
     * Creates a new Event Object
     *
     * @param source      the source of the Event, most likely a this reference.
     * @param isUsingJava true if the player is using java
     * @throws IllegalArgumentException if one of the Arguments is null or empty
     */
    protected StartMusicRequest(Identification source, boolean isUsingJava) throws IllegalArgumentException {
        super(CommonEvents.Type.RESPONSE_TYPE, source,
                isUsingJava ? Arrays.asList(ID) : Arrays.asList(ID, SoundIDs.StartEvent.isUsingNonJava)
        );
    }

    /**
     * creates a new StartRequest
     * @param source the caller
     * @param target the target who should start playing
     * @return the optional StartMusicRequest
     */
    public static Optional<StartMusicRequest> createStartMusicRequest(Identification source, Identification target) {
        return createStartMusicRequest(source, target, (TrackInfo) null);
    }

    /**
     * creates a new StartRequest
     * @param source the caller
     * @param target the target who should start playing
     * @param trackInfo the track to play
     * @return the optional StartMusicRequest
     */
    public static Optional<StartMusicRequest> createStartMusicRequest(Identification source, Identification target, TrackInfo trackInfo) {
        return createStartMusicRequest(source, target, trackInfo, false);
    }

    /**
     * creates a new StartRequest
     * @param source the caller
     * @param target the target who should start playing
     * @param trackInfo the track to play
     * @param isUsingJava true if the player is using java
     * @return the optional StartMusicRequest
     */
    public static Optional<StartMusicRequest> createStartMusicRequest(Identification source, Identification target, TrackInfo trackInfo, boolean isUsingJava) {
        if (target.equals(source))
            return Optional.empty();
        try {
            StartMusicRequest request = new StartMusicRequest(source, isUsingJava);
            request.addResource(new SelectorResource(source, target));
            if (trackInfo != null)
                request.addResource(new TrackInfoResource(target, trackInfo, source));
            return Optional.of(request);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    /**
     * creates a new StartRequest
     * @param source the caller
     * @param target the target who should start playing
     * @param playlist the playlist to play
     * @return the optional StartMusicRequest
     */
    public static Optional<StartMusicRequest> createStartMusicRequest(Identification source, Identification target, Playlist playlist) {
        return createStartMusicRequest(source, target, playlist, false);
    }

    /**
     * creates a new StartRequest
     * @param source the caller
     * @param target the target who should start playing
     * @param playlist the playlist to play
     * @param isUsingJava true if the player is using java
     * @return the optional StartMusicRequest
     */
    public static Optional<StartMusicRequest> createStartMusicRequest(Identification source, Identification target, Playlist playlist, boolean isUsingJava) {
        if (target.equals(source))
            return Optional.empty();
        try {
            StartMusicRequest request = new StartMusicRequest(source, isUsingJava);
            request.addResource(new SelectorResource(source, target));
            if (playlist != null)
                request.addResource(new PlaylistResource(target, playlist, source));
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
        if (!PlaylistResource.getPlaylist(eventModel).map(playlist -> playlist.verify(capabilities)).orElse(true)) {
            return false;
        }
        return SelectorResource.isTarget(eventModel, player)
                .orElse(false);
    }
}
