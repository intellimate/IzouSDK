package org.intellimate.izou.sdk.frameworks.music.player;

import java.util.Optional;

/**
 * this class represents a symbolic volume (int from 0 to 100 or -1 for mute)
 * @author LeanderK
 * @version 1.0
 */
public class Volume {
    private final int volume;

    private Volume(int volume) {
        this.volume = volume;
    }

    /**
     * returns the volume
     * @return the volume
     */
    public int getVolume() {
        return volume;
    }

    public static Optional<Volume> createVolume(int volume) {
        if ((volume >= 0 && volume <= 100) || volume == -1) {
            return Optional.of(new Volume(volume));
        } else {
            return Optional.empty();
        }
    }
}
