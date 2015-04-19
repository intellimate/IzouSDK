package org.intellimate.izou.sdk.frameworks.music.events;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.events.CommonEvents;
import org.intellimate.izou.sdk.events.Event;
import org.intellimate.izou.sdk.frameworks.music.resources.MusicErrorResource;
import org.intellimate.izou.sdk.util.AddOnModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author LeanderK
 * @version 1.0
 */
public class MusicPlayerError extends Event {
    public static final String ID = "izou.music.events.error";

    /**
     * Creates a new Event Object
     *
     * @param source      the source of the Event, most likely a this reference.
     * @throws IllegalArgumentException if one of the Arguments is null or empty
     */
    protected MusicPlayerError(AddOnModule addOnModule, Identification source) throws IllegalArgumentException {
        super(CommonEvents.get(addOnModule).getType().responseType(), source, new ArrayList<>(Arrays.asList(ID)));
    }

    /**
     * creates a new MusicPlayerError
     * @param addOnModule the module which creates the Event
     * @param source the source
     * @param error the error (not null and not empty)
     * @return the optional event
     */
    public static Optional<MusicPlayerError> createMusicPlayerError(AddOnModule addOnModule, Identification source, String error) {
        if (error == null || error.isEmpty())
            return Optional.empty();
        try {
            MusicPlayerError musicPlayerError = new MusicPlayerError(addOnModule, source);
            musicPlayerError.addResource(new MusicErrorResource(source, error));
            return Optional.of(musicPlayerError);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
