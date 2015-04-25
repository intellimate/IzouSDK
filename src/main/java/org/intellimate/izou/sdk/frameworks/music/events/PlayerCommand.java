package org.intellimate.izou.sdk.frameworks.music.events;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.events.CommonEvents;
import org.intellimate.izou.sdk.events.Event;
import org.intellimate.izou.sdk.frameworks.common.resources.OutputPluginSelectorResource;
import org.intellimate.izou.sdk.util.AddOnModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author LeanderK
 * @version 1.0
 */
public class PlayerCommand extends Event {
    public static final String ID = "izou.music.events.playercommand";
    /**
     * Creates a new Event Object
     *
     * @param addOnModule        the addonModule which is creating the Event
     * @param source      the source of the Event, most likely a this reference.
     * @throws IllegalArgumentException if one of the Arguments is null or empty
     */
    protected PlayerCommand(AddOnModule addOnModule, Identification source) throws IllegalArgumentException {
        super(CommonEvents.get(addOnModule).getType().responseType(), source, new ArrayList<>(Arrays.asList(ID)));
    }

    /**
     * Creates a new Event Object
     *
     * @param addOnModule        the addonModule which is creating the Event
     * @param source      the source of the Event, most likely a this reference.
     * @param target the target who should start playing
     * @throws IllegalArgumentException if one of the Arguments is null or empty
     */
    public static Optional<PlayerUpdate> createPlayerCommand(AddOnModule addOnModule, Identification source, Identification target) {
        try {
            PlayerUpdate playerUpdate = new PlayerUpdate(addOnModule, source);
            playerUpdate.addResource(new OutputPluginSelectorResource(source, target));
            return Optional.of(playerUpdate);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
