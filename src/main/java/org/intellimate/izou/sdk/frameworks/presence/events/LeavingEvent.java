package org.intellimate.izou.sdk.frameworks.presence.events;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.events.CommonEvents;
import org.intellimate.izou.sdk.events.Event;

import java.util.List;
import java.util.Optional;

/**
 * this event guarantees that the user is not around anymore (as much as possible)
 * @author LeanderK
 * @version 1.0
 */
public class LeavingEvent extends Event {
    /**
     * this event does not mean the user is able to notice anything (can be used for warm-up), it indicates
     * he might be
     */
    public static final String GENERAL_DESCRIPTOR = "izou.presence.general.leaving";
    /**
     * it means that the addon can guarantee that the user left an area near izou
     */
    public static final String STRICT_DESCRIPTOR = "izou.presence.strict.leaving";
    /**
     * it means that the addon can guarantee that the user entered an area near izou
     */
    public static final String ID = "izou.leaving";

    /**
     * Creates a new Event Object
     *
     * @param source      the source of the Event, most likely a this reference.
     * @param descriptors the descriptors to initialize the Event with
     * @throws IllegalArgumentException if one of the Arguments is null or empty
     */
    protected LeavingEvent(Identification source, List<String> descriptors) throws IllegalArgumentException {
        super(CommonEvents.Type.RESPONSE_TYPE, source, descriptors);
    }

    /**
     * creates a new LeavingEvent
     * @param source the caller
     * @param strict whether the addon can guarantee that the user is around
     * @param descriptors the descriptors
     * @return the optional PresenceEvent
     */
    public static Optional<LeavingEvent> createLeavingEvent(Identification source, boolean strict, List<String> descriptors) {
        try {
            if (strict) {
                descriptors.add(STRICT_DESCRIPTOR);
            } else {
                descriptors.add(GENERAL_DESCRIPTOR);
            }
            descriptors.add(ID);
            LeavingEvent stopRequest = new LeavingEvent(source, descriptors);
            return Optional.of(stopRequest);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
