package org.intellimate.izou.sdk.frameworks.music.resources;

import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.frameworks.music.player.Playlist;
import org.intellimate.izou.sdk.resource.Resource;

import java.util.HashMap;
import java.util.Optional;

/**
 * returns the current played playlist if asked via the resourceManager
 * @author LeanderK
 * @version 1.0
 */
@SuppressWarnings("unused")
public class NowPlayingResource extends Resource<HashMap<String, Object>> {
    @SuppressWarnings("SpellCheckingInspection")
    public static final String ID = "izou.music.resource.nowplaying";

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     */
    public NowPlayingResource(Identification provider) {
        super(ID, provider);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param playlist   the resource
     */
    public NowPlayingResource(Identification provider, Playlist playlist) {
        super(ID, provider, playlist.export());
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param consumer   the ID of the Consumer
     */
    public NowPlayingResource(Identification provider, Identification consumer) {
        super(ID, provider, consumer);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param playlist   the resource
     * @param consumer   the ID of the Consumer
     */
    public NowPlayingResource(Identification provider, Playlist playlist, Identification consumer) {
        super(ID, provider, playlist.export(), consumer);
    }

    /**
     * gets the first playlist if found in the EventModel
     * @param eventModel the EventModel
     * @return return the optional Playlist
     */
    public static Optional<Playlist> getPlaylist(EventModel eventModel) {
        if (eventModel.getListResourceContainer().containsResourcesFromSource(ID)) {
            return eventModel
                    .getListResourceContainer()
                    .provideResource(ID)
                    .stream()
                    .map(Playlist::importResource)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findAny();
        } else {
            return Optional.empty();
        }
    }
}
