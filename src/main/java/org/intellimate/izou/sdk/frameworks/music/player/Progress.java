package org.intellimate.izou.sdk.frameworks.music.player;

/**
 * The Progress of the current track.
 * @author LeanderK
 * @version 1.0
 */
public class Progress {
    /**
     * if the audio has no specified beginning or end (for example a LiveStream of the audio output of the computer).
     * THIS DOES NOT MEAN THE INFORMATION IS UNAVAILABLE.
     */
    public static long NO_PROGRESS = -1;

    /**
     * the length of the track in miliseconds.
     */
    private final long length;
    private final long knownPosition;
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
    public long getPostion() {
        return knownPosition + (System.currentTimeMillis() - knownMillisTimeStamp);
    }
}
