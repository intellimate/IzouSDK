package org.intellimate.izou.sdk.frameworks.music.resources;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.frameworks.music.Capabilities;
import org.intellimate.izou.sdk.resource.Resource;

import java.util.HashMap;

/**
 * @author LeanderK
 * @version 1.0
 */
public class CapabilitesResource extends Resource<HashMap<String, Boolean>> {
    public static final String RESOURCE_ID = "izou.music.capabilitesresource";

    /**
     * creates a new Resource.
     * This method is thread-safe.
     */
    public CapabilitesResource() {
        super(RESOURCE_ID);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     */
    public CapabilitesResource(Identification provider) {
        super(RESOURCE_ID, provider);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider             the Provider of the Resource
     * @param capabilities the capabilities
     */
    public CapabilitesResource(Identification provider, Capabilities capabilities) {
        super(RESOURCE_ID, provider, capabilities.write());
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param consumer   the ID of the Consumer
     */
    public CapabilitesResource(Identification provider, Identification consumer) {
        super(RESOURCE_ID, provider, consumer);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider             the Provider of the Resource
     * @param capabilities the capabilities
     * @param consumer             the ID of the Consumer
     */
    public CapabilitesResource(Identification provider, Capabilities capabilities, Identification consumer) {
        super(RESOURCE_ID, provider, capabilities.write(), consumer);
    }
}
