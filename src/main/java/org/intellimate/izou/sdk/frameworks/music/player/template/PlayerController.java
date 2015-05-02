package org.intellimate.izou.sdk.frameworks.music.player.template;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.identification.IdentificationManager;
import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.activator.Activator;
import org.intellimate.izou.sdk.frameworks.music.events.PlayerCommand;
import org.intellimate.izou.sdk.frameworks.music.events.StartMusicRequest;
import org.intellimate.izou.sdk.frameworks.music.events.StopMusic;
import org.intellimate.izou.sdk.frameworks.music.player.Playlist;
import org.intellimate.izou.sdk.frameworks.music.player.Progress;
import org.intellimate.izou.sdk.frameworks.music.player.TrackInfo;
import org.intellimate.izou.sdk.frameworks.music.player.Volume;
import org.intellimate.izou.sdk.frameworks.music.resources.PlaylistResource;
import org.intellimate.izou.sdk.frameworks.music.resources.ProgressResource;
import org.intellimate.izou.sdk.frameworks.music.resources.TrackInfoResource;
import org.intellimate.izou.sdk.frameworks.music.resources.VolumeResource;

import java.util.Optional;

/**
 * you should use this class to control your Player
 * @author LeanderKfini
 * @version 1.0
 */
public abstract class PlayerController extends Activator {
    private final Player player;

    public PlayerController(Context context, String ID, Player player) {
        super(context, ID);
        this.player = player;
    }

    /**
     * starts the playing command
     */
    public void startPlaying() {
        startPlaying(null);
    }

    /**
     * starts the playing command
     * @param trackInfo the track to play
     */
    public void startPlaying(TrackInfo trackInfo) {
        Optional<Identification> ownIdentification = IdentificationManager.getInstance()
                .getIdentification(this);
        Optional<Identification> playerIdentification = IdentificationManager.getInstance()
                .getIdentification(player);
        if (!ownIdentification.isPresent() || !playerIdentification.isPresent()) {
            error("unable to obtain identification");
            return;
        }
        StartMusicRequest.createStartMusicRequest(ownIdentification.get(), playerIdentification.get(), trackInfo)
                .ifPresent(event -> fire(event, 5));

    }

    /**
     * stops the playing of the music
     */
    public void stopPlaying() {
        Optional<Identification> ownIdentification = IdentificationManager.getInstance()
                .getIdentification(this);
        Optional<Identification> playerIdentification = IdentificationManager.getInstance()
                .getIdentification(player);
        if (!ownIdentification.isPresent()|| !playerIdentification.isPresent()) {
            error("unable to obtain id");
            return;
        }
        StopMusic.createStopMusic(ownIdentification.get(), playerIdentification.get())
                .ifPresent(event -> fire(event, 5));
    }

    /**
     * commands the player to fulfill the command
     * @param command the command
     * @param playlist the playlist, or null if not needed
     * @param progress the progress, or null if not needed
     * @param trackInfo the trackInfo, or null if not needed
     * @param volume the volume, or null if not needed
     */
    public void command(String command, Playlist playlist, Progress progress, TrackInfo trackInfo, Volume volume) {
        Optional<Identification> ownIdentification = IdentificationManager.getInstance()
                .getIdentification(this);
        Optional<Identification> playerIdentification = IdentificationManager.getInstance()
                .getIdentification(player);
        if (!ownIdentification.isPresent()|| !playerIdentification.isPresent()) {
            error("unable to obtain id");
            return;
        }
        Optional<PlayerCommand> playerCommand = PlayerCommand.createPlayerCommand(ownIdentification.get(),
                playerIdentification.get(), command, player.getCapabilities(), getContext());
        if (playlist != null) {
            playerCommand.get().addResource(new PlaylistResource(ownIdentification.get(), playlist));
        }
        if (progress != null) {
            playerCommand.get().addResource(new ProgressResource(ownIdentification.get(), progress));
        }
        if (trackInfo != null) {
            playerCommand.get().addResource(new TrackInfoResource(ownIdentification.get(), trackInfo));
        }
        if (volume != null) {
            playerCommand.get().addResource(new VolumeResource(ownIdentification.get(), volume));
        }
        fire(playerCommand.get(), 5);
    }
}
