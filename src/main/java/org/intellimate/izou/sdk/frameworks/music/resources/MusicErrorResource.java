package org.intellimate.izou.sdk.frameworks.music.resources;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.resource.Resource;

/**
 * this class is used to hold the error-message
 * @author LeanderK
 * @version 1.0
 */
public class MusicErrorResource extends Resource<String> {
    public static final String ID = "izou.music.resource.error";

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param error          the error
     */
    public MusicErrorResource(Identification provider, String error) {
        super(ID, provider, error);
    }
}
