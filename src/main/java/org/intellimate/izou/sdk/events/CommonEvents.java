package org.intellimate.izou.sdk.events;

import org.intellimate.izou.identification.Identifiable;
import org.intellimate.izou.sdk.contentgenerator.EventListener;
import org.intellimate.izou.sdk.frameworks.presence.events.LeavingEvent;
import org.intellimate.izou.sdk.frameworks.presence.events.PresenceEvent;

import java.util.Optional;

/**
 * A collection of common Event-Listeners, Descriptions and Types, see
 * <a href="https://github.com/intellimate/Izou/wiki/Common-IDs">online document</a>
 * @author LeanderK
 * @version 1.0
 */
public class CommonEvents {

    /**
     * if the type of event is based on comparing the current time a fixed or dynamic time AND the of the other systems
     * reaction should be consistent ignoring environment variables
     * @param identifiable the identifiable ot associate the EventListener with
     * @return an optional which may contain an EventListener
     */
    public Optional<EventListener> alarmListener(Identifiable identifiable) {
        return EventListener.createEventListener(
                ALARM_DESCRIPTOR,
                "if the type of event is based on comparing the current time a fixed or dynamic time AND the of the "
                        + "other systems reaction should be consistent ignoring environment variables",
                "izou_alarm",
                identifiable
        );
    }

    /**
     * if the type of event is based on comparing the current time a fixed or dynamic time AND the of the other systems
     * reaction should be consistent ignoring environment variables
     */
    public static final String ALARM_DESCRIPTOR = "izou.alarm";

    /**
     * the common EventListeners and Descriptors associated with the Presence
     */
    public static class Presence {
        /**
         * this event does indicate some kind of presence, strict or general.
         * <p>
         * Important: there is no authentication provided, if the descriptor: UNKNOWN_DESCRIPTOR is added to the
         * event. Only if the descriptor: KNOWN_DESCRIPTOR is added, one should conclude that this is the actual user.
         * </p>
         * @param identifiable the identifiable ot associate the EventListener with
         * @return an optional which may contain an EventListener
         */
        public static Optional<EventListener> presenceListener(Identifiable identifiable) {
            return EventListener.createEventListener(
                    PresenceEvent.ID,
                    "this event does indicate some kind of presence, strict or general",
                    "izou_presence",
                    identifiable
            );
        }

        /**
         * this event does indicate some kind of leaving, strict or general
         * <p>
         * Important: there is no authentication provided, if the descriptor: UNKNOWN_DESCRIPTOR is added to the
         * event. Only if the descriptor: KNOWN_DESCRIPTOR is added, one should conclude that this is the actual user.
         * </p>
         * @param identifiable the identifiable ot associate the EventListener with
         * @return an optional which may contain an EventListener
         */
        public static Optional<EventListener> leavingListener(Identifiable identifiable) {
            return EventListener.createEventListener(
                    LeavingEvent.ID,
                    "this event does indicate some kind of leaving, strict or general",
                    "izou_presence_leaving",
                    identifiable
            );
        }

        /**
         * this event does not mean the user is able to notice anything (can be used for warm-up), it indicates
         * he might be.
         * <p>
         * Important: there is no authentication provided, if the descriptor: UNKNOWN_DESCRIPTOR is added to the
         * event. Only if the descriptor: KNOWN_DESCRIPTOR is added, one should conclude that this is the actual user.
         * </p>
         * @param identifiable the identifiable ot associate the EventListener with
         * @return an optional which may contain an EventListener
         */
        public static Optional<EventListener> generalListener(Identifiable identifiable) {
            return EventListener.createEventListener(
                    PresenceEvent.GENERAL_DESCRIPTOR,
                    "this event does not mean the user is able to notice anything (can be used for warm-up), it " +
                            "indicates he might be",
                    "izou_presence_general",
                    identifiable
            );
        }

        /**
         * it means the user has probably left (he could have left a while ago)
         * @param identifiable the identifiable ot associate the EventListener with
         * @return an optional which may contain an EventListener
         */
        public static Optional<EventListener> generalLeavingListener(Identifiable identifiable) {
            return EventListener.createEventListener(
                    LeavingEvent.GENERAL_DESCRIPTOR,
                    "it means the user has probably left (he could have left a while ago)",
                    "izou_presence_general_leaving",
                    identifiable
            );
        }

        /**
         * it means that the addon can guarantee that the user entered an area near izou.
         * <p>
         * Important: there is no authentication provided, if the descriptor: UNKNOWN_DESCRIPTOR is added to the
         * event. Only if the descriptor: KNOWN_DESCRIPTOR is added, one should conclude that this is the actual user.
         * </p>
         * @param identifiable the identifiable ot associate the EventListener with
         * @return an optional which may contain an EventListener
         */
        public static Optional<EventListener> strictListener(Identifiable identifiable) {
            return EventListener.createEventListener(
                    PresenceEvent.STRICT_DESCRIPTOR,
                    "it means that the addon can guarantee that the user entered an area near izou",
                    "izou_presence_strict",
                    identifiable
            );
        }

