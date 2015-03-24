package intellimate.izou.sdk.contentgenerator;

import intellimate.izou.events.EventModel;
import intellimate.izou.identification.IdentificationManager;
import intellimate.izou.sdk.events.Event;
import intellimate.izou.sdk.util.AddOnModule;

import java.util.Arrays;
import java.util.Optional;

/**
 * This class is used in the ContentGenerator to signal that a ContentGenerator wants to get triggered when it gets
 * fired.
 * @author Leander Kurscheidt
 * @version 1.0
 */
public class EventListener {
    private final Event event;
    private final String descriptor;
    private final String description;
    private final String descriptorID;

    /**
     * Creates a new EventListener.
     *
     * @param event the Event to associate with
     * @param descriptor the descriptor of the Event you want to listen to
     * @param description the description of the descriptor
     * @param descriptorID an ID for the descriptor (Should contain no special characters, spaces etc.)
     */
    EventListener(Event event, String descriptor, String description, String descriptorID) {
        this.event = event;
        this.descriptor = descriptor;
        this.description = description;
        this.descriptorID = descriptorID;
    }

    /**
     * create the EventListener
     * @param descriptor the descriptor of the Event you want to listen to
     * @param description the description of the descriptor
     * @param descriptorID an ID for the descriptor (Should contain no special characters, spaces etc.). Only String
     *                     which match the regex (\w-_)+ are allowed.
     * @param addOnModule the AddOnModule which wants to create the EventListener
     * @return if one of the parameter is null, or unable to to obtain ID
     * @throws java.lang.IllegalArgumentException if the descriptorID contains illegal characters
     */
    public static Optional<EventListener> createEventListener(String descriptor, String description,
                                                       String descriptorID, AddOnModule addOnModule)
                                                                                        throws IllegalArgumentException{

        if (!descriptorID.matches("[\\w\\-_]+"))
            throw new IllegalArgumentException("descriptorID: " + descriptorID + " contains illegal characters");
        return IdentificationManager.getInstance().getIdentification(addOnModule)
                .flatMap(id -> Event.createEvent(Event.NOTIFICATION, id, Arrays.asList(descriptor)))
                .map(event -> new EventListener(event, descriptor, description, descriptorID));
    }

    /**
     * Returns the associated Event
     *
     * @return The Event
     */
    public EventModel<?> getEvent() {
        return event;
    }

    /**
     * Returns the descriptor of the Event you want to listen to
     * @return a String
     */
    public String getDescriptor() {
        return descriptor;
    }

    /**
     * the description of the descriptor
     * @return a String
     */
    public String getDescription() {
        return description;
    }

    /**
     * an ID for the descriptor (Should contain no special characters, spaces etc.)
     * @return a String
     */
    public String getDescriptorID() {
        return descriptorID;
    }
}
