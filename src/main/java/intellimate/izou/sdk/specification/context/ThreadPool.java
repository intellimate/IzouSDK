package intellimate.izou.sdk.specification.context;

import java.util.concurrent.ExecutorService;

/**
 * @author Leander Kurscheidt
 * @version 1.0
 */
public interface ThreadPool extends intellimate.izou.system.context.ThreadPool {
    /**
     * returns a ThreadPool associated with the AddOn
     *
     * @return an instance of ExecutorService or Null if there is a problem with the Identifier
     */
    ExecutorService getThreadPool();
}
