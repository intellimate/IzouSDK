package org.intellimate.izou.sdk.frameworks.music.resources;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.frameworks.music.player.Volume;
import org.intellimate.izou.sdk.resource.Resource;

/**
 * Resource used to obtain the Volume
 * @author LeanderK
 * @version 1.0
 */
public class VolumeResource extends Resource<Volume> {
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
        super(ID, provider, volume);
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
        super(ID, provider, volume, consumer);
    }
}
