package org.intellimate.izou.sdk.frameworks.music.player.template;

import org.intellimate.izou.events.EventListenerModel;
import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.identification.Identifiable;
import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.resource.ResourceBuilderModel;
import org.intellimate.izou.resource.ResourceModel;
import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.frameworks.common.resources.SelectorResource;
import org.intellimate.izou.sdk.frameworks.music.Capabilities;
import org.intellimate.izou.sdk.frameworks.music.events.PlayerCommand;
import org.intellimate.izou.sdk.frameworks.music.events.PlayerError;
import org.intellimate.izou.sdk.frameworks.music.events.StartMusicRequest;
import org.intellimate.izou.sdk.frameworks.music.events.StopMusic;
import org.intellimate.izou.sdk.frameworks.music.player.*;
import org.intellimate.izou.sdk.frameworks.music.resources.PlaylistResource;
import org.intellimate.izou.sdk.frameworks.music.resources.ProgressResource;
import org.intellimate.izou.sdk.frameworks.music.resources.TrackInfoResource;
import org.intellimate.izou.sdk.frameworks.music.resources.VolumeResource;
import org.intellimate.izou.sdk.frameworks.permanentSoundOutput.events.MuteEvent;
import org.intellimate.izou.sdk.frameworks.permanentSoundOutput.events.StopEvent;
import org.intellimate.izou.sdk.frameworks.permanentSoundOutput.events.UnMuteEvent;
import org.intellimate.izou.sdk.frameworks.presence.events.LeavingEvent;
import org.intellimate.izou.sdk.output.OutputPlugin;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * use this class to actually play music.
 * <p>
 * <b>How to use it:</b><br>
 * to register further commands, use the getCommandHandler() method in the constructor.
 * to start playing, use the PlayerController.startPlaying() methods, never call the play() method directly!.
 * Also, don't call the stopSound() method directly!.
 * All basic methods are implemented in this class,but to code special behaviour fell free to explore the other
 * classes and interfaces.<br>
 * Note: the playback automatically stops when the user left.
 * </p>
 * @author LeanderK
 * @version 1.0
 */
public abstract class Player<T> extends OutputPlugin<T> implements MusicProvider, ResourceBuilderModel, MusicHelper, EventListenerModel {
    private Playlist playlist = new Playlist(new ArrayList<>());
    private Volume volume = Volume.createVolume(50).orElse(null);
    private Progress progress = new Progress(0,0);
    private PlaybackState playbackState = PlaybackState.PLAY;
    private final Capabilities capabilities;
    private CompletableFuture<?> playingThread = null;
    private final boolean runsInPlay;
    private boolean isRunning = false;
    private boolean isPlaying = false;
    private final List<Identifiable> activators;
    private final CommandHandler commandHandler = createCommandHandler();
    private final InformationProvider informationProvider = new InformationProvider(getContext(), getID(), this, commandHandler);
    private final boolean isUsingJava;

    /**
     * creates a new output-plugin with a new id
     *  @param context context
     * @param id      the id of the new output-plugin. The ID of the InformationProvider is also based on this id
     *                (id + ".InformationProvider")
     * @param runsInPlay whether the termination of the play method should be treated as the termination the
 *                   music (STOP not PAUSE)
     * @param activators the activators which are able to start the Player if the Player is not able to start from
*                   request from other addons
     * @param providesTrackInfo whether the Player provides TrackInfo
     * @param playbackShuffle whether the player is able to provide the info that the playback is shuffling
     * @param playbackRepeat whether the player is able to provide the info that the playback is repeating
     * @param playbackRepeatSong whether the player is able to provide the info that the playback is repeating the song
     * @param isUsingJava true if using java, false if not (and for example a C-library)
     */
    public Player(Context context, String id, boolean runsInPlay, List<Identifiable> activators,
                  boolean providesTrackInfo, boolean playbackShuffle, boolean playbackRepeat, boolean playbackRepeatSong, boolean isUsingJava) {
        super(context, id);
        this.runsInPlay = runsInPlay;
        this.activators = activators;
        this.isUsingJava = isUsingJava;
        capabilities = new Capabilities(context);
        if (providesTrackInfo)
            capabilities.setProvidesTrackInfo(true);
        if (playbackShuffle)
            capabilities.setPlaybackShuffle(true);
        if (playbackRepeat)
            capabilities.setPlaybackRepeat(true);
        if (playbackRepeatSong)
            capabilities.setPlaybackRepeatSong(true);
        getContext().getEvents().registerEventListener(Collections.singletonList(LeavingEvent.ID), this);
    }

