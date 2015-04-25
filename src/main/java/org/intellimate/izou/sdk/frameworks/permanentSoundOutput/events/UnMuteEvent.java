package org.intellimate.izou.sdk.frameworks.permanentSoundOutput.events;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.events.CommonEvents;
import org.intellimate.izou.sdk.events.Event;
import org.intellimate.izou.sdk.frameworks.common.resources.OutputPluginSelectorResource;
import org.intellimate.izou.sdk.util.AddOnModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * signals that every sound-output should unmute
 * @author LeanderK
 * @version 1.0
 */
public class UnMuteEvent extends Event {
    public static final String ID = "izou.sound.events.unmute";
    /**
     * Creates a new Event Object
     *
     * @param source      the source of the Event, most likely a this reference.
     * @throws IllegalArgumentException if one of the Arguments is null or empty
     */
    protected UnMuteEvent(AddOnModule addOnModule, Identification source)
            throws IllegalArgumentException {
        super(CommonEvents.get(addOnModule).getType().responseType(), source, new ArrayList<>(Arrays.asList(ID,
                CommonEvents.get(addOnModule).getDescriptors().stopDescriptor())));
    }

    /**
     * creates a new StopEvent
     * @param addOnModule a reference to the module which created this Event
     * @param source the caller
     * @param target the target who should start playing
     * @return the optional StartMusicRequest
     */
    public static Optional<UnMuteEvent> createUnMuteEvent(AddOnModule addOnModule, Identification source, Identification target) {
        if (target == null || target.equals(source))
            return Optional.empty();
        try {
            UnMuteEvent unmuteRequest = new UnMuteEvent(addOnModule, source);
            unmuteRequest.addResource(new OutputPluginSelectorResource(source, target));
            return Optional.of(unmuteRequest);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
