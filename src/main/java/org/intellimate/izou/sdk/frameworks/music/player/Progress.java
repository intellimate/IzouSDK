package org.intellimate.izou.sdk.frameworks.music.player;

import org.intellimate.izou.resource.ResourceModel;

import java.util.HashMap;
import java.util.Optional;

/**
 * The Progress of the current track.
 * @author LeanderK
 * @version 1.0
 */
@SuppressWarnings("unused")
public class Progress {
    /**
     * if the audio has no specified beginning or end (for example a LiveStream of the audio output of the computer).
     * THIS DOES NOT MEAN THE INFORMATION IS UNAVAILABLE.
     */
    public static long NO_PROGRESS = -1;

    /*
     * the length of the track in milliseconds.
     */
    public static final String lengthDescriptor = "izou.music.progress.length";
    private final long length;
    public static final String knownPositionDescriptor = "izou.music.progress.knownPosition";
    private final long knownPosition;
    public static final String knownMillisTimeStampDescriptor = "izou.music.progress.knownMillisTimeStamp";
    private final long knownMillisTimeStamp;

    public Progress(long length, long knownPosition) {
        this.length = length;
        this.knownPosition = knownPosition;
        knownMillisTimeStamp = System.currentTimeMillis();
    }

    public Progress(long length, long knownPosition, long knownMillisTimeStamp) {
        this.length = length;
        this.knownPosition = knownPosition;
        this.knownMillisTimeStamp = knownMillisTimeStamp;
    }

    /**
     * returns the length of the Track in milliseconds
     * @return the length
     */
    public long getLength() {
        return length;
    }

    /**
     * returns the current position in milliseconds
     * @return the position
     */
    public long getPosition() {
        return knownPosition + (System.currentTimeMillis() - knownMillisTimeStamp);
    }

    /**
     * exports the progress
     * @return a new HashMap
     */
    public HashMap<String, Long> export() {
        HashMap<String, Long> data = new HashMap<>();
        data.put(lengthDescriptor, length);
        data.put(knownPositionDescriptor, knownPosition);
        data.put(knownMillisTimeStampDescriptor, knownMillisTimeStamp);
        return data;
    }

    /**
     * creates a Progress-object from the resourceModel
     * @param resourceModel the ResourceModel
     * @return the optional Progress
     */
    public static Optional<Progress> importResource(ResourceModel resourceModel) {
        Object resource = resourceModel.getResource();
        try {
            //noinspection unchecked
            HashMap<String, Long> data = (HashMap<String, Long>) resource;
            long length = data.get(lengthDescriptor);
            long knownPosition = data.get(knownPositionDescriptor);
            long knownTimestamp = data.get(knownMillisTimeStampDescriptor);
            return Optional.of(new Progress(length, knownPosition, knownTimestamp));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