    /**
     * creates a new output-plugin with a new id
     *  @param context context
     * @param id      the id of the new output-plugin. The ID of the InformationProvider is also based on this id
     *                (id + ".InformationProvider")
     * @param runsInPlay whether the termination of the play method should be treated as the termination the
     *                   music (STOP not PAUSE)
     * @param activator the activator which is able to start the Player if the Player is not able to start from
     *                   request from other addons
     * @param providesTrackInfo whether the Player provides TrackInfo
     * @param playbackShuffle whether the player is able to provide the info that the playback is shuffling
     * @param playbackRepeat whether the player is able to provide the info that the playback is repeating
     * @param playbackRepeatSong whether the player is able to provide the info that the playback is repeating the song
     * @param isUsingJava true if using java, false if not (and for example a C-library)
     */
    @SuppressWarnings("unused")
    public Player(Context context, String id, boolean runsInPlay, Identifiable activator, boolean providesTrackInfo,
                  boolean playbackShuffle, boolean playbackRepeat, boolean playbackRepeatSong, boolean isUsingJava) {
        this(context, id, runsInPlay, Collections.singletonList(activator), providesTrackInfo,
                playbackShuffle, playbackRepeat, playbackRepeatSong, isUsingJava);
    }

    /**
     * creates a new output-plugin with a new id
     *  @param context context
     * @param id      the id of the new output-plugin
     * @param runsInPlay whether the termination of the play method should be treated as the termination of playing the
     *                   music
     * @param playRequestTrackInfo whether the player is able to process PlayRequests with TrackInfo
     * @param providesTrackInfo whether the Player provides TrackInfo
     * @param playbackShuffle whether the player is able to provide the info that the playback is shuffling
     * @param playbackRepeat whether the player is able to provide the info that the playback is repeating
     * @param playbackRepeatSong whether the player is able to provide the info that the playback is repeating the song
     * @param isUsingJava true if using java, false if not (and for example a C-library)
     */
    @SuppressWarnings("unused")
    public Player(Context context, String id, boolean runsInPlay, boolean playRequestTrackInfo, boolean providesTrackInfo,
                  boolean playbackShuffle, boolean playbackRepeat, boolean playbackRepeatSong, boolean isUsingJava) {
        super(context, id);
        this.runsInPlay = runsInPlay;
        this.isUsingJava = isUsingJava;
        activators = null;
        capabilities = new Capabilities(context);
        capabilities.setPlayRequestOutside(true);
        if (playRequestTrackInfo)
            capabilities.setPlayRequestTrackInfo(true);
        if (providesTrackInfo)
            capabilities.setProvidesTrackInfo(true);
        if (playbackShuffle)
            capabilities.setPlaybackShuffle(true);
        if (playbackRepeat)
            capabilities.setPlaybackRepeat(true);
        if (playbackRepeatSong)
            capabilities.setPlaybackRepeatSong(true);
    }

    /**
     * returns the CommandHandler
     * @return the CommandHandler
     */
    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    /**
     * true if playing and false if not
     *
     * @return tre if playing
     */
    @Override
    public boolean isOutputRunning() {
        if (runsInPlay) {
            return playingThread != null && !playingThread.isDone();
        } else {
            return isRunning;
        }
    }

    /**
     * true if playing and false if not
     *
     * @return tre if playing
     */
    @Override
    public boolean isPlaying() {
        if (runsInPlay)
            return isRunning;
        return isPlaying;
    }

    /**
     * true if using java, false if not (and for example a C-library)
     *
     * @return true if using java
     */
    @Override
    public boolean isUsingJava() {
        return isUsingJava;
    }

    /**
     * this method has no effect if runsInPlay is enabled in the constructor.
     * stops the playing
     */
    public void stopMusicPlayback() {
        if (runsInPlay || !isPlaying)
            return;
        isPlaying = false;
        stopSound();
        endedSound();
        rollBackToDefault();
        super.stop();
    }

    /**
     * gets the current Playlist
     *
     * @return the current Playlist
     */
    @Override
    public Playlist getCurrentPlaylist() {
        return playlist;
    }

    /**
     * fires an update event which notifies that parameters have changed
     *
     * @param playlist the optional playlist
     */
    @Override
    public void updatePlayInfo(Playlist playlist) {
        this.playlist = playlist;
        MusicHelper.super.updatePlayInfo(playlist);
    }

