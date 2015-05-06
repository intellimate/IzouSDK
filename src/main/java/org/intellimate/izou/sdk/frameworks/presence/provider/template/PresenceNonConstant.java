package org.intellimate.izou.sdk.frameworks.presence.provider.template;

import org.intellimate.izou.events.EventListenerModel;
import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.identification.IdentificationManager;
import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.activator.Activator;
import org.intellimate.izou.sdk.events.CommonEvents;
import org.intellimate.izou.sdk.frameworks.presence.events.LeavingEvent;
import org.intellimate.izou.sdk.frameworks.presence.events.PresenceEvent;
import org.intellimate.izou.sdk.frameworks.presence.provider.Presence;
import org.intellimate.izou.sdk.frameworks.presence.resources.PresenceResource;
import org.intellimate.izou.sdk.util.ResourceUser;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * the base class for all presence-things that have no CONSTANT information (for example a motion-sensor).
 * if it is the first encountering with the user since he was discovered (and at least an hour has passed),
 * the event also contains CommonEvents.Response.Major_Response as a descriptor.
 * @author LeanderK
 * @version 1.0
 */
public abstract class PresenceNonConstant extends Activator implements EventListenerModel, ResourceUser {
    private boolean present = false;
    private boolean strictPresent = false;
    private LocalDateTime lastSeen = LocalDateTime.now();
    private final boolean strict;

    public PresenceNonConstant(Context context, String ID, boolean strict) {
        super(context, ID);
        this.strict = strict;
        getContext().getEvents().registerEventListener(Arrays.asList(LeavingEvent.ID, PresenceEvent.ID), this);
    }

    /**
     * call this method when you have encountered the user
     */
    public void userEncountered() {
        List<String> descriptors = new ArrayList<>();
        if (strict && (!present || !strictPresent) &&
                lastSeen.until(LocalDateTime.now(), ChronoUnit.MINUTES) > getMinuteThreshold()) {
            descriptors.add(CommonEvents.Response.FULL_RESPONSE_DESCRIPTOR);
        }
        Optional<PresenceEvent> presenceEvent = IdentificationManager.getInstance()
                .getIdentification(this)
                .flatMap(id -> PresenceEvent.createPresenceEvent(id, strict, descriptors));
        if (!presenceEvent.isPresent()) {
            error("unable to create PresenceEvent");
        } else {
            fire(presenceEvent.get(), 5);
        }
    }

    /**
     * the time-threshold that had to pass to fire the event with an FULL_RESPONSE_DESCRIPTOR
     * @return int in minutes
     */
    private int getMinuteThreshold() {
        return 60;
    }

    /**
     * Invoked when an activator-event occurs.
     *
     * @param event an instance of Event
     */
    @Override
    public void eventFired(EventModel event) {
        if (event.containsDescriptor(LeavingEvent.ID) || event.containsDescriptor(PresenceEvent.ID)) {
            if (event.containsDescriptor(LeavingEvent.ID)) {
                if (event.containsDescriptor(LeavingEvent.GENERAL_DESCRIPTOR)) {
                    present = false;
                    strictPresent = false;
                } else if (event.containsDescriptor(LeavingEvent.STRICT_DESCRIPTOR)) {
                    nonStrictAvailable().thenAccept(available -> {
                        if (!available)
                            present = false;
                        strictPresent = false;
                    });
                }
            } else {
                present = true;
                if (event.containsDescriptor(PresenceEvent.STRICT_DESCRIPTOR))
                    strictPresent = true;
            }
            lastSeen = LocalDateTime.now();
        }
    }

    /**
     * updates the boolean whether it is the mode vague
     */
    private CompletableFuture<Boolean> nonStrictAvailable() {
        return generateResource(PresenceResource.ID)
                .orElse(CompletableFuture.completedFuture(new ArrayList<>()))
                .thenApply(list -> list.stream()
                    .map(Presence::importPresence)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .noneMatch(Presence::isStrict));
    }
}
