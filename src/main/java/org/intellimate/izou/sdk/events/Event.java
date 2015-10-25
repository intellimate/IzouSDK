package org.intellimate.izou.sdk.events;

import org.intellimate.izou.events.EventBehaviourControllerModel;
import org.intellimate.izou.events.EventLifeCycle;
import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.resource.ListResourceProvider;
import org.intellimate.izou.resource.ResourceModel;
import org.intellimate.izou.sdk.resource.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * This class represents an Event.
 * This class is immutable! for every change it will return an new instance!
 */
public class Event implements EventModel<Event> {
    private final String type;
    private final Identification source;
    private final List<String> descriptors;
    private final ListResourceProvider listResourceContainer;
    private final EventBehaviourController eventBehaviourController;
    private final ConcurrentHashMap<EventLifeCycle, List<Consumer<EventLifeCycle>>> lifeCycleListeners = new ConcurrentHashMap<>();

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
        this.descriptors = Collections.unmodifiableList(descriptors);
        listResourceContainer = new ListResourceProviderImpl();
        eventBehaviourController = new EventBehaviourController();
    }

    /**
     * Creates a new Event Object
     * @param type the Type of the Event, try to use the predefined Event types
     * @param source the source of the Event, most likely a this reference.
     * @param listResourceContainer the ResourceContainer
     * @param descriptors the descriptors to initialize the Event with
     * @param eventBehaviourController the Controller of the Event
     * @throws IllegalArgumentException if one of the Arguments is null or empty
     */
    protected Event(String type, Identification source, ListResourceProvider listResourceContainer, List<String> descriptors,
                    EventBehaviourController eventBehaviourController)throws IllegalArgumentException {
        if(type == null || type.isEmpty()) throw new IllegalArgumentException("illegal type");
        if(source == null) throw new IllegalArgumentException("source is null");
        this.type = type;
        this.source = source;
        this.descriptors = Collections.unmodifiableList(new ArrayList<>(descriptors));
        this.listResourceContainer = listResourceContainer;
        this.eventBehaviourController = eventBehaviourController;
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

    @Override
    public void lifecycleCallback(EventLifeCycle eventLifeCycle) {
        lifeCycleListeners.getOrDefault(eventLifeCycle, new LinkedList<>()).stream()
                .forEach(eventLifeCycleConsumer -> eventLifeCycleConsumer.accept(eventLifeCycle));
    }

    /**
     * adds the Consumer to the specified EventLifeCycle.
     * In its current implementation the invocation of the Callback method is parallel, but the notificaton of the listners not.
     * @param eventLifeCycle the EventLifeCycle to target
     * @param cycleCallback the callback
     */
    @SuppressWarnings("unused")
    public Event addEventLifeCycleListener(EventLifeCycle eventLifeCycle, Consumer<EventLifeCycle> cycleCallback) {
        lifeCycleListeners.compute(eventLifeCycle, (unused, list) -> {
            if (list == null)
                list = new ArrayList<>();
            list.add(cycleCallback);
            return list;
        });
        return this;
    }

    @Override
    public String toString() {
        return "Event{" +
                "type='" + type + '\'' +
                ", source=" + source +
                ", descriptors=" + descriptors +
                ", listResourceContainer=" + listResourceContainer +
                '}';
    }
}
