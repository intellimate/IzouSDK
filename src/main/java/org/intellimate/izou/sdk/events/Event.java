package org.intellimate.izou.sdk.events;

import org.intellimate.izou.events.EventBehaviourControllerModel;
import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.resource.ListResourceProvider;
import org.intellimate.izou.resource.ResourceModel;
import org.intellimate.izou.sdk.resource.ListResourceProviderImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * This class represents an Event.
 * This class is immutable! for every change it will return an new instance!
 */
public class Event implements EventModel<Event> {
    /**
     * Use this type when other AddOns should react to this Event.
     * @deprecated use {@link CommonEvents}
     */
    @Deprecated
    public static final String RESPONSE = "response";
    /**
     * Use this type when only our Addon reacts to this Event
     * @deprecated use {@link CommonEvents}
     */
    @Deprecated
    public static final String NOTIFICATION = "notification";
    //common Events-Descriptors:
    /**
     * Event for a Welcome with maximum response.
     *
     * Every component that can contribute should contribute to this Event.
     * @deprecated use {@link CommonEvents}
     */
    @Deprecated
    public static final String FULL_WELCOME_EVENT = "izou.FullResponse";
    /**
     * Event for a Welcome with major response.
     *
     * Every component that is import should contribute to this Event.
     * @deprecated use {@link CommonEvents}
     */
    @Deprecated
    @SuppressWarnings("UnusedDeclaration")
    public static final String MAJOR_WELCOME_EVENT = "izou.MajorResponse";
    /**
     * Event for a Welcome with minor response.
     *
     * Only components that have information of great importance should contribute to this event.
     * @deprecated use {@link CommonEvents}
     */
    @Deprecated
    @SuppressWarnings("UnusedDeclaration")
    public static final String MINOR_WELCOME_EVENT = "izou.MinorResponse";
    private final String type;
    private final Identification source;
    private final List<String> descriptors;
    private final ListResourceProvider listResourceContainer = new ListResourceProviderImpl();
    private final EventBehaviourController eventBehaviourController = new EventBehaviourController();

    /**
     * Creates a new Event Object
     * @param type the Type of the Event, try to use the predefined Event types
     * @param source the source of the Event, most likely a this reference.
     * @param descriptors the descriptors to initialize the Event with
     * @throws IllegalArgumentException if one of the Arguments is null or empty
     */
    protected Event(String type, Identification source, List<String> descriptors) throws IllegalArgumentException {
        if(type == null || type.isEmpty()) throw new IllegalArgumentException("illegal type");
        if(source == null) throw new IllegalArgumentException("source is null");
        this.type = type;
        this.source = source;
        this.descriptors = Collections.synchronizedList(new ArrayList<>(descriptors));
    }

    /**
     * Creates a new Event Object
     * @param type the Type of the Event, try to use the predefined Event types
     * @param source the source of the Event, most likely a this reference.
     * @return an Optional, that may be empty if type is null or empty or source is null
     */
    public static Optional<Event> createEvent(String type, Identification source) {
        try {
            return Optional.of(new Event(type, source, new ArrayList<>()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    /**
     * Creates a new Event Object
     * @param type the Type of the Event, try to use the predefined Event types
     * @param source the source of the Event, most likely a this reference.
     * @param descriptors the descriptors
     * @return an Optional, that may be empty if type is null or empty or source is null
     */
    public static Optional<Event> createEvent(String type, Identification source, List<String> descriptors) {
        try {
            return Optional.of(new Event(type, source, descriptors));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
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
    public Event addResource(ResourceModel resource) {
        listResourceContainer.addResource(resource);
        return this;
    }

    /**
     * adds a List of Resources to the Container
     * @param resources a list containing all the resources
     */
    @Override
    public Event addResources(List<ResourceModel> resources) {
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
        ArrayList<String> strings = new ArrayList<>(descriptors);
        strings.add(type);
        return strings;
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
    public EventBehaviourControllerModel getEventBehaviourController() {
        return eventBehaviourController;
    }
}
