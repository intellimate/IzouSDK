package org.intellimate.izou.sdk.frameworks.presence.resources;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.resource.Resource;

import java.util.HashMap;

/**
 * every addon which communicates presence should return
 * @author LeanderK
 * @version 1.0
 */
public class PresenceProviders extends Resource<HashMap<Identification, >> {
    public static final String ID = "izou.presence.resources.presenceproviders";

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     */
    public PresenceProviders(Identification provider) {
        super(ID, provider);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param s          the resource
     */
    public PresenceProviders(Identification provider, String s) {
        super(ID, provider, s);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param consumer   the ID of the Consumer
     */
    public PresenceProviders(Identification provider, Identification consumer) {
        super(ID, provider, consumer);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param s          the resource
     * @param consumer   the ID of the Consumer
     */
    public PresenceProviders(Identification provider, String s, Identification consumer) {
        super(ID, provider, s, consumer);
    }
}
