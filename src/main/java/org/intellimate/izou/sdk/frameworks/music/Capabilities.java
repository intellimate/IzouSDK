package org.intellimate.izou.sdk.frameworks.music;

import org.intellimate.izou.sdk.Context;

import java.util.HashMap;

/**
 * This class has informations about the capabilities of the Music-Player. The default is always false.
 * @author LeanderK
 * @version 1.0
 */
public class Capabilities {
    public static final String playPauseControlDescriptor = "izou.music.capabilities.playpausecontrol";
    private boolean playPauseControl = false;
    public static final String playRequestTrackInfoDescriptor = "izou.music.capabilities.playrequesttrackinfo";
    private boolean playRequestTrackInfo = false;
    public static final String providesTrackInfoDescriptor = "izou.music.capabilities.providestrackinfo";
    private boolean providesTrackInfo = false;
    public static final String ableToSelectTrackDescriptor = "izou.music.capabilities.abletoselecttrack";
    private boolean ableToSelectTrack = false;
    public static final String nextPreviousDescriptor = "izou.music.capabilities.nextprevious";
    private boolean nextPrevious = false;
    public static final String ableToJumpDescriptor = "izou.music.capabilities.jump";
    private boolean ableToJump = false;
    public static final String playbackShuffleDescriptor = "izou.music.capabilities.playback.shuffle";
    private boolean playbackShuffle = false;
    public static final String playbackRepeatDescriptor = "izou.music.capabilities.playback.repeat";
    private boolean playbackRepeat = false;
    public static final String playbackRepeatSongDescriptor = "izou.music.capabilities.playback.repeatsong";
    private boolean playbackRepeatSong = false;
    public static final String playbackChangeableDescriptor = "izou.music.capabilities.playback.changable";
    private boolean playbackChangeable = false;
    private final Context context;

    public static Capabilities constructCapabilites(HashMap<String, Boolean> data, Context context) {
        Capabilities capabilities = new Capabilities(context);
        data.keySet().forEach(descriptor -> {
            switch (descriptor) {
                case playPauseControlDescriptor: capabilities.setPlayPauseControl(data.get(descriptor));
                    break;
                case playRequestTrackInfoDescriptor: capabilities.setPlayRequestTrackInfo(data.get(descriptor));
                    break;
                case providesTrackInfoDescriptor: capabilities.setProvidesTrackInfo(data.get(descriptor));
                    break;
                case ableToSelectTrackDescriptor: capabilities.setAbleToSelectTrack(data.get(descriptor));
                    break;
                case nextPreviousDescriptor: capabilities.setNextPrevious(data.get(descriptor));
                    break;
                case playbackChangeableDescriptor: capabilities.setPlaybackChangeable(data.get(descriptor));
                    break;
                case playbackRepeatDescriptor: capabilities.setPlaybackRepeat(data.get(descriptor));
                    break;
                case playbackRepeatSongDescriptor: capabilities.setPlaybackRepeatSong(data.get(descriptor));
                    break;
                case playbackShuffleDescriptor: capabilities.setPlaybackShuffle(data.get(descriptor));
                    break;
                default: context.getLogger().error("unkown command: " + descriptor);
                    break;
            }
        });
        return capabilities;
    }

    public Capabilities(Context context) {
        if (context == null) throw new NullPointerException("context is null");
        this.context = context;
    }

    /**
     * whether you can pause the playback without ending the player
     * @return true if able to, false if not
     */
    public boolean hasPlayPauseControl() {
        return playPauseControl;
    }

    /**
     * sets whether you can pause the playback without ending the player
     * @param playPauseControl true if the player is capable, false if not
     */
    public void setPlayPauseControl(boolean playPauseControl) {
        this.playPauseControl = playPauseControl;
    }

    /**
     * returns whether you can pause the playback without ending the player
     * @return true if able to, false if not
     */
    public boolean hasPlayRequestTrackInfo() {
        return playRequestTrackInfo;
    }

    /**
     * sets whether you can request a track with providing a TrackInfo
     * @param playRequestTrackInfo true if the player is capable, false if not
     */
    public void setPlayRequestTrackInfo(boolean playRequestTrackInfo) {
        this.playRequestTrackInfo = playRequestTrackInfo;
    }

