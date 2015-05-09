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
@SuppressWarnings("unused")
public abstract class PresenceNonConstant extends Activator implements EventListenerModel, ResourceUser {
    private boolean present = false;
    private boolean strictPresent = false;
    private LocalDateTime lastSeen = LocalDateTime.now();
    private final boolean strict;
    private final boolean fireUnknownIfNotPresent;

    public PresenceNonConstant(Context context, String ID, boolean strict, boolean fireUnknownIfNotPresent) {
        super(context, ID);
        this.strict = strict;
        this.fireUnknownIfNotPresent = fireUnknownIfNotPresent;
        getContext().getEvents().registerEventListener(Arrays.asList(LeavingEvent.ID, PresenceEvent.ID), this);
    }

    /**
     * call this method when you have encountered the user
     */
    public void userEncountered() {
        Optional<PresenceEvent> presenceEvent;
        List<String> descriptors = new ArrayList<>();
        if (strict && ((!present && !fireUnknownIfNotPresent)|| !strictPresent)) {
            if (lastSeen.until(LocalDateTime.now(), ChronoUnit.MINUTES) > getMajorMinuteThreshold()) {
                descriptors.add(CommonEvents.Response.MAJOR_RESPONSE_DESCRIPTOR);
            } else if (lastSeen.until(LocalDateTime.now(), ChronoUnit.MINUTES) > getMinorMinuteThreshold()) {
                descriptors.add(CommonEvents.Response.MINOR_RESPONSE_DESCRIPTOR);
            }
        } else if (!present && fireUnknownIfNotPresent) {
            descriptors.add(CommonEvents.Descriptors.NOT_INTERRUPT);
        }
        boolean known = !fireUnknownIfNotPresent || present;
        presenceEvent = IdentificationManager.getInstance()
                .getIdentification(this)
                .flatMap(id -> PresenceEvent.createPresenceEvent(id, strict, known, descriptors));
        if (!presenceEvent.isPresent()) {
            error("unable to create PresenceEvent");
        } else {
            fire(presenceEvent.get(), 5);
        }
    }

    /**
     * the time-threshold that had to pass to fire the event with an MINOR_RESPONSE_DESCRIPTOR,
     * default is 20
     * @return int in minutes
     */
    private int getMinorMinuteThreshold() {
        return 20;
    }

    /**
     * the time-threshold that had to pass to fire the event with an MAJOR_RESPONSE_DESCRIPTOR,
     * default is 90
     * @return int in minutes
     */
    private int getMajorMinuteThreshold() {
        return 90;
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