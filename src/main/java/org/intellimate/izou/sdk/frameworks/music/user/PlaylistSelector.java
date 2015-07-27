package org.intellimate.izou.sdk.frameworks.music.user;

import org.intellimate.izou.identification.Identifiable;
import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.resource.ResourceModel;
import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.frameworks.music.Capabilities;
import org.intellimate.izou.sdk.frameworks.music.player.Playlist;
import org.intellimate.izou.sdk.frameworks.music.resources.BroadcasterAvailablePlaylists;
import org.intellimate.izou.sdk.frameworks.music.resources.BroadcasterPlaylist;
import org.intellimate.izou.sdk.frameworks.music.resources.CapabilitiesResource;
import org.intellimate.izou.sdk.util.AddOnModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

/**
 * this class is used to get the available Playlists from the player. It can then build for example the PlayerRequest.
 * <p>
 * To use this class the player has to meet a few criteria:<br>
 * <ul>
 *  <li>the player must exist and be support the standard defined through the sdk</li>
 *  <li>the players-capabilities must allow requests from outside</li>
 *  <li>the players-capabilities must allow a requests with specified a specified playlist</li>
 *  <li>the players-capabilities signal its broadcasting playlists</li>
 * </ul><br>
 * an example:<br>
 * <pre>
 * {@code
 *
 * Identification playerID;
 * List<ResourceModel> resources = PlaylistSelector.getPlaylistsFromPlayer(playerID, this) //create a new PlaylistSelector
 *              .flatMap(playlistSelector -> playlistSelector.getPlayerRequest("jazz")) //maps to a PlayerRequest which should hold the playlist jazz
 *              .map(PlayerRequest::resourcesForExisting) //creates the resources needed to add to the event
 *              .orElseGet(ArrayList::new);
 * }
 * </pre>
 * </p>
 * @author LeanderK
 * @version 1.0
 */
public class PlaylistSelector {
    private final Identification player;
    private final Capabilities capabilities;
    private final List<String> playlists;
    private final Context context;
    private final Identifiable identifiable;

    /**
     * creates a new PlaylistSelector
     * @param player the player
     * @param capabilities the capabilities
     * @param playlists the playlists
     * @param context the context
     * @param identifiable
     */
    protected PlaylistSelector(Identification player, Capabilities capabilities, List<String> playlists, Context context, Identifiable identifiable) {
        this.player = player;
        this.capabilities = capabilities;
        this.playlists = playlists;
        this.context = context;
        this.identifiable = identifiable;
    }

    /**
     * provides all the names of the broadcasted playlists
     * @return a List with all the names
     */
    public List<String> getAvailablePlaylists() {
        return playlists;
    }

    /**
     * provides the ID of the associated player
     * @return the player
     */
    public Identification getPlayer() {
        return player;
    }

    /**
     * returns the Capabilities of the Player
     * @return the Capabilites
     */
    public Capabilities getCapabilities() {
        return capabilities;
    }

    /**
     * asks the Player for more information about the playlist.
     * <p>
     * This method may block for 1 second.
     * </p>
     * @param playlistName the name of the playlist
     * @return an optional answer of the player
     */
    public Optional<Playlist> getPlaylist(String playlistName) {
        try {
            return context.getResources().generateResource(BroadcasterPlaylist.createPlaylistRequest(player, playlistName))
                    .orElse(CompletableFuture.completedFuture(new ArrayList<>()))
                    .thenApply(list -> list.stream()
                            .filter(resourceModel -> resourceModel.getProvider().equals(player))
                            .findAny()
                            .flatMap(Playlist::importResource)
                    ).get(1, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            context.getLogger().error("unable to get Playlist");
            return Optional.empty();
        }
    }

    /**
     * asks the player for more information about the specified playlist and creates a PlayerRequest with the answer.
     * <p>
     * the resulting PlayerRequest is not permanent, which means that it will mute all other sound but is limited to
     * 10 minutes.
     * </p>
     * @param playlistName the name of the Playlist
     * @return an optional PlayerRequest
     */
    public Optional<PlayerRequest> getPlayerRequest(String playlistName) {
        return getPlayerRequest(playlistName, false);
    }

    /**
     * asks the player for more information about the specified playlist and creates a PlayerRequest with the answer.
     * @param playlistName the name of the Playlist
     * @param permanent true means the player can play indefinitely, but only if no one is currently using audio as
     *                  permanent. It will also not block. false is limited to 10 minutes playback, but will block.
     * @return the optional PlayerRequest
     */
    public Optional<PlayerRequest> getPlayerRequest(String playlistName, boolean permanent) {
        return getPlaylist(playlistName)
                .map(playlist -> PlayerRequest.createPlayerRequest(playlist, permanent, player, capabilities, context, identifiable));
    }

    /**
     * creates a new PlaylistSelector.
     * <p>
     * For this method to return a non-empty Optional the following criteria must be met:<br>
     * <ul>
     *  <li>the player must exist</li>
     *  <li>the players-capabilities must allow requests from outside</li>
     *  <li>the players-capabilities must allow a requests with specified a specified playlist</li>
     *  <li>the players-capabilities signal its broadcasting playlists</li>
     * </ul>
     * </p>
     * @param player the specified player
     * @param addOnModule the addOnModule used for Context etc.
     * @return the optional PlaylistSelector.
     */
    @SuppressWarnings("unused")
    public static Optional<PlaylistSelector> getPlaylistsFromPlayer(Identification player, AddOnModule addOnModule) {
        if (player == null || addOnModule == null)
            return Optional.empty();
        Function<Capabilities, CompletableFuture<Optional<PlaylistSelector>>> getPlaylistSelector = capabilities ->
                addOnModule.getContext().getResources()
                .generateResource(new BroadcasterAvailablePlaylists(player))
                .orElse(CompletableFuture.completedFuture(new ArrayList<>()))
                .thenApply(list -> list.stream()
                                .filter(resourceModel -> resourceModel.getProvider().equals(player))
                                .findAny()
                                .flatMap(BroadcasterAvailablePlaylists::getPlaylists)
                                .map(playlists ->
                                        new PlaylistSelector(player, capabilities, playlists, addOnModule.getContext(), addOnModule))
                );


        Function<List<ResourceModel>, Optional<Capabilities>> getCapabilities = resourceModels -> resourceModels.stream()
                .filter(resourceModel -> resourceModel.getProvider().equals(player))
                .findAny()
                .flatMap(resource -> Capabilities.importFromResource(resource, addOnModule.getContext()))
                .filter(capabilities -> {
                    if (!capabilities.handlesPlayRequestFromOutside()) {
                        addOnModule.getContext().getLogger().error("player does not handle play-request from outside");
                        return false;
                    }
                    if (!capabilities.hasPlayRequestDetailed()) {
                        addOnModule.getContext().getLogger().error("player does not handle trackInfo-request from outside");
                        return false;
                    }
                    if (!capabilities.isBroadcasting()) {
                        addOnModule.getContext().getLogger().error("player is not broadcasting playlists");
                        return false;
                    }
                    return true;
                });

        try {
            return addOnModule.getContext().getResources()
                    .generateResource(new CapabilitiesResource(player))
                    .orElse(CompletableFuture.completedFuture(new ArrayList<>()))
                    .thenApply(getCapabilities::apply)
                    .thenCompose(capabilities -> capabilities.map(getPlaylistSelector::apply)
                            .orElseGet(() -> CompletableFuture.completedFuture(Optional.<PlaylistSelector>empty()))
                    ).get(1, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            addOnModule.getContext().getLogger().error("unable to get PlaylistSelector");
            return Optional.empty();
        }
    }
}
