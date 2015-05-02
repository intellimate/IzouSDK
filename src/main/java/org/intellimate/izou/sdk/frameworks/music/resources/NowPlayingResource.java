package org.intellimate.izou.sdk.frameworks.music.resources;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.frameworks.music.player.Playlist;
import org.intellimate.izou.sdk.resource.Resource;

/**
 * returns the current played playlist
 * @author LeanderK
 * @version 1.0
 */
public class NowPlayingResource extends Resource<Playlist> {
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
        super(ID, provider, playlist);
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
        super(ID, provider, playlist, consumer);
    }
}