    /**
     * checks if the trackInfo is an update and fires the appropriate Event. This method should not be called
     * without an active playlist, or an NullPointerException will be thrown.
     * this method:<br>
     *     - fires nothing if the trackInfo equals the current trackInfo.<br>
     *     - fires an trackInfoUpdate if the trackInfo contains information not found in the current.<br>
     */
    @SuppressWarnings("unused")
    public void updateCurrentTrackInfo(TrackInfo trackInfo) {
        if (playlist.getCurrent().equals(trackInfo) ||
                playlist.getCurrent().isNew(trackInfo))
            return;
        this.playlist = playlist.update(playlist.getCurrent(), trackInfo);
        trackInfoUpdate(playlist, trackInfo);
    }

    /**
     * call this method if the trackInfo object in the playlist was updated. Only the trackinfo object will be sent via
     * Event
     * @param playlist the playlist
     * @param info the new trackInfo object
     */
    @SuppressWarnings("unused")
    public void trackInfoUpdate(Playlist playlist, TrackInfo info) {
        this.playlist = playlist;
        updatePlayInfo(info);
    }

    /**
     * gets the Volume
     *
     * @return the volume
     */
    @Override
    public Volume getVolume() {
        return volume;
    }

    /**
     * fires an update event which notifies that parameters have changed
     *
     * @param volume the optional volume
     */
    @Override
    public void updatePlayInfo(Volume volume) {
        if (this.volume.equals(volume))
            return;
        this.volume = volume;
        MusicHelper.super.updatePlayInfo(volume);
    }

    /**
     * gets the Progress
     *
     * @return the Progress
     */
    @Override
    public Progress getCurrentProgress() {
        return progress;
    }

    /**
     * fires an update event which notifies that parameters have changed
     *
     * @param progress the optional progress
     */
    @Override
    public void updatePlayInfo(Progress progress) {
        this.progress = progress;
        MusicHelper.super.updatePlayInfo(progress);
    }

    /**
     * gets the PlaybackState of the Player
     *
     * @return the PlaybackState
     */
    @Override
    public PlaybackState getPlaybackState() {
        return playbackState;
    }

    /**
     * signals that the playing paused
     */
    @SuppressWarnings("unused")
    public void pausePlaying() {
        switch (playbackState) {
            case PAUSE: return;
            default: playbackState = PlaybackState.PAUSE;
                updateStateInfo(playbackState);
        }
    }

    /**
     * signals that the playing resumed
     */
    @SuppressWarnings("unused")
    public void resumePlaying() {
        switch (playbackState) {
            case PLAY: return;
            default: playbackState = PlaybackState.PLAY;
                updateStateInfo(playbackState);
        }
    }

    /**
     * updates the Info about the current song
     * @param playlist the playlist, or null
     * @param progress the progress, or null
     * @param volume the volume, or null
     */
    @SuppressWarnings("unused")
    public void updatePlayInfo(Playlist playlist, Progress progress, Volume volume) {
        if (playlist != null)
            this.playlist = playlist;
        if (progress != null)
            this.progress = progress;
        if (volume != null)
            this.volume = volume;
        updatePlayInfo(playlist, progress, null, volume);
    }

    /**
     * fires an update event which notifies that parameters have changed
     *
     * @param playlist  the optional playlist
     * @param progress  the optional progress
     * @param trackInfo the optional trackInfo
     * @param volume    the optional volume
     */
    @Override
    public void updatePlayInfo(Playlist playlist, Progress progress, TrackInfo trackInfo, Volume volume) {
        if (playlist != null)
            this.playlist = playlist;
        if (progress != null)
            this.progress = progress;
        if (volume != null)
            this.volume = volume;
        MusicHelper.super.updatePlayInfo(playlist, progress, null, volume);
    }

    /**
     * gets the Capabilities of the Player
     *
     * @return and instance of Capabilities
     */
    @Override
    public Capabilities getCapabilities() {
        return capabilities;
    }

    /**
     * This method is called to register what resources the object provides.
     * just pass a List of Resources without Data in it.
     *
     * @return a List containing the resources the object provides
     */
    @Override
    public List<? extends ResourceModel> announceResources() {
        return informationProvider.announceResources();
    }

    /**
     * this method is called to register for what Events it wants to provide Resources.
     * <p>
     * The Event has to be in the following format: It should contain only one Descriptor and and one Resource with the
     * ID "description", which contains an description of the Event.
     * </p>
     *
     * @return a List containing ID's for the Events
     */
    @Override
    public List<? extends EventModel<?>> announceEvents() {
        return informationProvider.announceEvents();
    }

