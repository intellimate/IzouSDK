package org.intellimate.izou.sdk.events;

import org.intellimate.izou.sdk.contentgenerator.EventListener;
import org.intellimate.izou.sdk.util.AddOnModule;

import java.util.Optional;

/**
 * A collection of common Event-Listeners, Descriptions and Types, see
 * <a href="https://github.com/intellimate/Izou/wiki/Common-IDs">online document</a>
 * @author LeanderK
 * @version 1.0
 */
public class CommonEvents {
    private final Presence presence = new Presence();
    private final Response response = new Response();
    private final Descriptors descriptors = new Descriptors();
    private final Type type = new Type();
    private final AddOnModule addOnModule;

    CommonEvents(AddOnModule addOnModule) {
        this.addOnModule = addOnModule;
    }

    public static CommonEvents get(AddOnModule addOnModule) {
        if (addOnModule !=  null) {
            return new CommonEvents(addOnModule);
        } else {
            return null;
        }
    }

    /**
     * returns the common EventListeners and Descriptors associated with the Presence
     * @return an object containing methods that return the instances
     */
    public Presence getPresence() {
        return presence;
    }

    /**
     * returns the common EventListeners and Descriptors associated with the Response
     * @return an object containing methods that return the instances
     */
    public Response getResponse() {
        return response;
    }

    /**
     * returns the common EventListeners and Types
     * @return an object containing methods that return the instances
     */
    public Type getType() {
        return type;
    }

    /**
     * returns common descriptors
     * @return an object containing methods that return the instances
     */
    public Descriptors getDescriptors() {
        return descriptors;
    }

    /**
     * if the type of event is based on comparing the current time a fixed or dynamic time AND the of the other systems
     * reaction should be consistent ignoring environment variables
     * @return an optional which may contain an EventListener
     */
    public Optional<EventListener> alarmListener() {
        return EventListener.createEventListener(
                alarmDescriptor(),
                "if the type of event is based on comparing the current time a fixed or dynamic time AND the of the "
                        + "other systems reaction should be consistent ignoring environment variables",
                "izou_alarm",
                addOnModule
        );
    }

    /**
     * if the type of event is based on comparing the current time a fixed or dynamic time AND the of the other systems
     * reaction should be consistent ignoring environment variables
     * @return the descriptor
     */
    public static String alarmDescriptor() {
        return "izou.alarm";
    }

    public class Presence {
        /**
         * this event does not mean the user is able to notice anything (can be used for warm-up), it indicates
         * he might be
         * @return an optional which may contain an EventListener
         */
        public Optional<EventListener> generalListener() {
            return EventListener.createEventListener(
                    generalDescriptor(),
                    "this event does not mean the user is able to notice anything (can be used for warm-up), it " +
                            "indicates he might be",
                    "izou_presence_general",
                    addOnModule
            );
        }

        /**
         * this event does not mean the user is able to notice anything (can be used for warm-up), it indicates
         * he might be
         * @return the descriptor
         */
        public String generalDescriptor() {
            return "izou.presence.general";
        }

        /**
         * it means the user has probably left (he could have left a while ago)
         * @return an optional which may contain an EventListener
         */
        public Optional<EventListener> generalLeavingListener() {
            return EventListener.createEventListener(
                    generalLeavingDescriptor(),
                    "it means the user has probably left (he could have left a while ago)",
                    "izou_presence_general_leaving",
                    addOnModule
            );
        }

        /**
         * it means the user has probably left (he could have left a while ago)
         * @return the descriptor
         */
        public String generalLeavingDescriptor() {
            return "izou.presence.general.leaving";
        }

        /**
         * it means that the addon can guarantee that the user entered an area near izou
         * @return an optional which may contain an EventListener
         */
        public Optional<EventListener> strictListener() {
            return EventListener.createEventListener(
                    strictDescriptor(),
                    "it means that the addon can guarantee that the user entered an area near izou",
                    "izou_presence_strict",
                    addOnModule
            );
        }

