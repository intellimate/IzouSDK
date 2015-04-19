package org.intellimate.izou.sdk.frameworks.music.player;

import java.util.Optional;

/**
 * This class represents the Information about one track
 * @author LeanderK
 * @version 1.0
 */
public class TrackInfo {
    private final String name;
    private final String artist;
    private final String album;
    private final byte[] albumCover;
    private final String albumCoverFormat;
    private final Progress progress;
    private final String id;

    public TrackInfo(String name, String artist, String album) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        albumCover = null;
        albumCoverFormat = null;
        progress = null;
        id = null;
    }

    public TrackInfo(String name, String artist, String album, byte[] albumCover, String albumCoverFormat) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.albumCover = albumCover;
        this.albumCoverFormat = albumCoverFormat;
        progress = null;
        id = null;
    }

    public TrackInfo(String name, String artist, String album, Progress progress) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.progress = progress;
        albumCover = null;
        albumCoverFormat = null;
        id = null;
    }

    public TrackInfo(String name, String artist, String album, byte[] albumCover, String albumCoverFormat, Progress progress, String id) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.albumCover = albumCover;
        this.albumCoverFormat = albumCoverFormat;
        this.progress = progress;
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
     * returns the Progress of the track
     * @return the optional Progress
     */
    public Optional<Progress> getProgress() {
        return Optional.of(progress);
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
}
