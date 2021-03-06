package org.intellimate.izou.sdk.frameworks.music.player.template;

import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.resource.ResourceModel;
import org.intellimate.izou.sdk.frameworks.music.Capabilities;
import org.intellimate.izou.sdk.frameworks.music.events.PlayerError;
import org.intellimate.izou.sdk.frameworks.music.player.*;
import org.intellimate.izou.sdk.frameworks.music.resources.CommandResource;
import org.intellimate.izou.sdk.frameworks.music.resources.ProgressResource;
import org.intellimate.izou.sdk.frameworks.music.resources.TrackInfoResource;
import org.intellimate.izou.sdk.frameworks.music.resources.VolumeResource;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * this class is used to handle the Commands and directs them to the registered methods.
 * @author LeanderK
 * @version 1.0
 */
public class CommandHandler {
    private final MusicHelper musicHelper;
    private final MusicProvider musicProvider;
    private final Capabilities capabilities;
    private final Runnable stopCallback;
    private Consumer<String> playPause = null;
    private Consumer<TrackInfo> selectTrack = null;
    private Consumer<String> nextPrevious = null;
    private Consumer<Progress> jumpProgress = null;
    private Consumer<String> changePlayback = null;
    private Consumer<Volume> changeVolume;
    private Function<String, Playlist> playlistForNameFunction = null;
    private Supplier<List<String>> availablePlaylist = null;

    /**
     * creates a new CommandHandler
     * @param musicHelper the musicHelper
     * @param musicProvider the musicProvider
     * @param stopCallback the callback for the stop-command
     * @param capabilities the capabilities
     */
    public CommandHandler(MusicHelper musicHelper, MusicProvider musicProvider,
                          Runnable stopCallback, Capabilities capabilities) {
        this.musicHelper = musicHelper;
        this.capabilities = capabilities;
        this.musicProvider = musicProvider;
        this.stopCallback = stopCallback;
    }

    /**
     * adds the ability for the Play/Pause requests
     * @param controller  the controller for callback
     */
    public void setPlayPauseController(Consumer<String> controller) {
        if (controller == null)
            return;
        this.playPause = controller;
        capabilities.setPlayPauseControl(true);
    }

    /**
     * adds the ability to select tracks
     * @param controller  the controller for callback
     */
    public void setTrackSelectorController(Consumer<TrackInfo> controller) {
        if (controller == null)
            return;
        selectTrack = controller;
        capabilities.setAbleToSelectTrack(true);
    }

    /**
     * adds the ability to select the next/previous track
     * @param controller  the controller for callback
     */
    public void setNextPreviousController(Consumer<String> controller) {
        if (controller == null)
            return;
        nextPrevious = controller;
        capabilities.setNextPrevious(true);
    }

    /**
     * adds the ability to jump to a specified position of the current track
     * @param controller  the controller for callback
     */
    public void setJumpProgressController(Consumer<Progress> controller) {
        if (controller == null)
            return;
        jumpProgress = controller;
        capabilities.setAbleToJump(true);
    }

    /**
     * adds the ability to change the playback
     * @param controller  the controller for callback
     */
    public void setPlaybackChangeableController(Consumer<String> controller) {
        if (controller == null)
            return;
        changePlayback = controller;
        capabilities.setPlaybackChangeable(true);
    }

    /**
     * adds the ability to change the volume from outside the player
     * @param controller  the controller for callback
     */
    public void setVolumeChangeableController(Consumer<Volume> controller) {
        if (controller == null)
            return;
        changeVolume = controller;
        capabilities.setChangeVolume(true);
    }

    /**
     * adds the ability to return the available playlists on request.
     * @param availablePlaylist retruns a List of Strings which represent the Playlists
     * @param playlistForNameFunction takes a String from the List and returns the Playlist
     */
    public void broadcastAvailablePlaylists(Supplier<List<String>> availablePlaylist, Function<String, Playlist> playlistForNameFunction) {
        if (availablePlaylist == null || playlistForNameFunction == null)
            return;
        this.availablePlaylist = availablePlaylist;
        this.playlistForNameFunction = playlistForNameFunction;
        capabilities.setBroadcasting(true);
    }

