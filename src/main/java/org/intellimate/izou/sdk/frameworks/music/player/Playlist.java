package org.intellimate.izou.sdk.frameworks.music.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * a playlist is a list of TrackInfos
 * @author LeanderK
 * @version 1.0
 */
public class Playlist {
    private final List<TrackInfo> queue;
    private int position = 0;
    private final String name;
    private final List<PlaybackMode> playbackModes;

    public Playlist(List<TrackInfo> queue) {
        this.queue = queue;
        this.name = null;
        playbackModes = new ArrayList<>();
    }

    public Playlist(List<TrackInfo> queue, String name, List<PlaybackMode> playbackModes) {
        this.queue = queue;
        this.name = name;
        this.playbackModes = playbackModes;
    }

    /**
     * returns the queue
     * @return the List of TrackInfos
     */
    public List<TrackInfo> getQueue() {
        return queue;
    }

    /**
     * gets the name (optional)
     * <p>
     *     playlist doesn't need to have a name
     * </p>
     * @return the optional name
     */
    public Optional<String> getName() {
        return Optional.of(name);
    }

    /**
     * retruns the active PlaybackModes
     * @return the playbackModes
     */
    public List<PlaybackMode> getPlaybackModes() {
        return playbackModes;
    }
}
