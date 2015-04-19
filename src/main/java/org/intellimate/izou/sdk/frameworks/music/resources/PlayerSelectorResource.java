package org.intellimate.izou.sdk.frameworks.music.resources;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.resource.Resource;

/**
 * A resource which holds an Identification used for example to select the Player
 * @author LeanderK
 * @version 1.0
 */
public class PlayerSelectorResource extends Resource<Identification> {
    public static String RESOURCE_ID = "izou.music.resource.playerselecor";

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     */
    public PlayerSelectorResource() {
        super(RESOURCE_ID);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     */
    public PlayerSelectorResource(Identification provider) {
        super(RESOURCE_ID, provider);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider       the Provider of the Resource
     * @param identification the resource
     */
    public PlayerSelectorResource(Identification provider, Identification identification) {
        super(RESOURCE_ID, provider, identification, null);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param identification the resource
     * @param consumer       the ID of the Consumer
     */
    public PlayerSelectorResource(Identification provider, Identification identification, Identification consumer) {
        super(RESOURCE_ID, provider, identification, consumer);
    }
}