    /**
     * This method is called when an object wants to get a Resource.
     * <p>
     * Don't use the Resources provided as arguments, they are just the requests.
     * There is a timeout after 1 second.
     * </p>
     *
     * @param resources a list of resources without data
     * @param event     if an event caused the action, it gets passed. It can also be null.
     * @return a list of resources with data
     */
    @Override
    public List<ResourceModel> provideResource(List<? extends ResourceModel> resources, Optional<EventModel> event) {
        return informationProvider.provideResource(resources, event);
    }

    /**
     * method that uses the data from the OutputExtensions to generate a final output that will then be rendered.
     *
     * @param data       the data generated
     * @param eventModel the Event which caused the whole thing
     */
    @Override
    public void renderFinalOutput(List<T> data, EventModel eventModel) {
        Consumer<Runnable> checkOrCall = runnable -> {
            List<ResourceModel> resourceModels =
                    eventModel.getListResourceContainer().provideResource(SelectorResource.RESOURCE_ID);
            if (resourceModels.isEmpty()) {
                runnable.run();
            } else {
                resourceModels.stream()
                        .map(resourceModel -> resourceModel.getResource() instanceof Identification ?
                                ((Identification) resourceModel.getResource()) : null)
                        .filter(Objects::nonNull)
                        .filter(this::isOwner)
                        .findAny()
                        .ifPresent(id -> runnable.run());
            }
        };
        if (StartMusicRequest.verify(eventModel, capabilities, this, activators)) {
            if (!isOutputRunning()) {
                playerError(PlayerError.ERROR_ALREADY_PLAYING);
            }
            playingThread = submit((Runnable) () -> {
                //noinspection RedundantIfStatement
                if (runsInPlay) {
                    isRunning = false;
                } else {
                    isRunning = true;
                }
                isPlaying = true;
                fireStartMusicRequest(eventModel);
            }).thenRun(() -> play(eventModel))
                    .thenRun(() -> {
                        if (runsInPlay) {
                            isRunning = false;
                            isPlaying = false;
                            endedSound();
                        }
                    });
        }
        if (eventModel.containsDescriptor(MuteEvent.ID)) {
            checkOrCall.accept(this::mute);
        }
        if (eventModel.containsDescriptor(UnMuteEvent.ID)) {
            checkOrCall.accept(this::unMute);
        }
        if (eventModel.containsDescriptor(StopEvent.ID)) {
            checkOrCall.accept(this::stopMusicPlayback);
        }
        if (StopMusic.verify(eventModel, this)) {
            stopMusicPlayback();
        }
        if (PlayerCommand.verify(eventModel, this)) {
            getCommandHandler().handleCommandResources(eventModel);
        }
    }

    /**
     * override this method if you want to change the command handler
     * @return the command handler
     */
    protected CommandHandler createCommandHandler() {
        return new CommandHandler(this, this, this, capabilities);
    }

    /**
     * this method will be called to create and fire the StartMusicRequest
     * @param eventModel the cause
     */
    protected void fireStartMusicRequest(EventModel eventModel) {
        Optional<Playlist> playlist = PlaylistResource.getPlaylist(eventModel);
        Optional<Progress> progress = ProgressResource.getProgress(eventModel);
        Optional<TrackInfo> trackInfo = TrackInfoResource.getTrackInfo(eventModel);
        Optional<Volume> volume = VolumeResource.getVolume(eventModel);
        startedSound(playlist.orElse(null), progress.orElse(null), trackInfo.orElse(null), volume.orElse(null), isUsingJava);
    }

    @Override
    public void stop() {
        stopMusicPlayback();
    }

    @Override
    public void eventFired(EventModel eventModel) {
        if (eventModel.containsDescriptor(LeavingEvent.GENERAL_DESCRIPTOR))
            stopMusicPlayback();
    }

    /**
     * sets every information into its default state (playlist, volume, etc...)
     */
    public void rollBackToDefault() {
        playlist = new Playlist(new ArrayList<>());
        playlist = new Playlist(new ArrayList<>());
        volume = Volume.createVolume(50).orElse(null);
        progress = new Progress(0,0);
        playbackState = PlaybackState.PLAY;
    }

    /**
     * this method call must mute the plugin.
     */
    public abstract void mute();

    /**
     * this method call must un-mute the plugin.
     */
    public abstract void unMute();

    /**
     * this method call must stop the sound.
     * NEVER CALL THIS METHOD DIRECTLY, USED {@link #stopMusicPlayback()}.
     */
    public abstract void stopSound();

    /**
     * this method will be called if a request was cached which was eligible to start the music.
     * please check the events resources for parameters (if expected).
     * @param eventModel the cause
     */
    public abstract void play(EventModel eventModel);
}
