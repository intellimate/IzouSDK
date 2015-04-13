package org.intellimate.izou.sdk.frameworks.music.resources;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.frameworks.music.Capabilities;
import org.intellimate.izou.sdk.resource.Resource;

/**
 * A resource containing commands for the player.
 * @author LeanderK
 * @version 1.0
 */
public class CommandResource extends Resource<String> {
    public final static String ResourceID = "izou.music.command";
    public final static String PLAY = "play";
    public final static String PAUSE = "pause";
    public final static String STOP = "stop";
    public final static String SELECT_TRACK = "select";

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param command          the resource
     * @param capabilities the capabilities of the player
     */
    public CommandResource(Identification provider, String command, Capabilities capabilities) {
        super(ResourceID, provider, command);
        if (!verifyCommand(command))
            throw new IllegalArgumentException("IllegalCommand!");
        if (!verifyCapabilities(command, capabilities))
            throw new IllegalArgumentException("Player is not able to handle Command");
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param provider   the Provider of the Resource
     * @param command    the resource
     * @param consumer   the ID of the Consumer
     * @param capabilities the capabilities of the player
     */
    public CommandResource(Identification provider, String command, Identification consumer, Capabilities capabilities) {
        super(ResourceID, provider, command, consumer);
        if (!verifyCommand(command))
            throw new IllegalArgumentException("IllegalCommand!");
        if (!verifyCapabilities(command, capabilities))
            throw new IllegalArgumentException("Player is not able to handle Command");
    }

    /**
     * verifies that an command is not malformed
     * @param command the Command
     * @return false if malformed
     */
    private static boolean verifyCommand(String command) {
        return command.equals(PLAY) || command.equals(PAUSE) || command.equals(STOP) || command.equals(SELECT_TRACK);
    }

    /**
     * verifies that the player is capable of handling the command
     * @param command the command
     * @param capabilities the capabilities
     * @return true if capable, false if not
     */
    private static boolean verifyCapabilities(String command, Capabilities capabilities) {
        switch (command) {
            case PLAY: return capabilities.hasPlayPauseControl();
            case PAUSE: return capabilities.hasPlayPauseControl();
            case SELECT_TRACK: return capabilities.isAbleToSelectTrack();
            case STOP: return true;
        }
        return false;
    }
}
