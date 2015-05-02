package org.intellimate.izou.sdk.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.spi.AbstractLogger;

/**
 * This interface has various utility-methods for Logging
 * @author Leander Kurscheidt
 * @version 1.0
 */
public interface Loggable extends ContextProvider {
    String FQCN = Loggable.class.getName();
    /**
     * Used to log messages at debug level
     * @param msg the message
     * @param e the Throwable
     */
    default void debug(String msg, Throwable e) {
        AbstractLogger logger = (AbstractLogger) getContext().getLogger();
        logger.logIfEnabled(FQCN, Level.DEBUG, null, msg, e);
    }

    /**
     * Used to log messages at debug level
     * @param msg the message
     */
    default void debug(String msg) {
        AbstractLogger logger = (AbstractLogger) getContext().getLogger();
        logger.logIfEnabled(FQCN, Level.DEBUG, null, msg, (Object) null);
    }

    /**
     * Used to log messages at error level
     * @param msg the message
     * @param e the Throwable
     */
    default void error(String msg, Throwable e) {
        AbstractLogger logger = (AbstractLogger) getContext().getLogger();
        logger.logIfEnabled(FQCN, Level.ERROR, null, msg, e);
    }

    /**
     * Used to log messages at error level
     * @param msg the message
     */
    default void error(String msg) {
        AbstractLogger logger = (AbstractLogger) getContext().getLogger();
        logger.logIfEnabled(FQCN, Level.ERROR, null, msg, (Object) null);
    }

    /**
     * Used to log messages at fatal level
     * @param msg the message
     * @param e the Throwable
     */
    default void fatal(String msg, Throwable e) {
        AbstractLogger logger = (AbstractLogger) getContext().getLogger();
        logger.logIfEnabled(FQCN, Level.FATAL, null, msg, e);
    }

    /**
     * Used to log messages at fatal level
     * @param msg the message
     */
    default void fatal(String msg) {
        AbstractLogger logger = (AbstractLogger) getContext().getLogger();
        logger.logIfEnabled(FQCN, Level.FATAL, null, msg, (Object) null);
    }

    /**
     * Used to log messages at warn level
     * @param msg the message
     * @param e the Throwable
     */
    default void warn(String msg, Throwable e) {
        AbstractLogger logger = (AbstractLogger) getContext().getLogger();
        logger.logIfEnabled(FQCN, Level.WARN, null, msg, e);
    }

    /**
     * Used to log messages at warn level
     * @param msg the message
     */
    default void warn(String msg) {
        AbstractLogger logger = (AbstractLogger) getContext().getLogger();
        logger.logIfEnabled(FQCN, Level.WARN, null, msg, (Object) null);
    }
}