    /**
     * returns whether the player may provide TrackInfo
     * @return true if able to, false if not
     */
    public boolean providesTrackInfo() {
        return providesTrackInfo;
    }

    /**
     * sets whether you may provide a trackinfo
     * @param providesTrackInfo true if the player is capable, false if not
     */
    public void setProvidesTrackInfo(boolean providesTrackInfo) {
        this.providesTrackInfo = providesTrackInfo;
    }

    /**
     * whether one is able to select a Track
     * @return true if able, false if not
     */
    public boolean isAbleToSelectTrack() {
        return ableToSelectTrack;
    }

    /**
     * sets whether one is able to select a Track
     * @param ableToSelectTrack true if able, false if not
     */
    public void setAbleToSelectTrack(boolean ableToSelectTrack) {
        this.ableToSelectTrack = ableToSelectTrack;
    }

    /**
     * whether one is able to select the next/previous track
     * @return true if able, false if not
     */
    public boolean hasNextPrevious() {
        return nextPrevious;
    }

    /**
     * sets whether one is able to select the next/previous track
     * @param nextPrevious true if able, false if not
     */
    public void setNextPrevious(boolean nextPrevious) {
        this.nextPrevious = nextPrevious;
    }

    /**
     * whether one is able to jump (select the current position of the track)
     * @return true if able, false if not
     */
    public boolean isAbleToJump() {
        return ableToJump;
    }

    /**
     * sets whether one is able to jump
     * @param ableToJump true if able, false if not
     */
    public void setAbleToJump(boolean ableToJump) {
        this.ableToJump = ableToJump;
    }

    /**
     * whether one is able to shuffle songs in a playlist
     * @return true if able, false if not
     */
    public boolean isPlaybackRepeat() {
        return playbackRepeat;
    }

    /**
     * sets whether the player is able to shuffle songs in a playlist
     * @param playbackRepeat true if able, false if not
     */
    public void setPlaybackRepeat(boolean playbackRepeat) {
        this.playbackRepeat = playbackRepeat;
    }

    /**
     * whether one is able to repeat songs in a playlist
     * @return true if able, false if not
     */
    public boolean isPlaybackRepeatSong() {
        return playbackRepeatSong;
    }

    /**
     * sets whether one is able to repeat songs in a playlist
     * @param playbackRepeatSong true if able, false if not
     */
    public void setPlaybackRepeatSong(boolean playbackRepeatSong) {
        this.playbackRepeatSong = playbackRepeatSong;
    }

    /**
     * whether one is able to change the playback
     * @return true if able, false if not
     */
    public boolean isPlaybackChangeable() {
        return playbackChangeable;
    }

    /**
     * sets whether one is able to change the playback
     * @param playbackChangeable true if able, false if not
     */
    public void setPlaybackChangeable(boolean playbackChangeable) {
        this.playbackChangeable = playbackChangeable;
    }

    /**
     * whether one is able to shuffle the playback
     * @return true if able, false if not
     */
    public boolean isPlaybackShuffle() {
        return playbackShuffle;
    }

    /**
     * sets whether one is able to shuffle the playback
     * @param playbackShuffle true if able, false if not
     */
    public void setPlaybackShuffle(boolean playbackShuffle) {
        this.playbackShuffle = playbackShuffle;
    }

    /**
     * the associated Context (mainly used for logging)
     * @return the context
     */
    public Context getContext() {
        return context;
    }

    public HashMap<String, Boolean> write() {
        HashMap<String, Boolean> data = new HashMap<>();
        data.put(playPauseControlDescriptor, playPauseControl);
        data.put(playRequestTrackInfoDescriptor, playRequestTrackInfo);
        data.put(providesTrackInfoDescriptor, providesTrackInfo);
        data.put(ableToSelectTrackDescriptor, ableToSelectTrack);
        data.put(nextPreviousDescriptor, nextPrevious);
        data.put(ableToJumpDescriptor, ableToJump);
        data.put(playbackChangeableDescriptor, playbackChangeable);
        data.put(playbackRepeatDescriptor, playbackRepeat);
        data.put(playbackRepeatSongDescriptor, playbackRepeatSong);
        data.put(playbackShuffleDescriptor, playbackShuffle);
        return data;
    }
}
