package org.intellimate.izou.sdk.frameworks.music.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * a playlist is a list of TrackInfo-objects, it is immutable!
 * @author LeanderK
 * @version 1.0
 */
public class Playlist {
    private final List<TrackInfo> queue;
    private final int position;
    private final String name;
    private final List<PlaybackMode> playbackModes;

    public Playlist(List<TrackInfo> queue) {
        this.queue = Collections.unmodifiableList(queue);
        this.name = "";
        playbackModes = new ArrayList<>();
        this.position = 0;
    }

    public Playlist(List<TrackInfo> queue, String name, List<PlaybackMode> playbackModes, int position) {
        this.queue = Collections.unmodifiableList(queue);
        this.name = name;
        this.playbackModes = playbackModes;
        this.position = position;
    }

    /**
     * returns the queue
     * @return the List of TrackInfo-objects
     */
    public List<TrackInfo> getQueue() {
        return queue;
    }

    /**
     * returns the current played track
     * @return an instance of TrackInfo
     */
    public TrackInfo getCurrent() {
        return queue.get(position);
    }

    /**
     * returns the current position
     * @return the current position
     */
    public int getPosition() {
        return position;
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

    /**
     * creates a new Playlist, with the next Track as head (or an illegal position if it is the last!!)
     * @return a new Playlist
     */
    public Playlist nextTrack() {
        return new Playlist(queue, name, playbackModes, position + 1);
    }

    /**
     * creates a new Playlist, with the specified position
     * @return a new Playlist
     */
    public Playlist setNewPosition(int position) {
        return new Playlist(queue, name, playbackModes, position);
    }

    /**
     * updates the TrackInfo-Object
     * @param old the old TrackInfo
     * @param newTrackInfo the new TrackInfo
     * @return the resulting new Playlist
     */
    public Playlist update(TrackInfo old, TrackInfo newTrackInfo) {
        List<TrackInfo> list = new ArrayList<>(queue);
        list.set(list.indexOf(old), newTrackInfo);
        return new Playlist(queue, name, playbackModes, position);
    }
}
