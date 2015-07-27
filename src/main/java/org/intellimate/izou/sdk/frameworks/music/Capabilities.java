package org.intellimate.izou.sdk.frameworks.music;

import org.intellimate.izou.resource.ResourceModel;
import org.intellimate.izou.sdk.Context;

import java.util.HashMap;
import java.util.Optional;

/**
 * This class has information about the capabilities of the Music-Player. The default is always false.
 * @author LeanderK
 * @version 1.0
 */
@SuppressWarnings("WeakerAccess")
public class Capabilities {
    @SuppressWarnings("SpellCheckingInspection")
    protected static final String playPauseControlDescriptor = "izou.music.capabilities.playpausecontrol";
    private boolean playPauseControl = false;
    @SuppressWarnings("SpellCheckingInspection")
    protected static final String playRequestOutsideDescriptor = "izou.music.capabilities.playrequestoutside";
    private boolean playRequestOutside = false;
    @SuppressWarnings("SpellCheckingInspection")
    protected static final String playRequestTrackInfoDescriptor = "izou.music.capabilities.playrequesttrackinfo";
    private boolean playRequestTrackInfo = false;
    @SuppressWarnings("SpellCheckingInspection")
    protected static final String providesTrackInfoDescriptor = "izou.music.capabilities.providestrackinfo";
    private boolean providesTrackInfo = false;
    @SuppressWarnings("SpellCheckingInspection")
    protected static final String ableToSelectTrackDescriptor = "izou.music.capabilities.abletoselecttrack";
    private boolean ableToSelectTrack = false;
    @SuppressWarnings("SpellCheckingInspection")
    protected static final String nextPreviousDescriptor = "izou.music.capabilities.nextprevious";
    private boolean nextPrevious = false;
    protected static final String ableToJumpDescriptor = "izou.music.capabilities.jump";
    private boolean ableToJump = false;
    protected static final String playbackShuffleDescriptor = "izou.music.capabilities.playback.shuffle";
    private boolean playbackShuffle = false;
    protected static final String playbackRepeatDescriptor = "izou.music.capabilities.playback.repeat";
    private boolean playbackRepeat = false;
    @SuppressWarnings("SpellCheckingInspection")
    protected static final String playbackRepeatSongDescriptor = "izou.music.capabilities.playback.repeatsong";
    private boolean playbackRepeatSong = false;
    @SuppressWarnings("SpellCheckingInspection")
    protected static final String playbackChangeableDescriptor = "izou.music.capabilities.playback.changable";
    private boolean playbackChangeable = false;
    protected static final String changeVolumeDescriptor = "izou.music.capabilities.volume";
    private boolean changeVolume = false;
    @SuppressWarnings("SpellCheckingInspection")
    protected static final String broadcastingDescriptor = "izou.music.capabilities.broadcasting";
    private boolean broadcasting = false;

    /**
     * creates a new Context
     * @param context the context to use
     * @deprecated context is not used anymore
     */
    @Deprecated
    public Capabilities(Context context) {
    }

    public Capabilities() {

    }

    /**
     * whether you can change the Volume from outside the player
     * @return true if able to, false if not
     */
    public boolean canChangeVolume() {
        return changeVolume;
    }

    /**
     * sets whether you can change the Volume from outside the player
     * @param changeVolume true if the player is capable, false if not
     */
    public void setChangeVolume(boolean changeVolume) {
        this.changeVolume = changeVolume;
    }

    /**
     * whether you can request playing from outside addons
     * @return true if able to, false if not
     */
    public boolean handlesPlayRequestFromOutside() {
        return playRequestOutside;
    }

    /**
     * sets whether you can request playing from outside addons
     * @param playRequestOutside true if the player is capable, false if not
     */
    public void setPlayRequestOutside(boolean playRequestOutside) {
        this.playRequestOutside = playRequestOutside;
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
     * returns whether you can obtain available Playlists via the BroadcasterAvailablePlaylists-resource.
     * @return true if able, false if not
     */
    public boolean isBroadcasting() {
        return broadcasting;
    }

    /**
     * sets whether you can obtain available Playlists via the BroadcasterAvailablePlaylists-resource.
     * @param broadcasting true if able, false if not
     */
    public void setBroadcasting(boolean broadcasting) {
        this.broadcasting = broadcasting;
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
        data.put(playRequestOutsideDescriptor, playRequestOutside);
        data.put(changeVolumeDescriptor, changeVolume);
        data.put(broadcastingDescriptor, broadcasting);
        return data;
    }

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
                case playRequestOutsideDescriptor: capabilities.setPlayRequestOutside(data.get(descriptor));
                    break;
                case changeVolumeDescriptor: capabilities.setChangeVolume(data.get(descriptor));
                    break;
                case ableToJumpDescriptor: capabilities.setAbleToJump(data.get(descriptor));
                    break;
                case broadcastingDescriptor: capabilities.setBroadcasting(data.get(descriptor));
                    break;
                default: context.getLogger().error("unkown command: " + descriptor);
                    break;
            }
        });
        return capabilities;
    }

    /**
     * creates a TrackInfo from the resourceModel
     * @param resourceModel the resourceModel
     * @return the optional TrackInfo
     */
    public static Optional<Capabilities> importFromResource(ResourceModel resourceModel, Context context) {
        Object resource = resourceModel.getResource();
        try {
            @SuppressWarnings("unchecked")
            HashMap<String, Boolean> hashMap = (HashMap<String, Boolean>) resource;
            return Optional.of(constructCapabilites(hashMap, context));
        } catch (ClassCastException e) {
            return Optional.empty();
        }
    }
}
