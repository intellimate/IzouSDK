package org.intellimate.izou.sdk.output;

import com.google.common.reflect.TypeToken;
import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.output.OutputPluginModel;
import org.intellimate.izou.resource.ResourceModel;
import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.util.AddOnModule;
import org.intellimate.izou.sdk.util.ThreadPoolUser;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * The OutputPlugin class gets Event and then starts threads filled with output-extension tasks to create the final
 * output and then render it on its own medium
 * @param <T> the type of the Argument
 * @param <X> the return type
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class OutputPluginArgument<T, X> extends AddOnModule implements OutputPluginModel<T, X>, ThreadPoolUser {

    /**
     * here are the events stored before they get processed
     */
    private final BlockingQueue<EventModel> eventBlockingQueue = new LinkedBlockingDeque<>();
    /**
     * the type argument for the Data you want to receive
     */
    private final TypeToken<X> receivingTypeToken;
    /**
     * the type argument for the Data you want to give the OutputExtensions as an argument
     */
    private final TypeToken<T> argumentTypeToken;

    /**
     * setting this boolean to true stops the while-loop
     */
    private boolean stop = false;

    /**
     * signals whether the OutputPlugin is working
     */
    private boolean isWorking = false;

    /**
     * creates a new output-plugin with a new id
     *
     * @param context context
     * @param id the id of the new output-plugin
     */
    public OutputPluginArgument(Context context, String id) {
        super(context, id);
        this.receivingTypeToken = new TypeToken<X>(getClass()) {};
        this.argumentTypeToken = new TypeToken<T>(getClass()) {};
    }

    /**
     * get the outputExtensionList
     *
     * @return gets the list of output-extensions in the output-plugin
     */
    public List<Identification> getOutputExtensionList() {
        return getContext().getOutput().getAssociatedOutputExtension(this);
    }

    /**
     * gets the blocking-queue that stores the backlog of Events
     *
     * @return blocking-queue that stores Events
     */
    public BlockingQueue<EventModel> getEventBlockingQueue() {
        return eventBlockingQueue;
    }

    /**
     * callback method to notify that an OutputExtension was added
     *
     * @param identification the Identification of the OutputExtension added
     */
    @Override
    public void outputExtensionAdded(Identification identification) {}

    /**
     * callback method to notify that an OutputExtension was added
     *
     * @param identification the Identification of the OutputExtension added
     */
    @Override
    public void outputExtensionRemoved(Identification identification) {}

    /**
     * returns the Type of the one wants to receive from the OutputExtensions
     *
     * @return the type of the generic
     */
    @Override
    public TypeToken<X> getReceivingType() {
        return receivingTypeToken;
    }

    /**
     * returns the Type of the argument for the OutputExtensions, or null if none
     *
     * @return the type of the Argument
     */
    @Override
    public TypeToken<T> getArgumentType() {
        return argumentTypeToken;
    }

    /**
     * Adds an event to blockingQueue
     *
     * @param event the event to add
     * @throws IllegalStateException raised if problems adding an event to blockingQueue
     */
    @Override
    public void addToEventList(EventModel event) {
        eventBlockingQueue.add(event);
    }

    @Override
    public boolean isRunning() {
        return isWorking;
    }

    @Override
    public void stop() {
        Thread.currentThread().interrupt();
    }

    /**
     * @param event the current processed Event
     */
    public void isDone(EventModel event) {
        Optional<ResourceModel> resource = event.getListResourceContainer().provideResource(getID()).stream()
                .filter(resourceS -> resourceS.getProvider().getID()
                        .equals(getContext().getOutput().getManagerIdentification().getID()))
                .findFirst();
        if(!resource.isPresent()) return;
        if(resource.get().getResource() instanceof Consumer) {
            Consumer consumer = (Consumer) resource.get().getResource();
            consumer.accept(null);
        }
    }

    /**
     * terminates the outputPlugin (will not listen to events anymore)
     */
    public void terminate() {
        stop = true;
        eventBlockingQueue.notify();
    }

    public void handleFutures(List<CompletableFuture<X>> futures) {
        List<X> result = futures.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException e) {
                        getContext().getLogger().error("interrupted", e);
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        getContext().getLogger().error("future finished exceptionally", e);
                        return null;
                    }
                })
                .collect(Collectors.toList());
        renderFinalOutput(result);
    }

    /**
     * main method for outputPlugin, runs the data-conversion and output-renderer
     *
     * when the outputExtensions are done processing the Event object, they add their finished objects into tDoneList,
     * from where they will have to be gotten to render them in renderFinalOutput
     */
    @Override
    public void run() {
        while (!stop) {
            EventModel event;
            try {
                event = blockingQueueHandling();  //gets the new Event if one was added to the blockingQueue
            } catch (InterruptedException e) {
                getContext().getLogger().warn(e);
                continue;
            }

            List<CompletableFuture<X>> outputExtensions = getContext().getOutput()
                    .generateAllOutputExtensions(this, getArgument(), event);

            try {
                outputExtensions = timeOut(outputExtensions, getTimeoutLimit());
            } catch (InterruptedException e) {
                getContext().getLogger().warn(e);
            }

            handleFutures(outputExtensions);

            //notifies output-manager when done processing
            isDone(event);
        }
    }

    /**
     * Default implementation waits until a new Event has been received and then processes it.
     *
     * This method is made to be overwritten as seen fit by the developer
     *
     * @throws InterruptedException if interrupted while waiting
     * @return the recently added Event-instance to be processed by the outputPlugin
     */
    public EventModel blockingQueueHandling() throws InterruptedException {
        return eventBlockingQueue.take();
    }

    /**
     * gets the timeout-limit in Milliseconds
     * @return timeout in milliseconds
     */
    public int getTimeoutLimit() {
        return 1000;
    }

    /**
     * method that uses tDoneList to generate a final output that will then be rendered.
     * @param data the data generated
     */
    public abstract void renderFinalOutput(List<X> data);

    /**
     * returns the argument for the OutputExtensions
     * @return the argument
     */
    public abstract T getArgument();
}
