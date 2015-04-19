package org.intellimate.izou.sdk.frameworks.music.events;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.events.CommonEvents;
import org.intellimate.izou.sdk.events.Event;
import org.intellimate.izou.sdk.frameworks.music.player.TrackInfo;
import org.intellimate.izou.sdk.frameworks.music.resources.PlayerSelectorResource;
import org.intellimate.izou.sdk.frameworks.music.resources.TrackInfoResource;
import org.intellimate.izou.sdk.util.AddOnModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * this events request the start of the music-playing
 * @author LeanderK
 * @version 1.0
 */
public class StartMusicRequest extends Event {
    public static final String ID = "izou.music.events.startrequest";

    /**
     * Creates a new Event Object
     *
     * @param source      the source of the Event, most likely a this reference.
     * @throws IllegalArgumentException if one of the Arguments is null or empty
     */
    protected StartMusicRequest(AddOnModule addOnModule, Identification source)
                                                                        throws IllegalArgumentException {
        super(CommonEvents.get(addOnModule).getType().responseType(), source, new ArrayList<>(Arrays.asList(ID)));
    }

    /**
     * creates a new StartRequest
     * @param addOnModule a reference to the module which created this Event
     * @param source the caller
     * @param target the target who should start playing
     * @return the optional StartMusicRequest
     */
    public static Optional<StartMusicRequest> createStartMusicRequest(AddOnModule addOnModule, Identification source, Identification target) {
        if (target == null || target.equals(source))
            return Optional.empty();
        try {
            StartMusicRequest request = new StartMusicRequest(addOnModule, source);
            request.addResource(new PlayerSelectorResource(source, target));
            return Optional.of(request);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    /**
     * creates a new StartRequest
     * @param addOnModule a reference to the module which created this Event
     * @param source the caller
     * @param target the target who should start playing
     * @param trackInfo the track to play
     * @return the optional StartMusicRequest
     */
    public static Optional<StartMusicRequest> createStartMusicRequest(AddOnModule addOnModule, Identification source, Identification target, TrackInfo trackInfo) {
        if (target.equals(source))
            return Optional.empty();
        try {
            StartMusicRequest request = new StartMusicRequest(addOnModule, source);
            request.addResource(new PlayerSelectorResource(source, target));
            request.addResource(new TrackInfoResource(target, trackInfo, source));
            return Optional.of(request);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
