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
    @SuppressWarnings("SpellCheckingInspection")
    public static final String CHANGE_PLAYBACK = "changeplayback";
    @SuppressWarnings("SpellCheckingInspection")
    public static final String CHANGE_VOLUME = "changevolume";

    /**
     * creates a new Resource.
     *
     * @param provider   the Provider of the Resource
     * @param command          the resource
     * @param capabilities the capabilities of the player
     */
    private CommandResource(Identification provider, String command, Capabilities capabilities) {
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
     * @return the commandResource if nothing illegal was passed
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
    public static boolean verifyCommand(String command) {
        return command.equals(PLAY) ||
                command.equals(PAUSE) ||
                command.equals(STOP) ||
                command.equals(SELECT_TRACK) ||
                command.equals(NEXT) ||
                command.equals(PREVIOUS) ||
                command.equals(CHANGE_PLAYBACK) ||
                command.equals(CHANGE_VOLUME);
    }

    /**
     * verifies that the player is capable of handling the command
     * @param command the command
     * @param capabilities the capabilities
     * @return true if capable, false if not
     */
    public static boolean verifyCapabilities(String command, Capabilities capabilities) {
        switch (command) {
            case PLAY: return capabilities.hasPlayPauseControl();
            case PAUSE: return capabilities.hasPlayPauseControl();
            case SELECT_TRACK: return capabilities.isAbleToSelectTrack();
            case NEXT: return capabilities.hasNextPrevious();
            case PREVIOUS: return capabilities.hasNextPrevious();
            case JUMP: return capabilities.isAbleToJump();
            case CHANGE_PLAYBACK: return capabilities.isPlaybackChangeable();
            case CHANGE_VOLUME: return capabilities.canChangeVolume();
            case STOP: return true;
        }
        return false;
    }

    /**
     * verifies tha the command is legal and able to be executed
     * @param command the command
     * @param capabilities the capabilities
     * @return true if able, false if not
     */
    public static boolean verify(String command, Capabilities capabilities) {
        return verifyCommand(command) && verifyCapabilities(command, capabilities);
    }
}
