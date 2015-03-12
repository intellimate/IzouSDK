package intellimate.izou.sdk.specification.context;

import intellimate.izou.identification.IllegalIDException;

/**
 * @author Leander Kurscheidt
 * @version 1.0
 */
public interface ContentGenerator {
    /**
     * register an ContentGenerator
     * @param contentGenerator the contentGenerator to register
     * @throws intellimate.izou.identification.IllegalIDException not implemented yet
     */
    void registerContentGenerator(intellimate.izou.sdk.specification.ContentGenerator contentGenerator) throws IllegalIDException;

    /**
     * unregisters an ContentGenerator
     * @param contentGenerator the ContentGenerator to unregister
     */
    void unregisterContentGenerator(intellimate.izou.sdk.specification.ContentGenerator contentGenerator);
}
