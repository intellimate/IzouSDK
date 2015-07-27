package org.intellimate.izou.sdk.frameworks.music.user;

import org.intellimate.izou.identification.Identifiable;
import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.identification.IdentificationManager;
import org.intellimate.izou.resource.ResourceModel;
import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.frameworks.music.Capabilities;
import org.intellimate.izou.sdk.frameworks.music.player.Playlist;
import org.intellimate.izou.sdk.frameworks.music.player.TrackInfo;
import org.intellimate.izou.sdk.frameworks.music.player.Volume;
import org.intellimate.izou.sdk.frameworks.music.resources.*;
import org.intellimate.izou.sdk.util.AddOnModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * this is a simple class which should, added as a resource to an Event, request the Player to play the selected
 * track or playlist.
 * <p>
 * To use this class the player has to meet a few criteria:<br>
 * <ul>
 *  <li>the player must exist and be support the standard defined through the sdk</li>
 *  <li>the players-capabilities must allow requests from outside</li>
 *  <li>(maybe, depends on the create-method) the players-capabilities must allow a requests with specified a specified playlist/trackInfo</li>
 * </ul><br>
 * an example:<br>
 * <pre>
 * {@code
 *
 * Identification playerID;
 * Playlist playlist;
 * List<ResourceModel> resources = PlayerRequest.createPlayerRequest(playlist, player, this) //create a new PlayerRequest
 *          .map(PlayerRequest::resourcesForExisting) //creates the resources needed to add to the event
 *          .orElseGet(ArrayList::new);
 * }
 * </pre>
 * </p>
 * </p>
 * @author LeanderK
 * @version 1.0
 */
public class PlayerRequest {
    private final TrackInfo trackInfo;
    private final Playlist playlist;
    private final boolean permanent;
    private final Identification player;
    private final Capabilities capabilities;
    private final Context context;
    private final Identifiable identifiable;
    private Volume volume = null;

    /**
     * internal Constructor
     * @param trackInfo the trackInfo
     * @param playlist the playlist
     * @param permanent whether the Request is permanent
     * @param player the player
     * @param capabilities the capabilities
     * @param context the context
     * @param identifiable the identifiable
     */
    protected PlayerRequest(TrackInfo trackInfo, Playlist playlist, boolean permanent, Identification player,
                            Capabilities capabilities, Context context, Identifiable identifiable) {
        this.trackInfo = trackInfo;
        this.playlist = playlist;
        this.permanent = permanent;
        this.player = player;
        this.capabilities = capabilities;
        this.context = context;
        this.identifiable = identifiable;
    }

    /**
     * internal Constructor
     * @param trackInfo the trackInfo
     * @param playlist the playlist
     * @param permanent whether the Request is permanent
     * @param player the player
     * @param capabilities the capabilities
     * @param context the Context
     * @param identifiable the identifiable
     * @param volume the Volume to set to
     */
    protected PlayerRequest(TrackInfo trackInfo, Playlist playlist, boolean permanent, Identification player,
                            Capabilities capabilities, Context context, Identifiable identifiable, Volume volume) {
        this.trackInfo = trackInfo;
        this.playlist = playlist;
        this.permanent = permanent;
        this.player = player;
        this.capabilities = capabilities;
        this.context = context;
        this.identifiable = identifiable;
        this.volume = volume;
    }

    /**
     * sets the Volume if the Player supports it.
     * @param volume the Volume to set to
     * @return true if the Player supports setting the Volume
     */
    public boolean setVolume(Volume volume) {
        if (capabilities.canChangeVolume()) {
            this.volume = volume;
            return true;
        }
        return false;
    }

    /**
     * tries to set the Volume of the PlayerRequest.
     * <p>
     * if the Player supports the Change of the Volume, it will create a new PlayerRequest and return it, if not it
     * returns this.
     * </p>
     * @param volume the Volume to set to
     * @return a new PlayerRequest or this.
     */
    public PlayerRequest trySetVolume(Volume volume) {
        if (capabilities.canChangeVolume()) {
            return new PlayerRequest(trackInfo, playlist, permanent, player, capabilities, context, identifiable, volume);
        }
        return this;
    }

