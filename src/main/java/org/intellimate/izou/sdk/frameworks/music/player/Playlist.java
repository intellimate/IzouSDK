package org.intellimate.izou.sdk.frameworks.music.player;

import org.intellimate.izou.resource.ResourceModel;

import java.util.*;

/**
 * a playlist is a list of TrackInfo-objects, it is immutable!
 * @author LeanderK
 * @version 1.0
 */
@SuppressWarnings("unused")
public class Playlist {
    public static final String QUEUE_DESCRIPTOR = "izou.music.playlist.queue";
    private final List<TrackInfo> queue;
    public static final String POSITION_DESCRIPTOR = "izou.music.playlist.position";
    private final int position;
    public static final String NAME_DESCRIPTOR = "izou.music.playlist.name";
    private final String name;
    public static final String PLAYBACK_MODE_DESCRIPTOR = "izou.music.playlist.playbackmode";
    private final List<PlaybackMode> playbackModes;
    public static final String DATA_DESCRIPTOR = "izou.music.playlist.data";
    private final String data;

    public Playlist(String name) {
        this(Collections.emptyList(), name, Collections.emptyList(), 0, null);
    }

    public Playlist(List<TrackInfo> queue) {
        this(queue, null, Collections.emptyList(), 0, null);
    }

    public Playlist(List<TrackInfo> queue, String name, List<PlaybackMode> playbackModes, int position) {
        this(queue, name, playbackModes, position, null);
    }

    public Playlist(List<TrackInfo> queue, String name, List<PlaybackMode> playbackModes, int position, String data) {
        this.queue = Collections.unmodifiableList(new ArrayList<>(queue));
        this.position = position;
        this.name = name;
        this.playbackModes = new ArrayList<>(playbackModes);
        this.data = data;
    }

    /**
     * returns the queue
     * @return the List of TrackInfo-objects
     */
    public List<TrackInfo> getQueue() {
        return queue;
    }

    /**
     * returns the current played track or null
     * @return an instance of TrackInfo
     */
    public TrackInfo getCurrent() {
        TrackInfo trackInfo = null;
        try {
            trackInfo = queue.get(position);
        } catch (IndexOutOfBoundsException e) {
            trackInfo = null;
        }
        return trackInfo;
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
        if (name == null) {
            return Optional.empty();
        } else {
            return Optional.of(name);
        }
    }

    /**
     * retruns the active PlaybackModes
     * @return the playbackModes
     */
    public List<PlaybackMode> getPlaybackModes() {
        return playbackModes;
    }

    /**
     *returns the associated data (not really specified, specified by implementation)
     * @return the optional data-element
     */
    public Optional<String> getData() {
        if (data == null) {
            return Optional.empty();
        } else {
            return Optional.of(data);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Playlist)) return false;

        Playlist playlist = (Playlist) o;

        if (position != playlist.position) return false;
        if (queue != null ? !queue.equals(playlist.queue) : playlist.queue != null) return false;
        if (name != null ? !name.equals(playlist.name) : playlist.name != null) return false;
        if (data != null ? !data.equals(playlist.data) : playlist.data != null) return false;
        return !(playbackModes != null ? !playbackModes.equals(playlist.playbackModes) : playlist.playbackModes != null);

    }

    @Override
    public int hashCode() {
        int result = queue != null ? queue.hashCode() : 0;
        result = 31 * result + position;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (playbackModes != null ? playbackModes.hashCode() : 0);
        return result;
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
     * @param position the position to set to
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

    /**
     * Shuffles the playlist and returns the shuffled playlist, so the original stays intact.
     * Only the part of the playlist after the current position is shuffled.
     *
     * @return a shuffled copy of this playlist
     */
    public Playlist shuffle() {
        int position = getPosition();
        long seed = System.nanoTime();

        if (position >= 0 && position < queue.size()) {
            List<TrackInfo> trackInfos = queue.subList(0, position);
            List<TrackInfo> notPlayed = queue.subList(position, queue.size());

            List<TrackInfo> shuffledNotPlayed = new ArrayList<>(notPlayed);
            Collections.shuffle(shuffledNotPlayed, new Random(seed));
            trackInfos.addAll(shuffledNotPlayed);
            return new Playlist(trackInfos);
        } else {
            List<TrackInfo> trackInfos = new ArrayList<>(queue);
            Collections.shuffle(trackInfos, new Random(seed));
            return new Playlist(trackInfos);
        }
    }

    /**
     * exports the Playlist to a HashMap
     * @return a HashMap
     */
    public HashMap<String, Object> export() {
        HashMap<String, Object> data = new HashMap<>();
        for (int i = 0; i < queue.size(); i++) {
            data.put(QUEUE_DESCRIPTOR+i, queue.get(i).export());
        }
        for (int i = 0; i < playbackModes.size(); i++) {
            data.put(PLAYBACK_MODE_DESCRIPTOR+i, playbackModes.get(i).name());
        }
        data.put(NAME_DESCRIPTOR, name);
        data.put(POSITION_DESCRIPTOR, position);
        data.put(DATA_DESCRIPTOR, this.data);
        return data;
    }

    /**
     * constructs (if no errors were found) the Playlist from the Resource
     * @param resourceModel the resourceModel to import from
     * @return the optional Playlist
     */
    public static Optional<Playlist> importResource(ResourceModel resourceModel) {
        Object resource = resourceModel.getResource();
        try {
            //noinspection unchecked
            HashMap<String, Object> data = (HashMap<String, Object>) resource;
            ArrayList<TrackInfo> queue = new ArrayList<>();
            ArrayList<PlaybackMode> playbackModes = new ArrayList<>();
            final String[] name = {null};
            final int[] position = {-1};
            final String[] dataString = {null};
            data.entrySet().forEach(entry -> {
                if (entry.getKey().startsWith(QUEUE_DESCRIPTOR)) {
                    int index = Integer.parseInt(entry.getKey().replace(QUEUE_DESCRIPTOR, ""));
                    //noinspection unchecked
                    TrackInfo.importFromHashMap((HashMap<String, Object>) entry.getValue())
                            .ifPresent(trackInfo -> queue.add(index, trackInfo));
                } else if (entry.getKey().startsWith(PLAYBACK_MODE_DESCRIPTOR)) {
                    try {
                        int index = Integer.parseInt(entry.getKey().replace(PLAYBACK_MODE_DESCRIPTOR, ""));
                        //noinspection unchecked
                        playbackModes.add(index, PlaybackMode.valueOf((String) entry.getValue()));
                    } catch (IllegalArgumentException ignored) {
                        //happens when the name is not present...maybe future sdks define other playbackModes
                    }
                } else if (entry.getKey().equals(NAME_DESCRIPTOR)) {
                    name[0] = (String) entry.getValue();
                } else if (entry.getKey().equals(POSITION_DESCRIPTOR)) {
                    position[0] = (int) entry.getValue();
                } else if (entry.getKey().equals(DATA_DESCRIPTOR)) {
                    dataString[0] = (String) entry.getValue();
                }
            });
            return Optional.of(new Playlist(queue, name[0] , playbackModes, position[0], dataString[0]));
        } catch (ClassCastException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
