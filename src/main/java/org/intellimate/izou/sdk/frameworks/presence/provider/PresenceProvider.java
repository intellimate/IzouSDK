package org.intellimate.izou.sdk.frameworks.presence.provider;

import org.intellimate.izou.identification.Identifiable;

/**
 * util class to provide information about the type of presence this addon can guarantee.
 * @author LeanderK
 * @version 1.0
 */
public interface PresenceProvider extends Identifiable {
    /**
     * true if the addon can guarantee that the user is around, false if not
     * @return true if it can guarantee it, false if not
     */
    boolean isStrict();

    /**
     * gets the PresenceIndicatorLevel of the addon (mainly used for communication between presence-providing addons)
     * @return the Level
     */
    PresenceIndicatorLevel getLevel();

    /**
     * returns true if the user might be/is present
     * @return true if present
     */
    boolean isPresent();
}
