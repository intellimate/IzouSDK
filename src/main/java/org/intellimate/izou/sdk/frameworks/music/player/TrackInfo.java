package org.intellimate.izou.sdk.frameworks.music.player;

import org.intellimate.izou.resource.ResourceModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.BiPredicate;

/**
 * This class represents the Information about one track, this class is immutable
 * @author LeanderK
 * @version 1.0
 */
@SuppressWarnings("unused")
public class TrackInfo {
    public static final String nameDescriptor = "izou.music.trackinfo.name";
    private final String name;
    public static final String artistDescriptor = "izou.music.trackinfo.artist";
    private final String artist;
    public static final String albumDescriptor = "izou.music.trackinfo.album";
    private final String album;
    public static final String albumCoverDescriptor = "izou.music.trackinfo.albumCover";
    private final byte[] albumCover;
    public static final String albumCoverFormatDescriptor = "izou.music.trackinfo.albumCoverFormat";
    private final String albumCoverFormat;
    public static final String idDescriptor = "izou.music.trackinfo.id";
    private final String id;

    public TrackInfo(String name, String artist, String album) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        albumCover = null;
        albumCoverFormat = null;
        id = null;
    }

    public TrackInfo(String name, String artist, String album, byte[] albumCover, String albumCoverFormat) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.albumCover = albumCover;
        this.albumCoverFormat = albumCoverFormat;
        id = null;
    }

    public TrackInfo(String name, String artist, String album, byte[] albumCover, String albumCoverFormat, String id) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.albumCover = albumCover;
        this.albumCoverFormat = albumCoverFormat;
        this.id = id;
    }



    /**
     * returns a String containing the Name of the Track
     * @return the optional Name
     */
    public Optional<String> getName() {
        return Optional.of(name);
    }

    /**
     * returns a String containing the Name of the Artist
     * @return the optional Artist
     */
    public Optional<String> getArtist() {
        return Optional.of(artist);
    }

    /**
     * returns a String containing the Name of the Artist
     * @return the optional Artist
     */
    public Optional<String> getAlbum() {
        return Optional.of(album);
    }

    /**
     * returns the album cover as bytes, format specified by: {@link #getAlbumCoverFormat()}.
     * @return the optional Album Cover
     */
    public Optional<byte[]> getAlbumCover() {
        return Optional.of(albumCover);
    }

    /**
     * returns the format of the album cover, which can be obtained by calling {@link #getAlbumCover()}.
     * @return the optional Format
     */
    public Optional<String> getAlbumCoverFormat() {
        return Optional.of(albumCoverFormat);
    }

    /**
     * returns the ID of the TrackInfo.
     * <p>
     * this field is mostly internal, can be an url etc. should be used only if obtained from a player
     * </p>
     * @return the optional id
     */
    public Optional<String> getId() {
        return Optional.of(id);
    }

    /**
     * retruns true if any of these arguments is already existing and would be overwritten, otherwise retruns false.
     * The method also returns true if everything is null except the albumCover-Format (maybe known in advance).
     * @param name the name
     * @param artist the artist
     * @param album the album
     * @param albumCover the album cover
     * @param albumCoverFormat the album cover format
     * @param id the id of the track
     * @return true if new
     */
    public boolean isNew(String name, String artist, String album, byte[] albumCover, String albumCoverFormat, String id) {
        if (name == null && artist == null && album == null && albumCover == null && id == null)
            return true;
        BiPredicate<String, String> compareStrings = (newString, oldString) ->
                newString != null && (oldString == null || !newString.equals(oldString));
        if (compareStrings.test(name, this.name)) {
            return true;
        }
        if (compareStrings.test(artist, this.artist)) {
            return true;
        }
        if (compareStrings.test(album, this.album)) {
            return true;
        }
        if (compareStrings.test(id, this.id)) {
            return true;
        }
        if (compareStrings.test(albumCoverFormat, this.albumCoverFormat)) {
            return true;
        }
        if (albumCover != null) {
            if (this.albumCover == null || !Arrays.equals(albumCover, this.albumCover))
                return true;
        }
        return false;
    }

    /**
     * returns a trackinfo if some information was added and not overwritten
     * @param name the name
     * @param artist the artist
     * @param album the album
     * @param albumCover the album cover
     * @param albumCoverFormat the album cover format
     * @param id the id of the track
     * @return a trackinfo if some information was updated
     */
    public Optional<TrackInfo> update(String name, String artist, String album, byte[] albumCover, String albumCoverFormat, String id) {
        if (isNew(name, artist, album, albumCover, albumCoverFormat, id))
            return Optional.empty();
        return Optional.of(new TrackInfo(
                this.name == null? name : this.name,
                this.artist == null? artist : this.artist,
                this.album == null? album : this.album,
                this.albumCover == null? albumCover : this.albumCover,
                this.albumCoverFormat == null? albumCoverFormat : this.albumCoverFormat,
                this.id == null? id : this.id
        ));
    }

    /**
     * exports the TrackInfo to a Hashmap
     * @return a HashMap
     */
    public HashMap<String, Object> export() {
        HashMap<String, Object> data = new HashMap<>();
        data.put(nameDescriptor, name);
        data.put(artistDescriptor, artist);
        data.put(albumDescriptor, albumDescriptor);
        data.put(albumCoverDescriptor, albumCover);
        data.put(albumCoverFormatDescriptor, albumCoverFormatDescriptor);
        data.put(idDescriptor, id);
        return data;
    }

    /**
     * returns the optional TrackInfo if the HashMap contains no malformed data
     * @param hashMap the data to import from
     * @return an optional
     */
    public static Optional<TrackInfo> importFromHashMap(HashMap<String, Object> hashMap) {
        try {
            String name = (String) hashMap.get(nameDescriptor);
            String album = (String) hashMap.get(albumDescriptor);
            String artist = (String) hashMap.get(artistDescriptor);
            byte[] albumCover = (byte[]) hashMap.get(albumCoverDescriptor);
            String albumCoverFormat = (String) hashMap.get(albumCoverFormatDescriptor);
            String id = (String) hashMap.get(idDescriptor);
            return Optional.of(new TrackInfo(name, artist, album, albumCover, albumCoverFormat, id));
        } catch (ClassCastException e) {
            return Optional.empty();
        }
    }

    /**
     * creates a TrackInfo from the resourceModel
     * @param resourceModel the resourceModel
     * @return the optional TrackInfo
     */
    public static Optional<TrackInfo> importFromResource(ResourceModel resourceModel) {
        Object resource = resourceModel.getResource();
        try {
            @SuppressWarnings("unchecked")
            HashMap<String, Object> hashMap = (HashMap<String, Object>) resource;
            return importFromHashMap(hashMap);
        } catch (ClassCastException e) {
            return Optional.empty();
        }
    }
}
