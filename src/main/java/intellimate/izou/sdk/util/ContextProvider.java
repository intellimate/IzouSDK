package intellimate.izou.sdk.util;

import intellimate.izou.sdk.Context;

/**
 * This interface signals that this class returns a Context
 * @author Leander Kurscheidt
 * @version 1.0
 */
public interface ContextProvider {
    /**
     * returns the instance of Context
     * @return the instance of Context
     */
    public Context getContext();

    /**
     * Used to log messages at debug level
     * @param msg the message
     * @param e the Throwable
     */
    void debug(String msg, Throwable e);

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
