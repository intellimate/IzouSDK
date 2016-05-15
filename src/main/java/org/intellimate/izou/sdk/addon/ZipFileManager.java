package org.intellimate.izou.sdk.addon;

import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginException;
import ro.fortsoft.pf4j.PluginWrapper;

/**
 * This class must be extended for an AddOn to work properly.
 * It is used to identify the zip Files as candidates for AddOns
 */
@SuppressWarnings("WeakerAccess")
public abstract class ZipFileManager implements Plugin {
    /**
     * Wrapper of the plugin.
     */
    private PluginWrapper wrapper;

    /**
     * Constructor to be used by plugin manager for plugin instantiation.
     * Your plugins have to provide constructor with this exact signature to
     * be successfully loaded by manager.
     *
     * @param wrapper the PluginWrapper to assign the ZipFileManager to
     */
    public ZipFileManager(PluginWrapper wrapper) {
        if (wrapper == null) {
            throw new IllegalArgumentException("Wrapper cannot be null");
        }

        this.wrapper = wrapper;
    }

    /**
     * Retrieves the wrapper of this plug-in.
     */
    @Override
    public final PluginWrapper getWrapper() {
        return wrapper;
    }

    /**
     * Start method is called by the application when the plugin is loaded.
     */
    @Override
    public void start() throws PluginException {
    }

    /**
     * Stop method is called by the application when the plugin is unloaded.
     */
    @Override
    public void stop() throws PluginException {
    }
}
