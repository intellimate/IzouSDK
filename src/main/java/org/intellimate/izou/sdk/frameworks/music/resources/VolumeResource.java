package org.intellimate.izou.sdk.frameworks.music.resources;

import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.resource.ResourceModel;
import org.intellimate.izou.sdk.frameworks.music.player.Volume;
import org.intellimate.izou.sdk.resource.Resource;

import java.util.Optional;

/**
 * Resource used to obtain the Volume
 * @author LeanderK
 * @version 1.0
 */
public class VolumeResource extends Resource<Integer> {
    public static final String ID = "izou.music.resource.volume";

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     */
    public VolumeResource(Identification provider) {
        super(ID, provider);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param volume     the resource
     */
    public VolumeResource(Identification provider, Volume volume) {
        super(ID, provider, volume.getVolume());
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param consumer   the ID of the Consumer
     */
    public VolumeResource(Identification provider, Identification consumer) {
        super(ID, provider, consumer);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param volume     the resource
     * @param consumer   the ID of the Consumer
     */
    public VolumeResource(Identification provider, Volume volume, Identification consumer) {
        super(ID, provider, volume.getVolume(), consumer);
    }

    /**
     * gets the first Volume if found in the EventModel
     * @param eventModel the EventModel
     * @return return the optional Volume
     */
    public static Optional<Volume> getVolume(EventModel eventModel) {
        if (eventModel.getListResourceContainer().containsResourcesFromSource(ID)) {
            return eventModel
                    .getListResourceContainer()
                    .provideResource(ID)
                    .stream()
                    .map(ResourceModel::getResource)
                    .filter(ob -> ob instanceof Integer)
                    .map(ob -> (Integer) ob)
                    .map(Volume::createVolume)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findAny();
        } else {
            return Optional.empty();
        }
    }
}
