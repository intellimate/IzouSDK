package org.intellimate.izou.sdk.frameworks.music.resources;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.frameworks.music.player.Playlist;
import org.intellimate.izou.sdk.resource.Resource;

import java.util.HashMap;

/**
 * use this method obtain a specified Playlist.
 * Internally it keeps the PlaylistData in the HashMap when the request happens, the key is the ResourceID.
 * @author LeanderK
 * @version 1.0
 */
public class BroadcasterPlaylist extends Resource<HashMap<String, Object>> {
    @SuppressWarnings("SpellCheckingInspection")
    public static final String RESOURCE_ID = "izou.music.resource.broadcaster.playlist";

    /**
     * creates a new Resource.
     *
     * @param provider   the Provider of the Resource
     */
    public BroadcasterPlaylist(Identification provider) {
        super(RESOURCE_ID, provider);
    }

    /**
     * creates a new Resource.
     *
     * @param provider   the Provider of the Resource
     * @param hashMap    the resource
     */
    protected BroadcasterPlaylist(Identification provider, HashMap<String, Object> hashMap) {
        super(RESOURCE_ID, provider, hashMap);
    }

    /**
     * creates a new Resource.
     *
     * @param consumer   the ID of the Consumer
     * @param hashMap    the resource
     */
    protected BroadcasterPlaylist(HashMap<String, Object> hashMap, Identification consumer) {
        super(RESOURCE_ID, hashMap, consumer);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param consumer   the ID of the Consumer
     */
    protected BroadcasterPlaylist(Identification provider, Identification consumer) {
        super(RESOURCE_ID, provider, consumer);
    }

    /**
     * creates a new Resource.
     *
     * @param provider   the Provider of the Resource
     * @param hashMap    the resource
     * @param consumer   the ID of the Consumer
     */
    protected BroadcasterPlaylist(Identification provider, HashMap<String, Object> hashMap, Identification consumer) {
        super(RESOURCE_ID, provider, hashMap, consumer);
    }

    /**
     * creates the Playlist-Request
     * @param consumer the consumer who wants to ask for the playlist
     * @param playlistName the name of the playlist
     * @return the resource
     */
    public static BroadcasterPlaylist createPlaylistRequest(Identification consumer, String playlistName) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(RESOURCE_ID, playlistName);
        return new BroadcasterPlaylist(hashMap, consumer);
    }

    /**
     * creates the Playlist-Answer
     * @param provider the provider
     * @param playlist the playlist
     * @return the resource
     */
    public static BroadcasterPlaylist createPlaylistAnswer(Identification provider, Playlist playlist) {
        return new BroadcasterPlaylist(provider, playlist.export());
    }
}
