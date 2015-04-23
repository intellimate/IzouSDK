package org.intellimate.izou.sdk.frameworks.music.resources;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.frameworks.music.Capabilities;
import org.intellimate.izou.sdk.resource.Resource;

import java.util.Optional;

/**
 * A resource containing commands for the player.
 * @author LeanderK
 * @version 1.0
 */
public class CommandResource extends Resource<String> {
    public final static String ResourceID = "izou.music.resource.command";
    public final static String PLAY = "play";
    public final static String PAUSE = "pause";
    public final static String STOP = "stop";
    public final static String SELECT_TRACK = "select";
    public final static String NEXT = "next";
    public static final String PREVIOUS = "previous";
    public static final String JUMP = "jump";
    public static final String CHANGE_PLAYBACK = "changeplayback";
    public static final String MUTE = "mute";
    public static final String UNMUTE = "unmute";

    /**
     * creates a new Resource.
     *
     * @param provider   the Provider of the Resource
     * @param command          the resource
     * @param capabilities the capabilities of the player
     */
    CommandResource(Identification provider, String command, Capabilities capabilities) {
        super(ResourceID, provider, command);
        if (!verifyCommand(command))
            throw new IllegalArgumentException("IllegalCommand!");
        if (!verifyCapabilities(command, capabilities))
            throw new IllegalArgumentException("Player is not able to handle Command");
    }

    /**
     * creates a new Resource.
     *
     * @param provider   the Provider of the Resource
     * @param command          the resource
     * @param capabilities the capabilities of the player
     * @param context used for logging
     */
    public static Optional<CommandResource> createCommandResource(Identification provider, String command, Capabilities capabilities, Context context) {
        CommandResource commandResource = new CommandResource(provider, command, capabilities);
        if (!verifyCommand(command)) {
            context.getLogger().error("IllegalCommand!");
            return Optional.empty();
        }
        if (!verifyCapabilities(command, capabilities)) {
            context.getLogger().error("Player is not able to handle Command!");
            return Optional.empty();
        }
        return Optional.of(commandResource);
    }

    /**
     * verifies that an command is not malformed
     * @param command the Command
     * @return false if malformed
     */
    private static boolean verifyCommand(String command) {
        return command.equals(PLAY) ||
                command.equals(PAUSE) ||
                command.equals(STOP) ||
                command.equals(SELECT_TRACK) ||
                command.equals(NEXT) ||
                command.equals(PREVIOUS) ||
                command.equals(CHANGE_PLAYBACK) ||
                command.equals(MUTE) ||
                command.equals(UNMUTE);
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
            case NEXT: return capabilities.hasNextPrevious();
            case PREVIOUS: return capabilities.hasNextPrevious();
            case JUMP: return capabilities.isAbleToJump();
            case CHANGE_PLAYBACK: return capabilities.isPlaybackChangeable();
            case UNMUTE: return true;
            case MUTE: return true;
            case STOP: return true;
        }
        return false;
    }
}
