package org.intellimate.izou.sdk.frameworks.music.player;

import com.google.common.reflect.TypeToken;
import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.output.OutputPluginModel;
import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.util.AddOnModule;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * The base class for all music Players, is technically an OutputPlugin
 * @author LeanderK
 * @version 1.0
 */
public abstract class MusicPlayer extends AddOnModule implements OutputPluginModel<Object, Object> {

    /**
     * stops the run-loop
     */
    private boolean stop;

    /**
     * true if te MusicPlayer is currently playing
     */
    private boolean isPlaying;

    /**
     * here are the events stored before they get processed
     */
    private final BlockingQueue<EventModel> eventBlockingQueue = new LinkedBlockingDeque<>();

    /**
     * initializes the Module
     *
     * @param context the current Context
     * @param ID      the ID
     */
    public MusicPlayer(Context context, String ID) {
        super(context, ID);
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
    public TypeToken<Object> getReceivingType() {
        return null;
    }

    /**
     * returns the Type of the argument for the OutputExtensions, or null if none
     *
     * @return the type of the Argument
     */
    @Override
    public TypeToken<Object> getArgumentType() {
        return null;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
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
     *
     */
    public abstract void play(EventModel eventModel);
}
