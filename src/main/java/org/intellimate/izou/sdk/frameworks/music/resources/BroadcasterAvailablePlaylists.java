package org.intellimate.izou.sdk.frameworks.music.resources;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.resource.ResourceModel;
import org.intellimate.izou.sdk.resource.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * If the player is able to broadcast the available playlists, this resource can be used to them.
 * It returns a String which contains the names of the all the available playlists.
 * @author LeanderK
 * @version 1.0
 */
public class BroadcasterAvailablePlaylists extends Resource<List<String>> {
    @SuppressWarnings("SpellCheckingInspection")
    public static final String RESOURCE_ID = "izou.music.resource.broadcaster.availableplaylists";

    /**
     * creates a new Resource.
     * This method is thread-safe.
     */
    public BroadcasterAvailablePlaylists() {
        super(RESOURCE_ID);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     */
    public BroadcasterAvailablePlaylists(Identification provider) {
        super(RESOURCE_ID, provider);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param strings    the resource  (will get copied)
     */
    public BroadcasterAvailablePlaylists(Identification provider, List<String> strings) {
        super(RESOURCE_ID, provider, new ArrayList<String>(strings));
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param consumer   the ID of the Consumer
     */
    public BroadcasterAvailablePlaylists(Identification provider, Identification consumer) {
        super(RESOURCE_ID, provider, consumer);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param strings    the resource (will get copied)
     * @param consumer   the ID of the Consumer
     */
    public BroadcasterAvailablePlaylists(Identification provider, List<String> strings, Identification consumer) {
        super(RESOURCE_ID, provider, new ArrayList<String>(strings), consumer);
    }

    public static Optional<List<String>> getPlaylists(ResourceModel resourceModel) {
        Object resource = resourceModel.getResource();
        try {
            @SuppressWarnings("unchecked")
            List<String> playlists = (List<String>) resource;
            return Optional.of(playlists);
        } catch (ClassCastException e) {
            return Optional.empty();
        }
    }
}
