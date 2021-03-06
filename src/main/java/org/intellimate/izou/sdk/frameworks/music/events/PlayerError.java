package org.intellimate.izou.sdk.frameworks.music.events;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.events.CommonEvents;
import org.intellimate.izou.sdk.events.Event;
import org.intellimate.izou.sdk.frameworks.music.resources.MusicErrorResource;

import java.util.Collections;
import java.util.Optional;

/**
 * @author LeanderK
 * @version 1.0
 */
public class PlayerError extends Event {
    public static final String ID = "izou.music.events.error";
    public static final String ERROR_ALREADY_PLAYING = "1. music-player is already playing";
    public static final String ERROR_NOT_ABLE = "2. music-player is not able to do: ";
    public static final String ERROR_ILLEGAL = "2. music-player is receiving an illegal command: ";

    /**
     * Creates a new Event Object
     *
     * @param source      the source of the Event, most likely a this reference.
     * @throws IllegalArgumentException if one of the Arguments is null or empty
     */
    protected PlayerError(Identification source) throws IllegalArgumentException {
        super(CommonEvents.Type.RESPONSE_TYPE, source, Collections.singletonList(ID));
    }

    /**
     * creates a new MusicPlayerError
     * @param source the source
     * @param error the error (not null and not empty)
     * @return the optional event
     */
    public static Optional<PlayerError> createMusicPlayerError(Identification source, String error) {
        if (error == null || error.isEmpty())
            return Optional.empty();
        try {
            PlayerError playerError = new PlayerError(source);
            playerError.addResource(new MusicErrorResource(source, error));
            return Optional.of(playerError);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
