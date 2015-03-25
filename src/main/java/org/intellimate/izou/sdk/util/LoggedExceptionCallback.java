package org.intellimate.izou.sdk.util;

import org.intellimate.izou.threadpool.ExceptionCallback;

/**
 * This interface logs when its crashing inside the Izou-ThreadPool
 * @author Leander Kurscheidt
 * @version 1.0
 */
public interface LoggedExceptionCallback extends ExceptionCallback, ContextProvider {
    @Override
    default void exceptionThrown(Exception e) {
        getContext().getLogger().fatal("Module: " + this.getClass().getName() + " crashed", e);
    }
}
