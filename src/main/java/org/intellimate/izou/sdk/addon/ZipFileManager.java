package org.intellimate.izou.sdk.addon;

import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;

/**
 * This class must be extended for an AddOn to work properly.
 * It is used to identify the zip Files as candidates for AddOns
 */
public abstract class ZipFileManager extends Plugin {
    public ZipFileManager(PluginWrapper wrapper) {
        super(wrapper);
    }
}
