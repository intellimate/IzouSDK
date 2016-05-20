package org.intellimate.izou.sdk;

import org.intellimate.izou.sdk.addon.ZipFileManager;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.PluginWrapper;

/**
 * the ZipFileManager-Implementation used for the distribution
 * @author LeanderK
 * @version 1.0
 */
@Extension
public class ZipFileManagerImpl extends ZipFileManager {
    /**
     * Constructor to be used by plugin manager for plugin instantiation.
     * Your plugins have to provide constructor with this exact signature to
     * be successfully loaded by manager.
     *
     * @param wrapper the PluginWrapper to assign the ZipFileManager to
     */
    public ZipFileManagerImpl(PluginWrapper wrapper) {
        super(wrapper);
    }
}
