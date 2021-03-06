package org.intellimate.izou.sdk.addon;

import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;

/**
 * This class must be extended for an AddOn to work properly.
 * It is used to identify the zip Files as candidates for AddOns
 */
@SuppressWarnings("WeakerAccess")
public abstract class ZipFileManager extends Plugin {

    /**
     * The {@link PluginWrapper} for this addOn.
     *
     * @param wrapper The {@link PluginWrapper} for this addOn.
     */
    public ZipFileManager(PluginWrapper wrapper) {
        super(wrapper);
    }
}
