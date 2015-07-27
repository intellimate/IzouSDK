package org.intellimate.izou.sdk.frameworks.music.user;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.frameworks.music.Capabilities;
import org.intellimate.izou.sdk.frameworks.music.player.Playlist;
import org.intellimate.izou.sdk.frameworks.music.player.TrackInfo;
import org.intellimate.izou.sdk.frameworks.music.resources.CapabilitiesResource;
import org.intellimate.izou.sdk.util.AddOnModule;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * this is a simple class which should, added as a resource to an Event, request the Player to play the selected
 * track or playlist. If you use this class the player must handle request from outside
 * How to use: Create the PlayerRequest and as a last step call buildResource().
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

    protected PlayerRequest(TrackInfo trackInfo, Playlist playlist, boolean permanent, Identification player, Capabilities capabilities, Context context) {
        this.trackInfo = trackInfo;
        this.playlist = playlist;
        this.permanent = permanent;
        this.player = player;
        this.capabilities = capabilities;
        this.context = context;
    }

    @SuppressWarnings("unused")
    public static Optional<PlayerRequest> createPlayerRequest(TrackInfo trackInfo, boolean permanent, Identification player, AddOnModule source) {
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
                    .map(capabilities -> new PlayerRequest(trackInfo, null, permanent, player, capabilities, source.getContext()));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            source.getContext().getLogger().error("unable to obtain capabilities");
            return Optional.empty();
        }
    }

    @SuppressWarnings("unused")
    public static Optional<PlayerRequest> createPlayerRequest(Playlist playlist, boolean permanent, Identification player, AddOnModule source) {
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
                    .map(capabilities -> new PlayerRequest(null, playlist, permanent, player, capabilities, source.getContext()));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            source.getContext().getLogger().debug("unable to obtain capabilities");
            return Optional.empty();
        }
    }
}