package org.intellimate.izou.sdk.frameworks.music.resources;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.frameworks.music.player.Playlist;
import org.intellimate.izou.sdk.resource.Resource;

/**
 * @author LeanderK
 * @version 1.0
 */
public class PlaylistResource extends Resource<Playlist> {
    public static String ID = "izou.music.resource.resource.playlist";
    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     */
    public PlaylistResource(Identification provider) {
        super(ID, provider);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param playlist   the resource
     */
    public PlaylistResource(Identification provider, Playlist playlist) {
        super(ID, provider, playlist);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param consumer   the ID of the Consumer
     */
    public PlaylistResource(Identification provider, Identification consumer) {
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
    public PlaylistResource(Identification provider, Playlist playlist, Identification consumer) {
        super(ID, provider, playlist, consumer);
    }
}
