package org.intellimate.izou.sdk.frameworks.permanentSoundOutput.resource;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.resource.Resource;

/**
 * every outputplugin which is permanently using the sound output provides his indentification here
 * @author LeanderK
 * @version 1.0
 */
public class UsingSoundResource extends Resource<Identification> {
    public static final String ID = "izou.sound.resources.usingsound";
    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     */
    public UsingSoundResource(Identification provider) {
        super(ID, provider, provider, null);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider       the Provider of the Resource
     * @param consumer       the ID of the Consumer
     */
    public UsingSoundResource(Identification provider, Identification consumer) {
        super(ID, provider, provider, consumer);
    }
}