        /**
         * it means the user has most likely left (he may come back immediately)
         * @param identifiable the identifiable ot associate the EventListener with
         * @return an optional which may contain an EventListener
         */
        public static Optional<EventListener> strictLeavingListener(Identifiable identifiable) {
            return EventListener.createEventListener(
                    LeavingEvent.STRICT_DESCRIPTOR,
                    "it means the user has most likely left (he may come back immediately)",
                    "izou_presence_strict_leaving",
                    identifiable
            );
        }
    }

    /**
     * the common EventListeners and Descriptors associated with the Response
     */
    public static class Response {
        /**
         * Event for maximum response. Every component that can contribute should contribute to this Event.
         * Examples: alarm, User coming home etc. Handle them carefully, they should be only fired rarely!
         * @param identifiable the identifiable ot associate the EventListener with
         * @return an optional which may contain an EventListener
         */
        public static Optional<EventListener> fullResponseListener(Identifiable identifiable) {
            return EventListener.createEventListener(
                    FULL_RESPONSE_DESCRIPTOR,
                    "Event for maximum response. Every component that can contribute should contribute to this " +
                            "Event. Examples: alarm, User coming home etc. Handle them carefully, they should be only " +
                            "fired rarely!",
                    "izou_full_response",
                    identifiable
            );
        }

        /**
         * Event for maximum response. Every component that can contribute should contribute to this Event.
         * Examples: alarm, User coming home etc. Handle them carefully, they should be only fired rarely!
         */
        public static final String FULL_RESPONSE_DESCRIPTOR = "izou.FullResponse";

        /**
         * Event for major response. Every component that is import should contribute to this Event. MajorResponse
         * is intended to be the most common Response-Event
         * @param identifiable the identifiable ot associate the EventListener with
         * @return an optional which may contain an EventListener
         */
        public static Optional<EventListener> majorResponseListener(Identifiable identifiable) {
            return EventListener.createEventListener(
                    MAJOR_RESPONSE_DESCRIPTOR,
                    "Event for major response. Every component that is import should contribute to this Event. " +
                            "MajorResponse is intended to be the most common Response-Event",
                    "izou_major_response",
                    identifiable
            );
        }

        /**
         * Event for major response. Every component that is import should contribute to this Event. MajorResponse
         * is intended to be the most common Response-Event
         */
        public static final String MAJOR_RESPONSE_DESCRIPTOR = "izou.MajorResponse";

        /**
         * Event for a Welcome with minor response. Only components that have information of great
         * importance should contribute to this event!
         * @param identifiable the identifiable ot associate the EventListener with
         * @return an optional which may contain an EventListener
         */
        public static Optional<EventListener> minorResponseListener(Identifiable identifiable) {
            return EventListener.createEventListener(
                    MINOR_RESPONSE_DESCRIPTOR,
                    "Event for a Welcome with minor response. Only components that have information of great importance " +
                            "should contribute to this event!",
                    "izou_minor_response",
                    identifiable
            );
        }

        /**
         * Event for a Welcome with minor response. Only components that have information of great
         * importance should contribute to this event!
         */
        public static final String MINOR_RESPONSE_DESCRIPTOR = "izou.MinorResponse";
    }

    /**
     * the common EventListeners and Types
     */
    public static class Type {
        /**
         * Event-Type which indicates that other AddOns should react to this Event.
         * @param identifiable the identifiable ot associate the EventListener with
         * @return an optional which may contain an EventListener
         */
        public static Optional<EventListener> responseListener(Identifiable identifiable) {
            return EventListener.createEventListener(
                    RESPONSE_TYPE,
                    "Event-Type which indicates that other AddOns should react to this Event.",
                    "izou_response",
                    identifiable
            );
        }

        /**
         * Event-Type which indicates that other AddOns should react to this Event.
         */
        public static final String RESPONSE_TYPE = "response";

        /**
         * Event-Type which indicates that only your AddOn should react to an Event
         * @param identifiable the identifiable ot associate the EventListener with
         * @return an optional which may contain an EventListener
         */
        public static Optional<EventListener> notificationListener(Identifiable identifiable) {
            return EventListener.createEventListener(
                    NOTIFICATION_TYPE,
                    "Event-Type which indicates that only your AddOn should react to an Event",
                    "izou_notification",
                    identifiable
            );
        }

        /**
         * Event-Type which indicates that only your AddOn should react to an Event
         */
        public static final String NOTIFICATION_TYPE = "notification";
    }

    /**
     * common descriptors
     */
    public static class Descriptors {
        /**
         * Event-Type which indicates that this events stops an already running addon
         * @param identifiable the identifiable ot associate the EventListener with
         * @return an optional which may contain an EventListener
         */
        public static Optional<EventListener> stopListener(Identifiable identifiable) {
            return EventListener.createEventListener(
                    STOP_DESCRIPTOR,
                    "Event-Type which indicates that that this events stops an already running addon",
                    "izou_stop",
                    identifiable
            );
        }
        /**
         * Event-Type which indicates that this events stops an already running addon
         */
        public static final String STOP_DESCRIPTOR = "stop";

        /**
         * Event-type which indicates that this event is important and should not get cancelled
         */
        public static final String NOT_INTERRUPT = "notinterrupt";
    }
}