    /**
     * returns a List of Resources that can be added to an already existing event.
     * <p>
     * This causes the Addon to block the Event in the OutputPlugin lifecycle of the Event.
     * </p>
     * @return a List of Resources
     */
    @SuppressWarnings("unused")
    public List<ResourceModel> resourcesForExisting() {
        List<ResourceModel> resourceModels = new ArrayList<>();
        IdentificationManager.getInstance().getIdentification(identifiable)
                .map(id -> new MusicUsageResource(id, true))
                .ifPresent(resourceModels::add);
        if (volume != null) {
            IdentificationManager.getInstance().getIdentification(identifiable)
                    .map(id -> new VolumeResource(id, volume))
                    .ifPresent(resourceModels::add);
        }
        if (playlist != null) {
            IdentificationManager.getInstance().getIdentification(identifiable)
                    .map(id -> new PlaylistResource(id, playlist))
                    .ifPresent(resourceModels::add);
        }
        if (trackInfo != null) {
            IdentificationManager.getInstance().getIdentification(identifiable)
                    .map(id -> new TrackInfoResource(id, trackInfo))
                    .ifPresent(resourceModels::add);
        }
        return resourceModels;
    }

    /**
     * helper method for PlaylistSelector
     * @param playlist the playlist selected
     * @param permanent the permanent addon
     * @param player the player
     * @param capabilities the capabilities
     * @param context the context
     * @param identifiable the identifiable
     * @return a PlayerRequest
     */
    static PlayerRequest createPlayerRequest(Playlist playlist, boolean permanent, Identification player, Capabilities capabilities, Context context, Identifiable identifiable) {
        return  new PlayerRequest(null, playlist, permanent, player, capabilities, context, identifiable);
    }

    /**
     * creates a new PlayerRequest.
     * <p>
     * For this method to return a non-empty Optional the following criteria must be met:<br>
     * <ul>
     *  <li>the player must exist and be support the standard defined through the sdk</li>
     *  <li>the players-capabilities must allow requests from outside</li>
     * </ul>
     * </p>
     * @param permanent true means the player can play indefinitely, but only if no one is currently using audio as
     *                  permanent, false is limited to 10 minutes playback.
     * @param player the player to target
     * @param source the addOnModule used for Context etc.
     * @return the optional PlayerRequest
     */
    @SuppressWarnings("unused")
    public static Optional<PlayerRequest> createPlayerRequest(boolean permanent, Identification player, AddOnModule source) {
        if (player == null || source == null)
            return Optional.empty();
        try {
            return source.getContext().getResources()
                    .generateResource(new CapabilitiesResource(player))
                    .orElse(CompletableFuture.completedFuture(new ArrayList<>()))
                    .thenApply(list -> list.stream()
                                    .filter(resourceModel -> resourceModel.getProvider().equals(player))
                                    .findAny()
                                    .flatMap(resource -> Capabilities.importFromResource(resource, source.getContext()))
                    ).get(1, TimeUnit.SECONDS)
                    .filter(capabilities -> {
                        if (!capabilities.handlesPlayRequestFromOutside()) {
                            source.getContext().getLogger().error("player does not handle play-request from outside");
                            return false;
                        }
                        return true;
                    })
                    .map(capabilities -> new PlayerRequest(null, null, permanent, player, capabilities, source.getContext(), source));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            source.getContext().getLogger().error("unable to obtain capabilities");
            return Optional.empty();
        }
    }

    /**
     * creates a new PlayerRequest.
     * <p>
     * the resulting PlayerRequest is not permanent, which means that it will mute all other sound but is limited to
     * 10 minutes.<br>
     * For this method to return a non-empty Optional the following criteria must be met:<br>
     * <ul>
     *  <li>the player must exist and be support the standard defined through the sdk</li>
     *  <li>the players-capabilities must allow requests from outside</li>
     *  <li>the players-capabilities must allow a requests with specified a specified playlist/trackInfo</li>
     * </ul>
     * </p>
     * @param trackInfo the trackInfo to pass with the request
     * @param player the player to target
     * @param source the addOnModule used for Context etc.
     * @return the optional PlayerRequest
     */
    @SuppressWarnings("unused")
    public static Optional<PlayerRequest> createPlayerRequest(TrackInfo trackInfo, Identification player, AddOnModule source) {
        return createPlayerRequest(trackInfo, false, player, source);
    }

