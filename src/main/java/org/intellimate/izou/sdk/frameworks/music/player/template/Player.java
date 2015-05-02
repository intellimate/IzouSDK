package org.intellimate.izou.sdk.frameworks.music.player.template;

import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.identification.Identifiable;
import org.intellimate.izou.resource.ResourceBuilderModel;
import org.intellimate.izou.resource.ResourceModel;
import org.intellimate.izou.sdk.Context;
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
import org.intellimate.izou.sdk.output.OutputPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * use this class to actually play music
 * @author LeanderK
 * @version 1.0
 */
public abstract class Player<T> extends OutputPlugin<T> implements MusicProvider, ResourceBuilderModel, MusicHelper {
    private Playlist playlist = new Playlist(new ArrayList<>());
    private Volume volume = Volume.createVolume(50).orElse(null);
    private Progress progress = new Progress(0,0);
    private final Capabilities capabilities;
    private final InformationProvider informationProvider = new InformationProvider(getContext(), getID(), this);
    private CompletableFuture<?> playingThread = null;
    private final boolean runsInPlay;
    private boolean isRunning = false;
    private boolean isPlaying = true;
    private final List<Identifiable> activators;
    private final CommandHandler commandHandler = createCommandHandler();

    /**
     * creates a new output-plugin with a new id
     *
     * @param context context
     * @param id      the id of the new output-plugin
     * @param runsInPlay whether the termination of the play method should be treated as the termination the
     *                   music (STOP not PAUSE)
     * @param activators the activators which are able to start the Player if the Player is not able to start from
     *                   request from other addons
     * @param providesTrackInfo whether the Player provides TrackInfo
     * @param playbackShuffle whether the player is able to provide the info that the playback is shuffling
     * @param playbackRepeat whether the player is able to provide the info that the playback is repeating
     * @param playbackRepeatSong whether the player is able to provide the info that the playback is repeating the song
     */
    public Player(Context context, String id, boolean runsInPlay, List<Identifiable> activators,
                  boolean providesTrackInfo, boolean playbackShuffle, boolean playbackRepeat, boolean playbackRepeatSong) {
        super(context, id);
        this.runsInPlay = runsInPlay;
        this.activators = activators;
        capabilities = new Capabilities(context);
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
     * creates a new output-plugin with a new id
     *
     * @param context context
     * @param id      the id of the new output-plugin
     * @param runsInPlay whether the termination of the play method should be treated as the termination the
     *                   music (STOP not PAUSE)
     * @param activator the activator which is able to start the Player if the Player is not able to start from
     *                   request from other addons
     * @param providesTrackInfo whether the Player provides TrackInfo
     * @param playbackShuffle whether the player is able to provide the info that the playback is shuffling
     * @param playbackRepeat whether the player is able to provide the info that the playback is repeating
     * @param playbackRepeatSong whether the player is able to provide the info that the playback is repeating the song
     */
    public Player(Context context, String id, boolean runsInPlay, Identifiable activator, boolean providesTrackInfo,
                  boolean playbackShuffle, boolean playbackRepeat, boolean playbackRepeatSong) {
        this(context, id, runsInPlay, Collections.singletonList(activator), providesTrackInfo,
                playbackShuffle, playbackRepeat, playbackRepeatSong);
    }

    /**
     * creates a new output-plugin with a new id
     *
     * @param context context
     * @param id      the id of the new output-plugin
     * @param runsInPlay whether the termination of the play method should be treated as the termination of playing the
     *                   music
     * @param playRequestTrackInfo whether the player is able to process PlayRequests with TrackInfo
     * @param providesTrackInfo whether the Player provides TrackInfo
     * @param playbackShuffle whether the player is able to provide the info that the playback is shuffling
     * @param playbackRepeat whether the player is able to provide the info that the playback is repeating
     * @param playbackRepeatSong whether the player is able to provide the info that the playback is repeating the song
     */
    public Player(Context context, String id, boolean runsInPlay, boolean playRequestTrackInfo, boolean providesTrackInfo,
                  boolean playbackShuffle, boolean playbackRepeat, boolean playbackRepeatSong) {
        super(context, id);
        this.runsInPlay = runsInPlay;
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
        return isPlaying;
    }

    /**
     * sets whether the output is playing, or pausing
     * @param isPlaying if it is playing true
     */
    void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    /**
     * this method has no effect if runsInPlay is enabled in the constructor
     */
    public void setPlayingStopped() {
        if (runsInPlay)
            return;
        isRunning = false;
        endSound();
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

    public void updatePlaylist(Playlist playlist) {
        this.playlist = playlist;
        updatePlayInfo(playlist);
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
     * sets an new Volume
     * @param volume the Volume
     */
    public void setVolume(Volume volume) {
        this.volume = volume;
        updatePlayInfo(volume);
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
     * sets the new Progress
     * @param progress the progress
     */
    public void setProgress(Progress progress) {
        this.progress = progress;
        updatePlayInfo(progress);
    }

    /**
     * updates the Info about the current song
     * @param playlist the playlist, or null
     * @param progress the progress, or null
     * @param volume the volume, or null
     */
    public void updateInfo(Playlist playlist, Progress progress, Volume volume) {
        if (playlist != null)
            this.playlist = playlist;
        if (progress != null)
            this.progress = progress;
        if (volume != null)
            this.volume = volume;
        updatePlayInfo(playlist, progress, null, volume);
    }

    /**
     * call this method if the trackInfo object in the playlist was updated. Only the trackinfo object will be sent via
     * Event
     * @param playlist the playlist
     * @param info the new trackInfo object
     */
    public void trackInfoUpdate(Playlist playlist, TrackInfo info) {
        this.playlist = playlist;
        updatePlayInfo(info);
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
                fireStartMusicRequest(eventModel);
            }).thenRun(() -> play(eventModel))
                    .thenRun(() -> {
                        if (runsInPlay) {
                            isRunning = false;
                            endSound();
                        }
                    });
        }
        if (eventModel.containsDescriptor(MuteEvent.ID)) {
            mute();
        }
        if (eventModel.containsDescriptor(UnMuteEvent.ID)) {
            unMute();
        }
        if (eventModel.containsDescriptor(StopEvent.ID)) {
            stopSound();
        }
        if (StopMusic.verify(eventModel, this)) {
            stopSound();
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
        startSound(playlist.orElse(null), progress.orElse(null), trackInfo.orElse(null), volume.orElse(null));
    }

    @Override
    public void stop() {
        stopSound();
        super.stop();
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

    /**
     * this method will be called if a request was catched which
     * @param eventModel the cause
     */
    public abstract void play(EventModel eventModel);
}
