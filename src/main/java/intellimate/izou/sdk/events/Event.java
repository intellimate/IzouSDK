package intellimate.izou.sdk.events;

import intellimate.izou.identification.Identification;
import intellimate.izou.resource.ListResourceProvider;
import intellimate.izou.resource.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class represents an Event.
 * This class is immutable! for every change it will return an new instance!
 */
public class Event implements intellimate.izou.events.Event<Event> {
    /**
     * Use this type when other AddOns should react to this Event.
     */
    public static final String RESPONSE = Event.class.getCanonicalName() + "Response";
    /**
     * Use this type when other AddOns should just notice (they needn't).
     */
    public static final String NOTIFICATION = Event.class.getCanonicalName() + "Notification";
    //common Events-Descriptors:
    /**
     * Event for a Welcome with maximum response.
     *
     * Every component that can contribute should contribute to this Event.
     * @deprecated see <a href="https://github.com/intellimate/Izou/wiki/Common-IDs">new online documentation</a>
     */
    @Deprecated
    public static final String FULL_WELCOME_EVENT = "izou.FullResponse";
    /**
     * Event for a Welcome with major response.
     *
     * Every component that is import should contribute to this Event.
     * @deprecated see <a href="https://github.com/intellimate/Izou/wiki/Common-IDs">new online documentation</a>
     */
    @Deprecated
    @SuppressWarnings("UnusedDeclaration")
    public static final String MAJOR_WELCOME_EVENT = "izou.MajorResponse";
    /**
     * Event for a Welcome with minor response.
     *
     * Only components that have information of great importance should contribute to this event.
     * @deprecated see <a href="https://github.com/intellimate/Izou/wiki/Common-IDs">new online documentation</a>
     */
    @Deprecated
    @SuppressWarnings("UnusedDeclaration")
    public static final String MINOR_WELCOME_EVENT = "izou.MinorResponse";
    private final String type;
    private final Identification source;
    private final List<String> descriptors;
    private final ListResourceProvider listResourceContainer = new intellimate.izou.sdk.resource.ListResourceProvider();
    private final EventBehaviourController eventBehaviourController = new EventBehaviourController(this);

    /**
     * Creates a new Event Object
     * @param type the Type of the Event, try to use the predefined Event types
     * @param source the source of the Event, most likely a this reference.
     */
    private Event(String type, Identification source, List<String> descriptors) {
        this.type = type;
        this.source = source;
        this.descriptors = descriptors;
    }

    /**
     * Creates a new Event Object
     * @param type the Type of the Event, try to use the predefined Event types
     * @param source the source of the Event, most likely a this reference.
     * @return an Optional, that may be empty if type is null or empty or source is null
     */
    public static Optional<Event> createEvent(String type, Identification source) {
        if(type == null || type.isEmpty()) return Optional.empty();
        if(source == null) return Optional.empty();
        return Optional.of(new Event(type, source, new ArrayList<String>()));
    }

    /**
     * Creates a new Event Object
     * @param type the Type of the Event, try to use the predefined Event types
     * @param source the source of the Event, most likely a this reference.
     * @return an Optional, that may be empty if type is null or empty or source is null
     */
    public static Optional<Event> createEvent(String type, Identification source, List<String> descriptors) {
        if(type == null || type.isEmpty()) return Optional.empty();
        if(source == null) return Optional.empty();
        return Optional.of(new Event(type, source, descriptors));
    }

    /**
     * The ID of the Event.
     * It describes the Type of the Event.
     * @return A String containing an ID
     */
    @Override
    public String getID() {
    return type;
    }

    /**
     * The type of the Event.
     * It describes the Type of the Event.
     * @return A String containing an ID
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * returns the Source of the Event, e.g. the object who fired it.
     * @return an identifiable
     */
    @Override
    public Identification getSource() {
        return source;
    }

    /**
     * returns all the Resources the Event currently has
     * @return an instance of ListResourceContainer
     */
    @Override
    public ListResourceProvider getListResourceContainer() {
        return listResourceContainer;
    }

    /**
     * adds a Resource to the Container
     * @param resource an instance of the resource to add
     * @return the resulting Event (which is the same instance)
     */
    @Override
    public Event addResource(Resource resource) {
        listResourceContainer.addResource(resource);
        return this;
    }

    /**
     * adds a List of Resources to the Container
     * @param resources a list containing all the resources
     */
    @Override
    public Event addResources(List<Resource> resources) {
        listResourceContainer.addResource(resources);
        return this;
    }

    /**
     * returns a List containing all the Descriptors.
     * @return a List containing the Descriptors
     */
    @Override
    public List<String> getDescriptors() {
        return descriptors;
    }

    /**
     * returns a List containing all the Descriptors and the type.
     * @return a List containing the Descriptors
     */
    @Override
    public List<String> getAllInformations() {
        return null;
    }

    /**
     * sets the Descriptors (but not the Event-Type).
     * <p>
     * Replaces all existing descriptors.
     * Since Event is immutable, it will create a new Instance.
     * </p>
     * @param descriptors a List containing all the Descriptors
     * @return the resulting Event (which is the same instance)
     */
    public Event setDescriptors(List<String> descriptors) {
        return new Event(getType(), getSource(), descriptors);
    }

    /**
     * sets the Descriptors (but not the Event-Type).
     * @param descriptor a String describing the Event.
     * @return the resulting Event (which is the same instance)
     */
    public Event addDescriptor(String descriptor) {
        List<String> newDescriptors = new ArrayList<>();
        newDescriptors.addAll(descriptors);
        newDescriptors.add(descriptor);
        return new Event(getType(), getSource(), newDescriptors);
    }

    /**
     * replaces the Descriptors
     * @param descriptors a list containing the Descriptors.
     * @return the resulting Event (which is the same instance)
     */
    public Event replaceDescriptors(List<String> descriptors) {
        return new Event(getType(), getSource(), descriptors);
    }

    /**
     * returns whether the event contains the specific descriptor.
     * this method also checks whether it matches the type.
     * @param descriptor a String with the ID of the Descriptor
     * @return boolean when the Event contains the descriptor, false when not.
     */
    @Override
    public boolean containsDescriptor(String descriptor) {
        return descriptors.contains(descriptor) || type.equals(descriptor);
    }

    /**
     * returns the associated EventBehaviourController
     * @return an instance of EventBehaviourController
     */
    @Override
    public intellimate.izou.events.EventBehaviourController getEventBehaviourController() {
        return eventBehaviourController;
    }
}