    /**
     * this method gets called when a new Command was found. It automatically fires the update Event or an error
     * @param eventModel the event with the Commands
     */
    public void handleCommandResources(EventModel eventModel) {
        List<ResourceModel<String>> resourceModels = eventModel.getListResourceContainer()
                .provideResource(CommandResource.ResourceID)
                .stream()
                .filter(resourceModel -> resourceModel.getResource() instanceof String)
                .map(resourceModel -> {
                    try {
                        //noinspection unchecked
                        return (ResourceModel<String>) resourceModel;
                    } catch (ClassCastException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        for (ResourceModel<String> resourceModel : resourceModels) {
            if (!CommandResource.verifyCommand(resourceModel.getResource()))
                continue;
            if (!CommandResource.verifyCapabilities(resourceModel.getResource(), capabilities)) {
                musicHelper.playerError(PlayerError.ERROR_NOT_ABLE + "command: " + resourceModel.getResource(),
                        resourceModel.getProvider());
                continue;
            }
            switch (resourceModel.getResource()) {
                case CommandResource.PLAY: if (!musicProvider.isPlaying())
                    playPause.accept(resourceModel.getResource());
                    break;
                case CommandResource.PAUSE: if (musicProvider.isPlaying())
                    playPause.accept(resourceModel.getResource());
                    break;
                case CommandResource.SELECT_TRACK: handleSelectTrack(eventModel, resourceModel);
                    break;
                case CommandResource.NEXT: nextPrevious.accept(resourceModel.getResource());
                    break;
                case CommandResource.PREVIOUS: nextPrevious.accept(resourceModel.getResource());
                    break;
                case CommandResource.JUMP: handleJump(eventModel, resourceModel);
                    break;
                case CommandResource.CHANGE_PLAYBACK: changePlayback.accept(resourceModel.getResource());
                    break;
                case CommandResource.CHANGE_VOLUME: handleVolume(eventModel, resourceModel);
                    break;
                case CommandResource.STOP: stopCallback.run();
                    break;
            }
        }
    }

    /**
     * handles the volume-command
     * @param eventModel the event
     * @param resourceModel the resourcemodel
     */
    private void handleVolume(EventModel eventModel, ResourceModel<String> resourceModel) {
        Optional<Volume> volumeResource = VolumeResource.getVolume(eventModel);
        if (!volumeResource.isPresent()) {
            musicHelper.playerError(PlayerError.ERROR_ILLEGAL + "command: " + resourceModel.getResource() + "missing resource",
                    resourceModel.getProvider());
        }
        changeVolume.accept(volumeResource.get());
    }

    /**
     * handles the jump-command
     * @param eventModel the event
     * @param resourceModel the resourcemodel
     */
    private void handleJump(EventModel eventModel, ResourceModel<String> resourceModel) {
        Optional<Progress> progress = ProgressResource.getProgress(eventModel);
        if (!progress.isPresent()) {
            musicHelper.playerError(PlayerError.ERROR_ILLEGAL + "command: " + resourceModel.getResource() + "missing resource",
                    resourceModel.getProvider());
        }
        jumpProgress.accept(progress.get());
    }

    /**
     * handles the select Track command
     * @param eventModel the event
     * @param resourceModel the resource
     */
    private void handleSelectTrack(EventModel eventModel, ResourceModel<String> resourceModel) {
        Optional<TrackInfo> trackInfo = TrackInfoResource.getTrackInfo(eventModel);
        if (!trackInfo.isPresent()) {
            musicHelper.playerError(PlayerError.ERROR_ILLEGAL + "command: " + resourceModel.getResource() + "missing resource",
                    resourceModel.getProvider());
        }
        selectTrack.accept(trackInfo.get());
    }

    /**
     * retruns the playlist for the specified name
     * @param name the name of the playlist
     * @return the playlist
     */
    Playlist getPlaylistFromName(String name) {
        return playlistForNameFunction.apply(name);
    }

    /**
     * generates all available playlists.
     * @return a list containing the names of all available Playlists.
     */
    List<String> getAvailablePlaylists() {
        return availablePlaylist.get();
    }
}
