package org.intellimate.izou.sdk.frameworks.music.resources;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.resource.ResourceModel;
import org.intellimate.izou.sdk.frameworks.music.player.PlaybackState;
import org.intellimate.izou.sdk.resource.Resource;

import java.util.Optional;

/**
 * @author LeanderK
 * @version 1.0
 */
public class PlaybackStateResource extends Resource<String> {
    @SuppressWarnings("SpellCheckingInspection")
    public static final String ID = "izou.music.resource.playbackstate";

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     */
    public PlaybackStateResource(Identification provider) {
        super(ID, provider);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param state      the PlaybackState
     */
    public PlaybackStateResource(Identification provider, PlaybackState state) {
        super(ID, provider, state.name());
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param consumer   the ID of the Consumer
     */
    public PlaybackStateResource(Identification provider, Identification consumer) {
        super(ID, provider, consumer);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param state      the PlaybackState
     * @param consumer   the ID of the Consumer
     */
    public PlaybackStateResource(Identification provider, PlaybackState state, Identification consumer) {
        super(ID, provider, state.name(), consumer);
    }

    /**
     * returns the PlaybackState from the resource
     * @param x the resourceModel
     * @return the optional Playbackstate (empty if illegal resource)
     */
    public static Optional<PlaybackState> getPlaybackStateFromResource(ResourceModel x) {
        if (!x.getResourceID().equals(ID))
            return Optional.empty();
        Object resource = x.getResource();
        if (resource instanceof String) {
            String state = (String) resource;
            try {
                return Optional.of(PlaybackState.valueOf(state));
            } catch (IllegalArgumentException e) {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }
}
