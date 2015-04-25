package org.intellimate.izou.sdk.frameworks.music.resources;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.resource.Resource;

/**
 * A resource which holds an Identification used obtain the current playing OP
 * @author LeanderK
 * @version 1.0
 */
public class PlayerResource extends Resource<Identification> {
    public static final String RESOURCE_ID = "izou.music.resource.playerselecor";

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param consumer   the Provider of the Resource
     */
    public PlayerResource(Identification consumer) {
        super(RESOURCE_ID, null, null, consumer);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param consumer       the consumer of the Resource
     * @param identification the resource
     */
    public PlayerResource(Identification consumer, Identification identification) {
        super(RESOURCE_ID, identification, identification, consumer);
    }
}