        /**
         * it means that the addon can guarantee that the user entered an area near izou
         * @return the descriptor
         */
        public String strictDescriptor() {
            return "izou.alarm";
        }
    }

    public class Response {
        /**
         * Event for maximum response. Every component that can contribute should contribute to this Event.
         * Examples: alarm, User coming home etc. Handle them carefully, they should be only fired rarely!
         * @return an optional which may contain an EventListener
         */
        public Optional<EventListener> fullResponseListener() {
            return EventListener.createEventListener(
                    fullResponseDescriptor(),
                    "Event for maximum response. Every component that can contribute should contribute to this " +
                            "Event. Examples: alarm, User coming home etc. Handle them carefully, they should be only " +
                            "fired rarely!",
                    "izou_full_response",
                    addOnModule
            );
        }

        /**
         * Event for maximum response. Every component that can contribute should contribute to this Event.
         * Examples: alarm, User coming home etc. Handle them carefully, they should be only fired rarely!
         * @return the descriptor
         */
        public String fullResponseDescriptor() {
            return "izou.FullResponse";
        }

        /**
         * Event for major response. Every component that is import should contribute to this Event. MajorResponse
         * is intended to be the most common Response-Event
         * @return an optional which may contain an EventListener
         */
        public Optional<EventListener> majorResponseListener() {
            return EventListener.createEventListener(
                    majorResponseDescriptor(),
                    "Event for major response. Every component that is import should contribute to this Event. " +
                            "MajorResponse is intended to be the most common Response-Event",
                    "izou_major_response",
                    addOnModule
            );
        }

        /**
         * Event for major response. Every component that is import should contribute to this Event. MajorResponse
         * is intended to be the most common Response-Event
         * @return the descriptor
         */
        public String majorResponseDescriptor() {
            return "izou.MajorResponse";
        }

        /**
         * Event for a Welcome with minor response. Only components that have information of great
         * importance should contribute to this event!
         * @return an optional which may contain an EventListener
         */
        public Optional<EventListener> minorResponseListener() {
            return EventListener.createEventListener(
                    minorResponseDescriptor(),
                    "Event for a Welcome with minor response. Only components that have information of great importance " +
                            "should contribute to this event!",
                    "izou_minor_response",
                    addOnModule
            );
        }

        /**
         * Event for a Welcome with minor response. Only components that have information of great
         * importance should contribute to this event!
         * @return the descriptor
         */
        public String minorResponseDescriptor() {
            return "izou.MinorResponse";
        }
    }

    public class Type {
        /**
         * Event-Type which indicates that other AddOns should react to this Event.
         * @return an optional which may contain an EventListener
         */
        public Optional<EventListener> responseListener() {
            return EventListener.createEventListener(
                    responseType(),
                    "Event-Type which indicates that other AddOns should react to this Event.",
                    "izou_response",
                    addOnModule
            );
        }

        /**
         * Event-Type which indicates that other AddOns should react to this Event.
         * @return the descriptor
         */
        public String responseType() {
            return "response";
        }

        /**
         * Event-Type which indicates that only your AddOn should react to an Event
         * @return an optional which may contain an EventListener
         */
        public Optional<EventListener> notificationListener() {
            return EventListener.createEventListener(
                    notificationType(),
                    "Event-Type which indicates that only your AddOn should react to an Event",
                    "izou_notification",
                    addOnModule
            );
        }

        /**
         * Event-Type which indicates that only your AddOn should react to an Event
         * @return the descriptor
         */
        public String notificationType() {
            return "notification";
        }
    }

    public class Descriptors {
        /**
         * Event-Type which indicates that this events stops an already running addon
         * @return an optional which may contain an EventListener
         */
        public Optional<EventListener> stopListener() {
            return EventListener.createEventListener(
                    stopDescriptor(),
                    "Event-Type which indicates that that this events stops an already running addon",
                    "izou_stop",
                    addOnModule
            );
        }

        /**
         * Event-Type which indicates that only your AddOn should react to an Event
         * @return the descriptor
         */
        public String stopDescriptor() {
            return "stop";
        }
    }
}
