package org.intellimate.izou.sdk.frameworks.music.events;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.events.CommonEvents;
import org.intellimate.izou.sdk.events.Event;
import org.intellimate.izou.sdk.frameworks.music.player.Playlist;
import org.intellimate.izou.sdk.frameworks.music.player.TrackInfo;
import org.intellimate.izou.sdk.frameworks.music.player.Volume;
import org.intellimate.izou.sdk.frameworks.music.resources.PlaylistResource;
import org.intellimate.izou.sdk.frameworks.music.resources.TrackInfoResource;
import org.intellimate.izou.sdk.frameworks.music.resources.VolumeResource;

import java.util.Collections;
import java.util.Optional;

/**
 * @author LeanderK
 * @version 1.0
 */
public class PlayerUpdate extends Event {
    @SuppressWarnings("SpellCheckingInspection")
    public static final String ID = "izou.music.events.playerupdate";
    /**
     * Creates a new Event Object
     *
     * @param source      the source of the Event, most likely a this reference.
     * @throws IllegalArgumentException if one of the Arguments is null or empty
     */
    protected PlayerUpdate(Identification source) throws IllegalArgumentException {
        super(CommonEvents.Type.RESPONSE_TYPE, source, Collections.singletonList(ID));
    }

    /**
     * Creates a new Event Object
     *
     * @param source      the source of the Event, most likely a this reference.
     * @return the optional PlayerUpdate (if no illegal arguments got passed)
     * @throws IllegalArgumentException if one of the Arguments is null or empty
     */
    public static Optional<PlayerUpdate> createPlayerUpdate(Identification source) {
        try {
            PlayerUpdate playerUpdate = new PlayerUpdate(source);
            return Optional.of(playerUpdate);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    /**
     * Creates a new Event Object
     *
     * @param source      the source of the Event, most likely a this reference.
     * @param volume the current volume, not null
     * @return the optional PlayerUpdate (if no illegal arguments got passed)
     * @throws IllegalArgumentException if one of the Arguments is null or empty
     */
    public static Optional<PlayerUpdate> createPlayerUpdate(Identification source, Volume volume) {
        if (volume == null)
            return Optional.empty();
        try {
            PlayerUpdate playerUpdate = new PlayerUpdate(source);
            playerUpdate.addResource(new VolumeResource(source, volume));
            return Optional.of(playerUpdate);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    /**
     * Creates a new Event Object
     *
     * @param source      the source of the Event, most likely a this reference.
     * @param playlist the current playing tracks
     * @return the optional PlayerUpdate (if no illegal arguments got passed)
     * @throws IllegalArgumentException if one of the Arguments is null or empty
     */
    public static Optional<PlayerUpdate> createPlayerUpdate(Identification source, Playlist playlist) {
        if (playlist == null)
            return Optional.empty();
        try {
            PlayerUpdate playerUpdate = new PlayerUpdate(source);
            playerUpdate.addResource(new PlaylistResource(source, playlist));
            return Optional.of(playerUpdate);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    /**
     * Creates a new Event Object
     *
     * @param source      the source of the Event, most likely a this reference.
     * @param trackInfo the current song
     * @return the optional PlayerUpdate (if no illegal arguments got passed)
     * @throws IllegalArgumentException if one of the Arguments is null or empty
     */
    public static Optional<PlayerUpdate> createPlayerUpdate(Identification source, TrackInfo trackInfo) {
        if (trackInfo == null)
            return Optional.empty();
        try {
            PlayerUpdate playerUpdate = new PlayerUpdate(source);
            playerUpdate.addResource(new TrackInfoResource(source, trackInfo));
            return Optional.of(playerUpdate);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
