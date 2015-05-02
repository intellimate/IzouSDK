package org.intellimate.izou.sdk.frameworks.permanentSoundOutput.output;

import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.frameworks.permanentSoundOutput.events.MuteEvent;
import org.intellimate.izou.sdk.frameworks.permanentSoundOutput.events.StopEvent;
import org.intellimate.izou.sdk.frameworks.permanentSoundOutput.events.UnMuteEvent;
import org.intellimate.izou.sdk.output.OutputPlugin;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * extend this class to use the framework
 * @author LeanderK
 * @version 1.0
 */
public abstract class SimplePermanentSoundOutputPlugin<T> extends OutputPlugin<T> implements PermanentSoundUsed,
        PermanentSoundHelper, PermanentSoundResources {
    private boolean isCurrentlyPlayingSound = false;

    /**
     * creates a new output-plugin with a new id
     *
     * @param context context
     * @param id the id of the new output-plugin
     */
    public SimplePermanentSoundOutputPlugin(Context context, String id) {
        super(context, id);
        resourcesInit(context);
    }

    /**
     * Default implementation waits until a new Event has been received and then processes it.
     * <p>
     * This method is made to be overwritten as seen fit by the developer
     *
     * @return the recently added Event-instance to be processed by the outputPlugin
     * @throws InterruptedException if interrupted while waiting
     */
    @Override
    public EventModel blockingQueueHandling() throws InterruptedException {
        EventModel eventModel = super.blockingQueueHandling();
        if (eventModel.containsDescriptor(MuteEvent.ID)) {
            mute();
        }
        if (eventModel.containsDescriptor(UnMuteEvent.ID)) {
            unMute();
        }
        if (eventModel.containsDescriptor(StopEvent.ID)) {
            stopSound();
        }
        return eventModel;
    }



    /**
     * retruns true if playing and false if not
     *
     * @return tre if playing
     */
    @Override
    public boolean isOutputRunning() {
        return isCurrentlyPlayingSound;
    }

    /**
     * use this method to start playing sound (only plays sound if it is not already playing)
     * @param function the function which plays the sound
     * @return the Future
     */
    public Optional<CompletableFuture<Void>> startPlaying(Runnable function) {
        if (isCurrentlyPlayingSound)
            return Optional.empty();
        CompletableFuture<Void> voidCompletableFuture = submit((Runnable) () -> startSound(this))
                .thenRun(() -> {
                    try {
                        isCurrentlyPlayingSound = true;
                        function.run();
                    } finally {
                        submit((Runnable) () -> endSound(this));
                        isCurrentlyPlayingSound = false;
                    }
                });
        return Optional.of(voidCompletableFuture);
    }

    /**
     * this method call must mute the plugin
     */
    public abstract void mute();

    /**
     * this method call must un-mute the plugin
     */
    public abstract void unMute();

    /**
     * this method call must stop the sound
     */
    public abstract void stopSound();
}
