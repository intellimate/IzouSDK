package org.intellimate.izou.sdk.addon;

import ro.fortsoft.pf4j.IzouPlugin;
import ro.fortsoft.pf4j.PluginWrapper;

/**
 * This class must be extended for an AddOn to work properly.
 * It is used to identify the zip Files as candidates for AddOns
 */
public abstract class ZipFileManager extends IzouPlugin {

    /**
     * Constructor to be used by plugin manager for plugin instantiation.
     * Your plugins have to provide constructor with this exact signature to
     * be successfully loaded by manager.
     *
     * @param wrapper    the PluginWrapper to assign the ZipFileManager to
     * @param sdkVersion the version of the izou sdk this plugin is using
     */
    public ZipFileManager(PluginWrapper wrapper, String sdkVersion) {
        super(wrapper, sdkVersion);
    }
}
