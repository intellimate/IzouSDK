package org.intellimate.izou.sdk.frameworks.music.player.template;

import org.intellimate.izou.events.EventListenerModel;
import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.identification.Identifiable;
import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.identification.IdentificationManager;
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
import org.intellimate.izou.sdk.frameworks.music.resources.*;
import org.intellimate.izou.sdk.frameworks.permanentSoundOutput.events.MuteEvent;
import org.intellimate.izou.sdk.frameworks.permanentSoundOutput.events.StopEvent;
import org.intellimate.izou.sdk.frameworks.permanentSoundOutput.events.UnMuteEvent;
import org.intellimate.izou.sdk.frameworks.presence.events.LeavingEvent;
import org.intellimate.izou.sdk.output.OutputPlugin;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * use this class to actually play music.
 * <p>
 * <b>How to use it:</b><br>
 * Usage is pretty straightforward. Extend this class an override the abstract methods.<br>
 *  - To register further commands, use the getCommandHandler() method in the constructor.<br>
 *  - To start playing, use the PlayerController.startPlaying() methods, never call the play() method directly!
 * Also, don't call the stopSound() method directly, use stopMusicPlayback().
 * All basic methods are implemented in this class,but to code special behaviour fell free to explore the other
 * classes and interfaces.<br>
 * Note: the playback automatically stops when the user left.<br>
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
    private final CommandHandler commandHandler;
    private final InformationProvider informationProvider;
    private final boolean isUsingJava;
    final Lock lock = new ReentrantLock();
    Condition blockRequest = null;

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
                  boolean providesTrackInfo, boolean playbackShuffle, boolean playbackRepeat,
                  boolean playbackRepeatSong, boolean isUsingJava) {
        super(context, id);
        this.runsInPlay = runsInPlay;
        this.activators = activators;
        this.isUsingJava = isUsingJava;
        capabilities = new Capabilities();
        if (providesTrackInfo)
            capabilities.setProvidesTrackInfo(true);
        if (playbackShuffle)
            capabilities.setPlaybackShuffle(true);
        if (playbackRepeat)
            capabilities.setPlaybackRepeat(true);
        if (playbackRepeatSong)
            capabilities.setPlaybackRepeatSong(true);
        commandHandler = createCommandHandler();
        informationProvider = new InformationProvider(getContext(), getID(), this,
                commandHandler);
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
        capabilities = new Capabilities();
        capabilities.setPlayRequestOutside(true);
        if (playRequestTrackInfo)
            capabilities.setPlayRequestDetailed(true);
        if (providesTrackInfo)
            capabilities.setProvidesTrackInfo(true);
        if (playbackShuffle)
            capabilities.setPlaybackShuffle(true);
        if (playbackRepeat)
            capabilities.setPlaybackRepeat(true);
        if (playbackRepeatSong)
            capabilities.setPlaybackRepeatSong(true);
        commandHandler = createCommandHandler();
        informationProvider = new InformationProvider(getContext(), getID(), this,
                commandHandler);
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
     * stops the playing/indicates that the playing stopped.
     * <p>
     * this method has no effect if runsInPlay is enabled in the constructor.<br>
     * </p>
     */
    public void stopMusicPlayback() {
        if (runsInPlay || !isPlaying)
            return;
        lock.lock();
        try {
            if (blockRequest != null)
                blockRequest.signal();
        } finally {
            lock.unlock();
        }
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
     * This method is called to register what resources the object provides.<br>
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
        if (StartMusicRequest.verify(eventModel, capabilities, this, activators)) {
            if (isOutputRunning()) {
                playerError(PlayerError.ERROR_ALREADY_PLAYING, eventModel.getSource());
            } else {
                handleEventRequest(eventModel);
            }
        } else if (eventModel.getListResourceContainer()
                .providesResource(Collections.singletonList(MusicUsageResource.ID))){
            if (isOutputRunning()) {
                eventModel.getListResourceContainer()
                        .provideResource(MusicUsageResource.ID)
                        .forEach(resourceModel ->
                                playerError(PlayerError.ERROR_ALREADY_PLAYING, resourceModel.getProvider()));
            } else {
                handleResourceRequest(eventModel);
            }
        } else {
            handleCommands(eventModel);
        }
    }

    /**
     * handles the a request to start playing music via Resource
     * @param eventModel the eventModel
     */
    private void handleResourceRequest(EventModel eventModel) {
        if (MusicUsageResource.isPermanent(eventModel)) {
            ResourceModel resourceModel = eventModel.getListResourceContainer()
                    .provideResource(MusicUsageResource.ID)
                    .stream()
                    .filter(MusicUsageResource::isPermanent)
                    .findAny()
                    .orElse(null);//should not happen

            //a partially applied function which takes an Identification an returns an Optional StartMusicRequest
            Function<Identification, Optional<StartMusicRequest>> getStartMusicRequest = own ->
                    StartMusicRequest.createStartMusicRequest(resourceModel.getProvider(), own);

            //if we have a trackInfo we create it with the trackInfo as a parameter
            getStartMusicRequest = TrackInfoResource.getTrackInfo(eventModel)
                    .map(trackInfo -> (Function<Identification, Optional<StartMusicRequest>>) own ->
                            StartMusicRequest.createStartMusicRequest(resourceModel.getProvider(), own, trackInfo))
                    .orElse(getStartMusicRequest);

            //if we have a trackInfo we create it with the playlist as a parameter
            getStartMusicRequest = PlaylistResource.getPlaylist(eventModel)
                    .map(playlist -> (Function<Identification, Optional<StartMusicRequest>>) own ->
                            StartMusicRequest.createStartMusicRequest(resourceModel.getProvider(), own, playlist))
                    .orElse(getStartMusicRequest);

            //composes a new Function which appends the Volume to the result
            getStartMusicRequest = getStartMusicRequest.andThen(
                    VolumeResource.getVolume(eventModel)
                    .flatMap(volume -> IdentificationManager.getInstance().getIdentification(this)
                            .map(identification -> new VolumeResource(identification, volume)))
                    .map(resource -> (Function<Optional<StartMusicRequest>, Optional<StartMusicRequest>>) opt ->
                                    opt.map(event -> (StartMusicRequest) event.addResource(resource))
                    )
                    .orElse(Function.identity())::apply);

            IdentificationManager.getInstance().getIdentification(this)
                    .flatMap(getStartMusicRequest::apply)
                    .ifPresent(this::fire);
        } else {
            play(eventModel);
            if (!runsInPlay) {
                blockRequest = lock.newCondition();
                lock.lock();
                try {
                    blockRequest.await(10, TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    debug("interrupted", e);
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    /**
     * handles the commands encoded as Resources/EventIds
     * @param eventModel the eventModel to check
     */
    private void handleCommands(EventModel eventModel) {
        Consumer<Runnable> checkOrCall = runnable -> {
            List<ResourceModel> resourceModels = eventModel.getListResourceContainer()
                    .provideResource(SelectorResource.RESOURCE_ID);
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
     * handles the a request to start playing music via Event
     * @param eventModel the StartMusicRequest
     */
    private void handleEventRequest(EventModel eventModel) {
        playingThread = submit((Runnable) () -> {
                    //noinspection RedundantIfStatement
                    if (runsInPlay) {
                        isRunning = false;
                    } else {
                        isRunning = true;
                    }
                    isPlaying = true;
                    fireStartMusicRequest(eventModel);
                })
                .thenRun(() -> play(eventModel))
                .thenRun(() -> {
                    if (runsInPlay) {
                        isRunning = false;
                        isPlaying = false;
                        endedSound();
                    }
                });
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
        volume = Volume.createVolume(50).orElse(null);
        progress = new Progress(0,0);
        playbackState = PlaybackState.PLAY;
        if (playingThread != null)
            playingThread.cancel(true);
        isRunning = false;
        isPlaying = false;
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
     * this method call must stop the sound.<br>
     * NEVER CALL THIS METHOD DIRECTLY, USE {@link #stopMusicPlayback()}.
     */
    public abstract void stopSound();

    /**
     * this method will be called if a request was cached which was eligible to start the music.<br>
     * please check the events resources for parameters (if expected).
     * @param eventModel the cause
     */
    public abstract void play(EventModel eventModel);
}
