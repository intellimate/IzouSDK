package org.intellimate.izou.sdk.frameworks.presence.events;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.sdk.events.CommonEvents;
import org.intellimate.izou.sdk.events.Event;

import java.util.List;
import java.util.Optional;

/**
 * this event indicates presence
 * @author LeanderK
 * @version 1.0
 */
public class PresenceEvent extends Event {
    /**
     * this event does not mean the user is able to notice anything (can be used for warm-up), it indicates
     * he might be
     */
    public static final String GENERAL_DESCRIPTOR = "izou.presence.general";
    /**
     * it means that the addon can guarantee that the user entered an area near izou
     */
    public static final String STRICT_DESCRIPTOR = "izou.presence.strict";
    /**
     * it means that the addon can guarantee that the user entered an area near izou
     */
    public static final String ID = "izou.presence";


    /**
     * Creates a new Event Object
     *
     * @param source      the source of the Event, most likely a this reference.
     * @param descriptors the descriptors to initialize the Event with
     * @throws IllegalArgumentException if one of the Arguments is null or empty
     */
    protected PresenceEvent(Identification source, List<String> descriptors) throws IllegalArgumentException {
        super(CommonEvents.Type.RESPONSE_TYPE, source, descriptors);
    }

    /**
     * creates a new PresenceEvent
     * @param source the caller
     * @param strict whether the addon can guarantee that the user is around
     * @param descriptors the descriptors
     * @return the optional PresenceEvent
     */
    public static Optional<PresenceEvent> createPresenceEvent(Identification source, boolean strict, List<String> descriptors) {
        try {
            if (strict) {
                descriptors.add(STRICT_DESCRIPTOR);
            } else {
                descriptors.add(GENERAL_DESCRIPTOR);
            }
            descriptors.add(ID);
            PresenceEvent stopRequest = new PresenceEvent(source, descriptors);
            return Optional.of(stopRequest);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