    /**
     * creates a new PlayerRequest.
     * <p>
     * For this method to return a non-empty Optional the following criteria must be met:<br>
     * <ul>
     *  <li>the player must exist and be support the standard defined through the sdk</li>
     *  <li>the players-capabilities must allow requests from outside</li>
     *  <li>the players-capabilities must allow a requests with specified a specified playlist/trackInfo</li>
     * </ul>
     * </p>
     * @param trackInfo the trackInfo to pass with the request
     * @param permanent true means the player can play indefinitely, but only if no one is currently using audio as
     *                  permanent, false is limited to 10 minutes playback.
     * @param player the player to target
     * @param source the addOnModule used for Context etc.
     * @return the optional PlayerRequest
     */
    @SuppressWarnings("unused")
    public static Optional<PlayerRequest> createPlayerRequest(TrackInfo trackInfo, boolean permanent, Identification player, AddOnModule source) {
        if (trackInfo == null ||player == null || source == null)
            return Optional.empty();
        try {
            return source.getContext().getResources()
                    .generateResource(new CapabilitiesResource(player))
                    .orElse(CompletableFuture.completedFuture(new ArrayList<>()))
                    .thenApply(list -> list.stream()
                                    .filter(resourceModel -> resourceModel.getProvider().equals(player))
                                    .findAny()
                                    .flatMap(resource -> Capabilities.importFromResource(resource, source.getContext()))
                    ).get(1, TimeUnit.SECONDS)
                    .filter(capabilities -> {
                        if (!capabilities.handlesPlayRequestFromOutside()) {
                            source.getContext().getLogger().error("player does not handle play-request from outside");
                            return false;
                        }
                        if (!capabilities.hasPlayRequestDetailed()) {
                            source.getContext().getLogger().error("player does not handle trackInfo-request from outside");
                            return false;
                        }
                        return true;
                    })
                    .map(capabilities -> new PlayerRequest(trackInfo, null, permanent, player, capabilities, source.getContext(), source));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            source.getContext().getLogger().error("unable to obtain capabilities");
            return Optional.empty();
        }
    }

    /**
     * creates a new PlayerRequest.
     * <p>
     * the resulting PlayerRequest is not permanent, which means that it will mute all other sound but is limited to
     * 10 minutes.<br>
     * For this method to return a non-empty Optional the following criteria must be met:<br>
     * <ul>
     *  <li>the player must exist and be support the standard defined through the sdk</li>
     *  <li>the players-capabilities must allow requests from outside</li>
     *  <li>the players-capabilities must allow a requests with specified a specified playlist/trackInfo</li>
     * </ul>
     * </p>
     * @param playlist the playlist to pass with the request
     * @param player the player to target
     * @param source the addOnModule used for Context etc.
     * @return the optional PlayerRequest
     */
    @SuppressWarnings("unused")
    public static Optional<PlayerRequest> createPlayerRequest(Playlist playlist, Identification player, AddOnModule source) {
        return createPlayerRequest(playlist, false, player, source);
    }

    /**
     * creates a new PlayerRequest.
     * <p>
     * For this method to return a non-empty Optional the following criteria must be met:<br>
     * <ul>
     *  <li>the player must exist and be support the standard defined through the sdk</li>
     *  <li>the players-capabilities must allow requests from outside</li>
     *  <li>the players-capabilities must allow a requests with specified a specified playlist/trackInfo</li>
     * </ul>
     * </p>
     * @param playlist the playlist to pass with the request
     * @param permanent true means the player can play indefinitely, but only if no one is currently using audio as
     *                  permanent, false is limited to 10 minutes playback.
     * @param player the player to target
     * @param source the addOnModule used for Context etc.
     * @return the optional PlayerRequest
     */
    @SuppressWarnings("unused")
    public static Optional<PlayerRequest> createPlayerRequest(Playlist playlist, boolean permanent, Identification player, AddOnModule source) {
        if (playlist == null ||player == null || source == null)
            return Optional.empty();
        try {
            return source.getContext().getResources()
                    .generateResource(new CapabilitiesResource(player))
                    .orElse(CompletableFuture.completedFuture(new ArrayList<>()))
                    .thenApply(list -> list.stream()
                                    .filter(resourceModel -> resourceModel.getProvider().equals(player))
                                    .findAny()
                                    .flatMap(resource -> Capabilities.importFromResource(resource, source.getContext()))
                    ).get(1, TimeUnit.SECONDS)
                    .filter(capabilities -> {
                        if (!capabilities.handlesPlayRequestFromOutside()) {
                            source.getContext().getLogger().error("player does not handle play-request from outside");
                            return false;
                        }
                        if (!capabilities.hasPlayRequestDetailed()) {
                            source.getContext().getLogger().error("player does not handle playlist-request from outside");
                            return false;
                        }
                        if (!playlist.verify(capabilities)) {
                            source.getContext().getLogger().error("player can not handle the playlist, probably illegal PlaybackModes");
                            return false;
                        }
                        return true;
                    })
                    .map(capabilities -> new PlayerRequest(null, playlist, permanent, player, capabilities, source.getContext(), source));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            source.getContext().getLogger().debug("unable to obtain capabilities");
            return Optional.empty();
        }
    }
}