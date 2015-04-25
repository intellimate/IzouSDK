package org.intellimate.izou.sdk.frameworks.common.resources;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.resource.Resource;

/**
 * A resource which holds an Identification used for example to select the Player
 * @author LeanderK
 * @version 1.0
 */
public class OutputPluginSelectorResource extends Resource<Identification> {
    public static final String RESOURCE_ID = "izou.common.resource.playerselecor";

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param consumer   the Provider of the Resource
     */
    public OutputPluginSelectorResource(Identification consumer) {
        super(RESOURCE_ID, null, null, consumer);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param consumer       the consumer of the Resource
     * @param identification the resource
     */
    public OutputPluginSelectorResource(Identification consumer, Identification identification) {
        super(RESOURCE_ID, identification, identification, consumer);
    }
}
