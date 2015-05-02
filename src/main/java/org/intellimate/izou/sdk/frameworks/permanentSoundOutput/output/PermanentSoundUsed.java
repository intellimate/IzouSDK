package org.intellimate.izou.sdk.frameworks.permanentSoundOutput.output;

/**
 * base-interface used for the other helper interfaces
 * @author LeanderK
 * @version 1.0
 */
public interface PermanentSoundUsed {
    /**
     * true if using the sound output and false if not
     * @return true if  using the sound output
     */
    boolean isOutputRunning();
}
