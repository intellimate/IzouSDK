package org.intellimate.izou.sdk.specification.context;

import org.intellimate.izou.identification.IllegalIDException;
import org.intellimate.izou.sdk.specification.ContentGeneratorModel;

/**
 * @author Leander Kurscheidt
 * @version 1.0
 */
public interface ContentGenerators {
    /**
     * Register an ContentGenerator
     *
     * @param contentGenerator the contentGenerator to register
     * @throws intellimate.izou.identification.IllegalIDException not implemented yet
     */
    void registerContentGenerator(ContentGeneratorModel contentGenerator)
            throws IllegalIDException;

    /**
     * unregisters an ContentGenerator
     * @param contentGenerator the ContentGenerator to unregister
     */
    void unregisterContentGenerator(ContentGeneratorModel contentGenerator);
}
