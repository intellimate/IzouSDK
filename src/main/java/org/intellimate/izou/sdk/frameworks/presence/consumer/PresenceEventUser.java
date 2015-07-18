package org.intellimate.izou.sdk.frameworks.presence.consumer;

import org.intellimate.izou.events.EventListenerModel;
import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.sdk.frameworks.presence.events.LeavingEvent;
import org.intellimate.izou.sdk.frameworks.presence.events.PresenceEvent;
import org.intellimate.izou.sdk.util.ContextProvider;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * provides various methods to get notified when the presence event got fired.
 * @author LeanderK
 * @version 1.0
 */
@SuppressWarnings("unused")
public interface PresenceEventUser extends ContextProvider {
    /**
     * registers the the passed Consumer as a callback which will be called when an presence event was fired.
     * <p>
     * You must unregister the returned EventListenerModel when you are not using the Callback anymore!
     * </p>
     * @param callback the callback to use
     * @param strict whether the callback should only be fired when the addon when the user is very likely near izou
     * @param known whether the callback should only be fired when the person causing the evens is very likely the user
     * @return the EventListener used to unregister at the Context
     */
    @SuppressWarnings("unused")
    default EventListenerModel registerPresenceCallback(Consumer<EventModel> callback, boolean strict, boolean known) {
        return registerPresenceCallback(callback, strict, known, false);
    }

    /**
     * registers the the passed Consumer as a callback which will be called when an presence event was fired.
     * <p>
     * You must unregister the returned EventListenerModel when you are not using the Callback anymore!
     * </p>
     * @param callback the callback to use
     * @param strict whether the callback should only be fired when the addon when the user is very likely near izou
     * @param known whether the callback should only be fired when the person causing the evens is very likely the user
     * @param firstEncounter whether the callback should only be fired when its the first encounter after the user left
     * @return the EventListener used to unregister at the Context
     */
    @SuppressWarnings("unused")
    default EventListenerModel registerPresenceCallback(Consumer<EventModel> callback, boolean strict, boolean known, boolean firstEncounter) {
        EventListenerModel eventListenerModel = event -> {
            if (strict && !event.containsDescriptor(PresenceEvent.STRICT_DESCRIPTOR)) {
                return;
            }
            if (known && !event.containsDescriptor(PresenceEvent.KNOWN_DESCRIPTOR)) {
                return;
            }
            if (firstEncounter && !event.containsDescriptor(PresenceEvent.FIRST_ENCOUNTER_DESCRIPTOR)) {
                return;
            }
            callback.accept(event);
        };
        getContext().getEvents().registerEventListener(Collections.singletonList(PresenceEvent.ID), eventListenerModel);
        return eventListenerModel;
    }

    /**
     * returns a CompletableFuture, that completes when the next specified presence-event was fired
     * @param strict whether it is very likely that the user is near
     * @param known whether it is very likely that the person causing the event is the user
     * @return an instance of CompletableFuture
     */
    @SuppressWarnings("unused")
    default CompletableFuture<EventModel> nextPresence(boolean strict, boolean known) {
        return nextPresence(strict, known, false);
    }

    /**
     * returns a CompletableFuture, that completes when the next specified presence-event was fired
     * @param strict whether it is very likely that the user is near
     * @param known whether it is very likely that the person causing the event is the user
     * @param firstEncounter whether any other than the first encounter after the user left should be ignored
     * @return an instance of CompletableFuture
     */
    @SuppressWarnings("unused")
    default CompletableFuture<EventModel> nextPresence(boolean strict, boolean known, boolean firstEncounter) {
        CompletableFuture<EventModel> completableFuture = new CompletableFuture<>();
        EventListenerModel eventListenerModel = event -> {
            if (strict && !event.containsDescriptor(PresenceEvent.STRICT_DESCRIPTOR)) {
                return;
            }
            if (known && !event.containsDescriptor(PresenceEvent.KNOWN_DESCRIPTOR)) {
                return;
            }
            if (firstEncounter && !event.containsDescriptor(PresenceEvent.FIRST_ENCOUNTER_DESCRIPTOR)) {
                return;
            }
            completableFuture.complete(event);
        };
        getContext().getEvents().registerEventListener(Collections.singletonList(PresenceEvent.ID), eventListenerModel);
        return completableFuture.whenComplete((event, throwable) ->
                getContext().getEvents().unregisterEventListener(eventListenerModel));
    }

    /**
     * registers the the passed Consumer as a callback which will be called when an leaving event was fired.
     * <p>
     * You must unregister the returned EventListenerModel when you are not using the Callback anymore!
     * </p>
     * @param callback the callback to use
     * @param strict whether the callback should only be fired when the addon when the user was very likely near izou
     * @return the EventListener used to unregister at the Context
     */
    @SuppressWarnings("unused")
    default EventListenerModel registerLeavingCallback(Consumer<EventModel> callback, boolean strict) {
        EventListenerModel eventListenerModel = event -> {
            if (strict && !event.containsDescriptor(LeavingEvent.STRICT_DESCRIPTOR)) {
                return;
            }
            callback.accept(event);
        };
        getContext().getEvents().registerEventListener(Collections.singletonList(LeavingEvent.ID), eventListenerModel);
        return eventListenerModel;
    }

    /**
     * returns a CompletableFuture, that completes when the next specified leaving-event was fired
     * @param strict whether it is very likely that the user was near
     * @return an instance of CompletableFuture
     */
    @SuppressWarnings("unused")
    default CompletableFuture<EventModel> nextLeaving(boolean strict) {
        CompletableFuture<EventModel> completableFuture = new CompletableFuture<>();
        EventListenerModel eventListenerModel = event -> {
            if (strict && !event.containsDescriptor(LeavingEvent.STRICT_DESCRIPTOR)) {
                return;
            }
            completableFuture.complete(event);
        };
        getContext().getEvents().registerEventListener(Collections.singletonList(LeavingEvent.ID), eventListenerModel);
        return completableFuture.whenComplete((event, throwable) ->
                getContext().getEvents().unregisterEventListener(eventListenerModel));
    }
}
