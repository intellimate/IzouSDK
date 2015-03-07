package intellimate.izou.sdk.util;

/**
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

    }

    /**
     * Used to log messages at debug level
     * @param msg the message
     */
    void debug(String msg);

    /**
     * Used to log messages at error level
     * @param msg the message
     * @param e the Throwable
     */
    void error(String msg, Throwable e);

    /**
     * Used to log messages at error level
     * @param msg the message
     */
    void error(String msg);
}
