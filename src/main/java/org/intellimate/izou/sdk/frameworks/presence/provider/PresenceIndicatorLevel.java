package org.intellimate.izou.sdk.frameworks.presence.provider;

/**
 * contains some constants describing different levels of indication that the user is around.
 * Mainly used internally the following way:
 * for killing events: the weakest decides
 * for deciding presence: the strongest decides
 * @author LeanderK
 * @version 1.0
 */
public enum PresenceIndicatorLevel {
    STRONG, WEAK, VERY_WEAK
}
