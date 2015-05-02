package org.intellimate.izou.sdk.frameworks.music.player.template;

import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.frameworks.music.Capabilities;
import org.intellimate.izou.sdk.frameworks.music.player.*;
import org.intellimate.izou.sdk.util.AddOnModule;

/**
 * this class is responsible for communication information
 * @author LeanderK
 * @version 1.0
 */
public class InformationProvider extends AddOnModule implements MusicResourceGenerator {
    private final MusicProvider musicProvider;
    /**
     * initializes the Module
     *
     * @param context the current Context
     * @param ID      the ID
     */
    public InformationProvider(Context context, String ID, MusicProvider musicProvider) {
        super(context, ID);
        this.musicProvider = musicProvider;
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
}
