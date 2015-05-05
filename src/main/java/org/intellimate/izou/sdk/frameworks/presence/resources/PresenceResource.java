package org.intellimate.izou.sdk.frameworks.presence.resources;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.frameworks.presence.provider.Presence;
import org.intellimate.izou.sdk.resource.Resource;

import java.util.HashMap;

/**
 * returns true if the user might be around.
 * Please note: this resource is an educated guess and does not guarantee presence.
 * Addons that are only indicating presence (for example through scanning wifi) should not respond if some stronger
 * indicators are present.
 * @author LeanderK
 * @version 1.0
 */
public class PresenceResource extends Resource<HashMap<String, Object>> {
    public static final String ID = "izou.presence.resources.presence";

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     */
    public PresenceResource(Identification provider) {
        super(ID, provider);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param presence   the resource
     */
    public PresenceResource(Identification provider, Presence presence) {
        super(ID, provider, presence.export());
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param consumer   the ID of the Consumer
     */
    public PresenceResource(Identification provider, Identification consumer) {
        super(ID, provider, consumer);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param presence   the resource
     * @param consumer   the ID of the Consumer
     */
    public PresenceResource(Identification provider, Presence presence, Identification consumer) {
        super(ID, provider, presence.export(), consumer);
    }
}
