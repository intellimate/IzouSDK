package org.intellimate.izou.sdk.frameworks.music.events;

import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.identification.Identifiable;
import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.events.CommonEvents;
import org.intellimate.izou.sdk.events.Event;
import org.intellimate.izou.sdk.frameworks.common.resources.SelectorResource;
import org.intellimate.izou.sdk.frameworks.music.Capabilities;
import org.intellimate.izou.sdk.frameworks.music.resources.CommandResource;
import org.intellimate.izou.sdk.util.AddOnModule;

import java.util.Collections;
import java.util.Optional;

/**
 * @author LeanderK
 * @version 1.0
 */
public class PlayerCommand extends Event {
    @SuppressWarnings("SpellCheckingInspection")
    public static final String ID = "izou.music.events.playercommand";
    /**
     * Creates a new Event Object
     *
     * @param addOnModule        the addonModule which is creating the Event
     * @param source      the source of the Event, most likely a this reference.
     * @throws IllegalArgumentException if one of the Arguments is null or empty
     */
    protected PlayerCommand(AddOnModule addOnModule, Identification source) throws IllegalArgumentException {
        super(CommonEvents.get(addOnModule).getType().responseType(), source, Collections.singletonList(ID));
    }

    /**
     * Creates a new Event Object
     *
     * @param addOnModule        the addonModule which is creating the Event
     * @param source      the source of the Event, most likely a this reference.
     * @param target the target who should start playing
     * @param capabilities the capabilities of the player
     * @param command the command
     * @throws IllegalArgumentException if one of the Arguments is null or empty
     */
    public static Optional<PlayerCommand> createPlayerCommand(AddOnModule addOnModule, Identification source, Identification target, String command, Capabilities capabilities) {
        try {
            Optional<CommandResource> commandResource = CommandResource.createCommandResource(source, command,
                    capabilities, addOnModule.getContext());
            if (!commandResource.isPresent()) {
                addOnModule.error("unable to obtain commandResource");
                return Optional.empty();
            }
            PlayerCommand playerCommand = new PlayerCommand(addOnModule, source);
            playerCommand.addResource(new SelectorResource(source, target));
            playerCommand.addResource(commandResource.get());
            return Optional.of(playerCommand);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    /**
     * verifies that the Event has the correct id and is correctly addressed
     * @param eventModel the eventModel
     * @param player the identifiable
     * @return true if everything is correctly
     */
    public static boolean verify(EventModel eventModel, Identifiable player) {
        if (!eventModel.containsDescriptor(PlayerCommand.ID))
            return false;
        return SelectorResource.isTarget(eventModel, player)
                .orElse(true);
    }
}
