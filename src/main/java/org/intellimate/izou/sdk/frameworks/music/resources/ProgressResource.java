package org.intellimate.izou.sdk.frameworks.music.resources;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.frameworks.music.player.Progress;
import org.intellimate.izou.sdk.resource.Resource;

/**
 * @author LeanderK
 * @version 1.0
 */
public class ProgressResource extends Resource<Progress> {
    public static final String ID = "izou.music.resource.progress";

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     */
    public ProgressResource(Identification provider) {
        super(ID, provider);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param progress   the resource
     */
    public ProgressResource(Identification provider, Progress progress) {
        super(ID, provider, progress);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param consumer   the ID of the Consumer
     */
    public ProgressResource(Identification provider, Identification consumer) {
        super(ID, provider, consumer);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param progress   the resource
     * @param consumer   the ID of the Consumer
     */
    public ProgressResource(Identification provider, Progress progress, Identification consumer) {
        super(ID, provider, progress, consumer);
    }
}
