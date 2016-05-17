package org.intellimate.izou.sdk.frameworks.music.player.template;

import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.identification.IdentificationManager;
import org.intellimate.izou.resource.ResourceModel;
import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.frameworks.music.Capabilities;
import org.intellimate.izou.sdk.frameworks.music.player.*;
import org.intellimate.izou.sdk.frameworks.music.resources.*;
import org.intellimate.izou.sdk.util.AddOnModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * this class is responsible for communication information on request.
 * Essentially it is responsible for answering the the resource-requests.
 * The code for answering lies in the Interface (it is implemented this way to allow better composition if you don't want
 * to use the templates)
 * @author LeanderK
 * @version 1.0
 */
public class InformationProvider extends AddOnModule implements MusicResourceGenerator {
    private final MusicProvider musicProvider;
    private final CommandHandler commandHandler;

    /**
     * initializes the Module
     *
     * @param context the current Context
     * @param musicProvider the musicProvider to get the data from
     * @param ID      the ID
     * @param commandHandler the commandHandler
     */
    public InformationProvider(Context context, String ID, MusicProvider musicProvider, CommandHandler commandHandler) {
        super(context, ID+".InformationProvider");
        this.musicProvider = musicProvider;
        this.commandHandler = commandHandler;
    }

    @Override
    public List<? extends ResourceModel> announceResources() {
        List<ResourceModel> list = new ArrayList<>();
        IdentificationManagerM.getInstance().getIdentification(this)
                .ifPresent(id -> {
                    list.add(new BroadcasterAvailablePlaylists(id));
                    list.add(new BroadcasterPlaylist(id));
                });
        list.addAll(MusicResourceGenerator.super.announceResources());
        return list;
    }

    /**
     * generates the resources
     *
     * @param resourceModel the resourceModel
     * @param event         the Event
     * @return a Stream containing the resourceModel(s)
     */
    @Override
    public Optional<? extends ResourceModel> generateResource(ResourceModel resourceModel, Optional<EventModel> event) {
        switch (resourceModel.getResourceID()) {
            case BroadcasterAvailablePlaylists.RESOURCE_ID: return createBroadcasterAvailablePlaylists();
            case BroadcasterPlaylist.RESOURCE_ID : return createBroadcasterPlaylist(resourceModel);
            default: return MusicResourceGenerator.super.generateResource(resourceModel, event);
        }
    }

    private Optional<? extends ResourceModel> createBroadcasterPlaylist(ResourceModel resourceModel) {
        if (!getCapabilities().isBroadcasting()) {
            getContext().getLogger().error(PROVIDE_RESOURCE_ERROR_NOT_CAPABLE + "BroadcasterPlaylist");
            return Optional.empty();
        } else {
            Object resource = resourceModel.getResource();
            try {
                //noinspection unchecked
                HashMap<String, Object> hashMap = (HashMap<String, Object>) resource;
                if (hashMap.get(BroadcasterPlaylist.RESOURCE_ID) == null) {
                    getContext().getLogger().error("playlist name not specified");
                    return Optional.empty();
                }
                Playlist playlist = commandHandler.getPlaylistFromName((String) hashMap.get(BroadcasterPlaylist.RESOURCE_ID));
                Optional<BroadcasterPlaylist> BroadcasterPlaylistResource = IdentificationManagerM.getInstance()
                        .getIdentification(this)
                        .map(id -> BroadcasterPlaylist.createPlaylistAnswer(id, playlist));
                if (!BroadcasterPlaylistResource.isPresent()) {
                    getContext().getLogger().error(PROVIDE_RESOURCE_ERROR_GENERATING + "BroadcasterPlaylist");
                }
                return BroadcasterPlaylistResource;
            } catch (ClassCastException e) {
                getContext().getLogger().error("got wrong resource-data");
                return Optional.empty();
            }
        }
    }

    private Optional<? extends ResourceModel> createBroadcasterAvailablePlaylists() {
        if (!getCapabilities().isBroadcasting()) {
            getContext().getLogger().error(PROVIDE_RESOURCE_ERROR_NOT_CAPABLE + "BroadcasterAvailablePlaylists");
            return Optional.empty();
        } else {
            List<String> playlists = commandHandler.getAvailablePlaylists();
            Optional<BroadcasterAvailablePlaylists> BroadcasterPlaylistResource = IdentificationManagerM.getInstance()
                    .getIdentification(this)
                    .map(id -> new BroadcasterAvailablePlaylists(id, playlists));
            if (!BroadcasterPlaylistResource.isPresent()) {
                getContext().getLogger().error(PROVIDE_RESOURCE_ERROR_GENERATING + "BroadcasterAvailablePlaylists");
            }
            return BroadcasterPlaylistResource;
        }
    }

    /**
     * gets the current Playlist
     *
     * @return the current Playlist
     */
    @Override
    public Playlist getCurrentPlaylist() {
        return musicProvider.getCurrentPlaylist();
    }

    /**
     * gets the Volume
     *
     * @return the volume
     */
    @Override
    public Volume getVolume() {
        return musicProvider.getVolume();
    }

    /**
     * gets the Progress
     *
     * @return the Progress
     */
    @Override
    public Progress getCurrentProgress() {
        return musicProvider.getCurrentProgress();
    }

    /**
     * gets the Capabilities of the Player
     *
     * @return and instance of Capabilities
     */
    @Override
    public Capabilities getCapabilities() {
        return musicProvider.getCapabilities();
    }

    /**
     * gets the PlaybackState of the Player
     *
     * @return the PlaybackState
     */
    @Override
    public PlaybackState getPlaybackState() {
        return musicProvider.getPlaybackState();
    }

    /**
     * true if playing and false if not
     *
     * @return tre if playing
     */
    @Override
    public boolean isOutputRunning() {
        return musicProvider.isOutputRunning();
    }

    /**
     * true if playing and false if not
     *
     * @return tre if playing
     */
    @Override
    public boolean isPlaying() {
        return musicProvider.isPlaying();
    }

    /**
     * true if using java, false if not (and for example a C-library)
     *
     * @return true if using java
     */
    @Override
    public boolean isUsingJava() {
        return musicProvider.isUsingJava();
    }
}
