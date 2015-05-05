package org.intellimate.izou.sdk.frameworks.presence.resources;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.frameworks.presence.provider.PresenceIndicatorLevel;
import org.intellimate.izou.sdk.resource.Resource;

/**
 * every addon which communicates presence should return this resource on request
 * @author LeanderK
 * @version 1.0
 */
public class PresenceProvider extends Resource<PresenceIndicatorLevel> {
    public static final String ID = "izou.presence.resources.presenceproviders";

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     */
    public PresenceProvider(Identification provider) {
        super(ID, provider);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider               the Provider of the Resource
     * @param presenceIndicatorLevel the resource
     */
    public PresenceProvider(Identification provider, PresenceIndicatorLevel presenceIndicatorLevel) {
        super(ID, provider, presenceIndicatorLevel);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param consumer   the ID of the Consumer
     */
    public PresenceProvider(Identification provider, Identification consumer) {
        super(ID, provider, consumer);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider               the Provider of the Resource
     * @param presenceIndicatorLevel the resource
     * @param consumer               the ID of the Consumer
     */
    public PresenceProvider(Identification provider, PresenceIndicatorLevel presenceIndicatorLevel, Identification consumer) {
        super(ID, provider, presenceIndicatorLevel, consumer);
    }
}
