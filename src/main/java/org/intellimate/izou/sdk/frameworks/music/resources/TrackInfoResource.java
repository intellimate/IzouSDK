package org.intellimate.izou.sdk.frameworks.music.resources;

import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.frameworks.music.player.TrackInfo;
import org.intellimate.izou.sdk.resource.Resource;

import java.util.HashMap;
import java.util.Optional;

/**
 * A resource which holds a trackInfo for events, can also be obtained via the resourceManager (retruns the current)
 * @author LeanderK
 * @version 1.0
 */
public class TrackInfoResource extends Resource<HashMap<String, Object>> {
    @SuppressWarnings("SpellCheckingInspection")
    public static final String RESOURCE_ID = "izou.music.resource.trackinfo";

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
        super(RESOURCE_ID, provider, trackInfo.export());
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
        super(RESOURCE_ID, provider, trackInfo.export(), consumer);
    }

    /**
     * gets the first TrackInfo if found in the EventModel
     * @param eventModel the EventModel
     * @return return the optional TrackInfo
     */
    public static Optional<TrackInfo> getTrackInfo(EventModel eventModel) {
        if (eventModel.getListResourceContainer().containsResourcesFromSource(RESOURCE_ID)) {
            return eventModel
                    .getListResourceContainer()
                    .provideResource(RESOURCE_ID)
                    .stream()
                    .findAny()
                    .flatMap(TrackInfo::importFromResource);
        } else {
            return Optional.empty();
        }
    }
}
