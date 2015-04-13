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
        return data;
    }
}
