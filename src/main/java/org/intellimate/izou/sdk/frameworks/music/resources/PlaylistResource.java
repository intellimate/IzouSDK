package org.intellimate.izou.sdk.frameworks.music.resources;

import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.resource.ResourceModel;
import org.intellimate.izou.sdk.frameworks.music.player.Playlist;
import org.intellimate.izou.sdk.resource.Resource;

import java.util.Optional;

/**
 * @author LeanderK
 * @version 1.0
 */
public class PlaylistResource extends Resource<Playlist> {
    public static String ID = "izou.music.resource.playlist";
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
                    .map(ResourceModel::getResource)
                    .filter(ob -> ob instanceof Playlist)
                    .map(ob -> (Playlist) ob)
                    .findAny();
        } else {
            return Optional.empty();
        }
    }
}
