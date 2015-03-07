package intellimate.izou.sdk.contentgenerator;

import intellimate.izou.identification.IdentificationManager;
import intellimate.izou.sdk.events.Event;
import intellimate.izou.sdk.resource.Resource;
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

    private EventListener(Event event) {
        this.event = event;
    }

    /**
     * create the EventListener
     * @param descriptor the descriptor of the Event you want to listen to
     * @param description the description of the descriptor
     * @param descriptorID an ID for the descriptor (Should contain no special characters, spaces etc.)
     * @param addOnModule the AddOnModule which wants to create the EventListener
     * @return if one of the parameter is null, or unable to to obtain ID
     */
    public Optional<EventListener> createEventListener(String descriptor, String description,
                                                       String descriptorID, AddOnModule addOnModule) {
        Optional<Resource<String>> descriptionResource = IdentificationManager.getInstance().getIdentification(addOnModule)
                .map(id -> new Resource<>("description", id, description));
        if (!descriptionResource.isPresent()) {
            addOnModule.getContext().getLogger().error("Unable to obtain resource");
            return Optional.empty();
        }
        Optional<Resource<String>> eventIDResource = IdentificationManager.getInstance().getIdentification(addOnModule)
                .map(id -> new Resource<>("descriptorID", id, descriptorID));
        if (!eventIDResource.isPresent()) {
            addOnModule.getContext().getLogger().error("Unable to obtain resource");
            return Optional.empty();
        }
        return IdentificationManager.getInstance().getIdentification(addOnModule)
                .flatMap(id -> Event.createEvent(Event.NOTIFICATION, id, Arrays.asList(descriptor)))
                .map(event -> event.addResources(Arrays.asList(descriptionResource.get(), eventIDResource.get())))
                .map(EventListener::new);
    }

    /**
     * returns the associated Event
     * @return The Event
     */
    public Event getEvent() {
        return event;
    }
}
