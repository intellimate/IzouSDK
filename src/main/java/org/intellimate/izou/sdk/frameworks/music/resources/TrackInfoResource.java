package org.intellimate.izou.sdk.frameworks.music.resources;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.frameworks.music.player.TrackInfo;
import org.intellimate.izou.sdk.resource.Resource;

import java.util.Optional;

/**
 * A resource which holds a trackInfo
 * @author LeanderK
 * @version 1.0
 */
public class TrackInfoResource extends Resource<TrackInfo> {
    public static String RESOURCE_ID = "izou.music.trackinfo";

    public static Optional<TrackInfoResource> of (Resource<?> resource) {
        if (resource.getID().equals(RESOURCE_ID)) {
            try {
                TrackInfo resourceTrackInfo = (TrackInfo) resource.getResource();
                if (resourceTrackInfo != null) {
                    return Optional.of(new TrackInfoResource(resource.getProvider(),
                            resourceTrackInfo,
                            resource.getConsumer()));
                }
            } catch (ClassCastException ignored) {}
        }
        return Optional.empty();
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     */
    public TrackInfoResource() {
        super(RESOURCE_ID);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     */
    public TrackInfoResource(Identification provider) {
        super(RESOURCE_ID, provider);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param trackInfo  the resource
     */
    public TrackInfoResource(Identification provider, TrackInfo trackInfo) {
        super(RESOURCE_ID, provider, trackInfo);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param consumer   the ID of the Consumer
     */
    public TrackInfoResource(Identification provider, Identification consumer) {
        super(RESOURCE_ID, provider, consumer);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param trackInfo  the resource
     * @param consumer   the ID of the Consumer
     */
    public TrackInfoResource(Identification provider, TrackInfo trackInfo, Identification consumer) {
        super(RESOURCE_ID, provider, trackInfo, consumer);
    }
}
