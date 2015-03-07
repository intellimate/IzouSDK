package intellimate.izou.sdk.util;

/**
 * This interface has various utility-methods for Logging
 * @author Leander Kurscheidt
 * @version 1.0
 */
public interface Loggable extends ContextProvider {
    /**
     * Used to log messages at debug level
     * @param msg the message
     * @param e the Throwable
     */
    default void debug(String msg, Throwable e) {
        getContext().getLogger().debug(msg, e);
    }

    /**
     * Used to log messages at debug level
     * @param msg the message
     */
    default void debug(String msg) {
        getContext().getLogger().debug(msg);
    }

    /**
     * Used to log messages at error level
     * @param msg the message
     * @param e the Throwable
     */
    default void error(String msg, Throwable e) {
        getContext().getLogger().error(msg, e);
    }

    /**
     * Used to log messages at error level
     * @param msg the message
     */
    default void error(String msg) {
        getContext().getLogger().error(msg);
    }
}
