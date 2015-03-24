package intellimate.izou.sdk.util;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * provides various methods to simplify the interaction with the ThreadPool
 * @author Leander Kurscheidt
 * @version 1.0
 */
public interface ThreadPoolUser extends ContextProvider {
    /**
     * submits the Runnable to the AddOns Thread-Pool
     * @param runnable the runnable to submit
     * @return the new CompletableFuture
     */
    default CompletableFuture<Void> submit(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, getContext().getThreadPool().getThreadPool())
                .whenComplete((u, ex) -> {
                    if (ex != null) {
                        getContext().getThreadPool().handleThrowable(ex, runnable);
                    }
                });
    }

    /**
     * submits the Supplier to the AddOns Thread-Pool
     * @param supplier the supplier executed
     * @param <U> the return type
     * @return the new CompletableFuture
     */
    default <U> CompletableFuture<U> submit(Supplier<U> supplier) {
        return CompletableFuture.supplyAsync(supplier, getContext().getThreadPool().getThreadPool())
                .whenComplete((u, ex) -> {
                    if (ex != null) {
                        getContext().getThreadPool().handleThrowable(ex, supplier);
                    }
                });
    }

    /**
     * submits the Callable to the AddOns Thread-Pool
     * @param x the Callable to submit
     * @param <U> the type to return
     * @param <X> the Callable
     * @return an Future-Object
     */
    default <U, X extends AddOnModule & Callable<U>> Future<U>  submit(X x) {
        return getContext().getThreadPool().getThreadPool().submit(x);
    }

    /**
     * times out the collection of futures
     * @param futures the collection of futures
     * @param milliseconds the limit in milliseconds (everything under 20 milliseconds makes no sense)
     * @param <U> the return type of the futures
     * @param <V> the type of the futures
     * @return a List of futures
     * @throws InterruptedException if the process was interrupted
     */
    default <U, V extends Future<U>> List<V> timeOut(Collection<? extends V> futures,
                                                     int milliseconds) throws InterruptedException {
        //Timeout
        int start = 0;
        boolean notFinished = true;
        while ( (start < milliseconds) && notFinished) {
            notFinished = futures.stream()
                    .anyMatch(future -> !future.isDone());
            start = start + 10;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw e;
            }
        }
        //cancel all running tasks
        if(notFinished) {
            futures.stream()
                    .filter(future -> !future.isDone())
                    .peek(future -> error(future.toString()+ " timed out"))
                    .forEach(future -> future.cancel(true));
        }
        return futures.stream()
                .filter(Future::isDone)
                .collect(Collectors.<V>toList());
    }
}
