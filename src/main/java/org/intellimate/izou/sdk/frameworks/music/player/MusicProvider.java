package org.intellimate.izou.sdk.frameworks.music.player;

import org.intellimate.izou.sdk.frameworks.music.Capabilities;
import org.intellimate.izou.sdk.frameworks.permanentSoundOutput.output.PermanentSoundUsed;

/**
 * @author LeanderK
 * @version 1.0
 */
public interface MusicProvider extends PermanentSoundUsed {
    /**
     * true if playing and false if not
     * @return tre if playing
     */
    @Override
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
}
