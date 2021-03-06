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
    public static final String dataDescriptor = "izou.music.trackinfo.data";
    private final String data;
    public static final String yearDescriptor = "izou.music.trackinfo.year";
    private final String year;
    public static final String genreDescriptor = "izou.music.trackinfo.genre";
    private final String genre;
    public static final String bmpDescriptor = "izou.music.trackinfo.bmp";
    private final String bmp;
    public static final String durationDescriptor = "izou.music.trackinfo.duration";
    private final long duration;

    public TrackInfo(String name) {
        this.name = name;
        this.artist = null;
        this.album = null;
        albumCover = null;
        albumCoverFormat = null;
        data = null;
        year = null;
        genre = null;
        bmp = null;
        duration = -1;
    }

    public TrackInfo(String name, String artist, String album) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        albumCover = null;
        albumCoverFormat = null;
        data = null;
        year = null;
        genre = null;
        bmp = null;
        duration = -1;
    }

    public TrackInfo(String name, String artist, String album, byte[] albumCover, String albumCoverFormat) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.albumCover = albumCover;
        this.albumCoverFormat = albumCoverFormat;
        data = null;
        year = null;
        genre = null;
        bmp = null;
        duration = -1;
    }

    public TrackInfo(String name, String artist, String album, byte[] albumCover, String albumCoverFormat, String data) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.albumCover = albumCover;
        this.albumCoverFormat = albumCoverFormat;
        this.data = data;
        year = null;
        genre = null;
        bmp = null;
        duration = -1;
    }

    public TrackInfo(String name, String artist, String album, byte[] albumCover, String albumCoverFormat, String data, String year, String genre, String bmp, long duration) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.albumCover = albumCover;
        this.albumCoverFormat = albumCoverFormat;
        this.data = data;
        this.year = year;
        this.genre = genre;
        this.bmp = bmp;
        this.duration = duration;
    }

    /**
     * returns a String containing the Name of the Track
     * @return the optional Name
     */
    public Optional<String> getName() {
        return getOptionalOrEmtpyIfNull(name);
    }

    /**
     * returns a String containing the Name of the Artist
     * @return the optional Artist
     */
    public Optional<String> getArtist() {
        return getOptionalOrEmtpyIfNull(artist);
    }

    /**
     * returns a String containing the Name of the Album
     * @return the optional Artist
     */
    public Optional<String> getAlbum() {
        return getOptionalOrEmtpyIfNull(album);
    }

    /**
     * returns the album cover as bytes, format specified by: {@link #getAlbumCoverFormat()}.
     * @return the optional Album Cover
     */
    public Optional<byte[]> getAlbumCover() {
        return getOptionalOrEmtpyIfNull(albumCover);
    }

    /**
     * returns the format of the album cover, which can be obtained by calling {@link #getAlbumCover()}.
     * @return the optional Format
     */
    public Optional<String> getAlbumCoverFormat() {
        return getOptionalOrEmtpyIfNull(albumCoverFormat);
    }

    /**
     * returns the data of the TrackInfo.
     * <p>
     * this field is mostly internal, can be an url etc. should be used only if obtained from a player
     * </p>
     * @return the optional data
     */
    public Optional<String> getData() {
        return getOptionalOrEmtpyIfNull(data);
    }

    /**
     * returns a String containing the Year of the release
     * @return the optional Year of the release
     */
    public Optional<String> getYear() {
        return getOptionalOrEmtpyIfNull(year);
    }

    /**
     * returns a String containing the Genre
     * @return the optional Genre
     */
    public Optional<String> getGenre() {
        return getOptionalOrEmtpyIfNull(genre);
    }

    /**
     * returns a String containing the BMP
     * @return the optional BMP
     */
    public Optional<String> getBmp() {
        return getOptionalOrEmtpyIfNull(bmp);
    }

    /**
     * returns a long containing the Duration in milliseconds
     * @return the optional Duration
     */
    public Optional<Long> getDuration() {
        if (duration < 0) {
            return Optional.empty();
        } else {
            return Optional.of(duration);
        }
    }

    private <T> Optional<T> getOptionalOrEmtpyIfNull(T t) {
        if (t == null) {
            return Optional.empty();
        } else {
            return Optional.of(t);
        }
    }

    /**
     * retruns true if any of these arguments is already existing and would be overwritten, otherwise retruns false.
     * The method also returns true if everything is null except the albumCover-Format (maybe known in advance).
     * @param trackInfo the trackInfo to compare against
     * @return true if new
     */
    public boolean isNew(TrackInfo trackInfo) {
        return isNew(trackInfo.name,
                trackInfo.artist,
                trackInfo.album,
                trackInfo.albumCover,
                trackInfo.albumCoverFormat,
                trackInfo.data,
                trackInfo.year,
                trackInfo.genre,
                trackInfo.bmp,
                trackInfo.duration);
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
     * @param year the year
     * @param genre the genre
     * @param duration the duration
     * @param bmp the bmp
     * @return true if new
     */
    public boolean isNew(String name, String artist, String album, byte[] albumCover, String albumCoverFormat, String id, String year, String genre, String bmp, long duration) {
        if (name == null && artist == null && album == null && albumCover == null && id == null)
            return true;
        BiPredicate<String, String> compareStrings = (newString, oldString) ->
                oldString != null && newString != null && !oldString.equals(newString);
        if (compareStrings.test(name, this.name)) {
            return true;
        }
        if (compareStrings.test(artist, this.artist)) {
            return true;
        }
        if (compareStrings.test(album, this.album)) {
            return true;
        }
        if (compareStrings.test(id, this.data)) {
            return true;
        }
        if (compareStrings.test(albumCoverFormat, this.albumCoverFormat)) {
            return true;
        }
        if (compareStrings.test(year, this.year)) {
            return true;
        }
        if (compareStrings.test(genre, this.genre)) {
            return true;
        }
        if (this.duration > 0 && duration > 0 && this.duration == duration) {
            return true;
        }
        if (compareStrings.test(bmp, this.bmp)) {
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
     * @param trackInfo the data to compare
     * @return a trackinfo if some information was updated
     */
    public Optional<TrackInfo> update(TrackInfo trackInfo) {
        return update(trackInfo.name,
                trackInfo.artist,
                trackInfo.album,
                trackInfo.albumCover,
                trackInfo.albumCoverFormat,
                trackInfo.data,
                trackInfo.year,
                trackInfo.genre,
                trackInfo.bmp,
                trackInfo.duration);
    }

    /**
     * returns a trackinfo if some information was added and not overwritten (see isNew) AND a change occurred.
     * @param name the name
     * @param artist the artist
     * @param album the album
     * @param albumCover the album cover
     * @param coverFormat the album cover format
     * @param data the data
     * @param year the year
     * @param genre the genre
     * @param duration the duration
     * @param bmp the bmp
     * @return a trackInfo if some information was updated, and an empty optional if: 1. information would be ovewritten
     * or 2. no change occured
     */
    public Optional<TrackInfo> update(String name, String artist, String album, byte[] albumCover, String coverFormat, String data, String year, String genre, String bmp, long duration) {
        if (isNew(name, artist, album, albumCover, albumCoverFormat, data, year, genre, bmp, duration))
            return Optional.empty();
        boolean change = false;
        if (name != null && !name.equals(this.name)) {
            change = true;
        }
        if (artist != null && !artist.equals(this.artist)) {
            change = true;
        }
        if (album != null && !album.equals(this.album)) {
            change = true;
        }
        if (name != null && !name.equals(this.name)) {
            change = true;
        }
        if (albumCoverFormat != null && !albumCoverFormat.equals(this.albumCoverFormat)) {
            change = true;
        }
        if (data != null && !data.equals(this.data)) {
            change = true;
        }
        if (year != null && !year.equals(this.year)) {
            change = true;
        }
        if (genre != null && !genre.equals(this.genre)) {
            change = true;
        }
        if (duration > 0 && duration != this.duration) {
            change = true;
        }
        if (bmp != null && !bmp.equals(this.bmp)) {
            change = true;
        }
        if (!change)
            return Optional.empty();
        return Optional.of(new TrackInfo(
                this.name == null? name : this.name,
                this.artist == null? artist : this.artist,
                this.album == null? album : this.album,
                this.albumCover == null? albumCover : this.albumCover,
                this.albumCoverFormat == null? albumCoverFormat : this.albumCoverFormat,
                this.data == null? data : this.data,
                this.year == null? year : this.year,
                this.genre == null? genre : this.genre,
                this.bmp == null? bmp : this.bmp,
                this.duration < 0 ? duration : this.duration
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
        data.put(dataDescriptor, this.data);
        data.put(yearDescriptor, this.year);
        data.put(genreDescriptor, this.genre);
        data.put(durationDescriptor, this.duration);
        data.put(bmpDescriptor, this.bmp);
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
            String data = (String) hashMap.get(dataDescriptor);
            String year = (String) hashMap.get(yearDescriptor);
            String genre = (String) hashMap.get(genreDescriptor);
            long duration = (Long) hashMap.get(durationDescriptor);
            String bmp = (String) hashMap.get(bmpDescriptor);
            return Optional.of(new TrackInfo(name, artist, album, albumCover, albumCoverFormat, data, year, genre, bmp, duration));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrackInfo)) return false;

        TrackInfo trackInfo = (TrackInfo) o;

        if (name != null ? !name.equals(trackInfo.name) : trackInfo.name != null) return false;
        if (artist != null ? !artist.equals(trackInfo.artist) : trackInfo.artist != null) return false;
        if (album != null ? !album.equals(trackInfo.album) : trackInfo.album != null) return false;
        if (year != null ? !year.equals(trackInfo.year) : trackInfo.year != null) return false;
        if (genre != null ? !genre.equals(trackInfo.genre) : trackInfo.genre != null) return false;
        if (bmp != null ? !bmp.equals(trackInfo.bmp) : trackInfo.bmp != null) return false;
        return !(duration > 0l ? duration != trackInfo.duration : trackInfo.duration < 0);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (artist != null ? artist.hashCode() : 0);
        result = 31 * result + (album != null ? album.hashCode() : 0);
        result = 31 * result + (year != null ? year.hashCode() : 0);
        result = 31 * result + (genre != null ? genre.hashCode() : 0);
        result = 31 * result + (bmp != null ? bmp.hashCode() : 0);
        result = (int) (31 * result + (duration > 0 ? duration : 0));
        return result;
    }
}
