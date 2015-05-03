package org.intellimate.izou.sdk.frameworks.music.player;

import org.intellimate.izou.sdk.frameworks.music.Capabilities;
import org.intellimate.izou.sdk.frameworks.permanentSoundOutput.output.PermanentSoundUsed;

/**
 * here are the methods for the interfaces to access all needed states.
 * @author LeanderK
 * @version 1.0
 */
public interface MusicProvider extends PermanentSoundUsed {
    /**
     * true if using the sound output and false if not
     * @return true if  using the sound output
     */
    @Override
    boolean isOutputRunning();

    /**
     * true if playing and false if not
     * @return tre if playing
     */
    boolean isPlaying();


    /**
     * gets the current Playlist
     * @return the current Playlist
     */
    Playlist getCurrentPlaylist();

    /**
     * gets the Volume
     * @return the volume
     */
    Volume getVolume();

    /**
     * gets the Progress
     * @return the Progress
     */
    Progress getCurrentProgress();

    /**
     * gets the Capabilities of the Player
     * @return and instance of Capabilities
     */
    Capabilities getCapabilities();

    /**
     * gets the PlaybackState of the Player
     * @return the PlaybackState
     */
    PlaybackState getPlaybackState();
}
