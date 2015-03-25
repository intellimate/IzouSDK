package org.intellimate.izou.sdk.contentgenerator;

import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.resource.ResourceModel;
import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.resource.Resource;
import org.intellimate.izou.sdk.specification.ContentGeneratorModel;
import org.intellimate.izou.sdk.util.AddOnModule;
import org.intellimate.izou.sdk.util.ResourceCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The Task of an ContentGenerator is to generate a Resources-Object when a Event it subscribed to was fired.
 * <p>
 *     When an Event this ContentGenerator subscribed to was fired, the ContentGeneratorManager will run the instance
 *     of it in a ThreadPool and generate(String eventID) will be called.
 * </p>
 */
public abstract class ContentGenerator extends AddOnModule implements ContentGeneratorModel, ResourceCreator {

    /**
     * Creates a new content generator.
     *
     * @param id the id of the content generator
     * @param context the context of the addOn
     */
    public ContentGenerator(String id, Context context) {
        super(context, id);
    }

    /**
     * this method is called to register for what Events it wants to provide Resources.
     * <p>
     * The Event has to be in the following format: It should contain only one Descriptor and and one Resource with the
     * ID "description", which contains an description of the Event.
     * </p>
     * @return a List containing ID's for the Events
     */
    @Override
    public List<? extends EventModel<?>> announceEvents() {
        return getTriggeredEvents().stream()
                .map(EventListener::getEvent)
                .collect(Collectors.toList());
    }

    /**
     * This method is called to register what resources the object provides.
     * just pass a List of Resources without Data in it.
     *
     * @return a List containing the resources the object provides
     */
    @Override
    public List<? extends ResourceModel> announceResources() {
        return getTriggeredResources();
    }

    /**
     * this method is called when an object wants to get a Resource.
     * <p>
     * Don't use the Resources provided as arguments, they are just the requests.
     * There is a timeout after 1 second.
     * </p>
     * @param list a list of resources without data
     * @param optional     if an event caused the action, it gets passed. It can also be null.
     * @return a list of resources with data
     */
    @Override
    public List<ResourceModel> provideResource(List<? extends ResourceModel> list, Optional<EventModel> optional) {
        //TODO: check arguemnts and return type here! Missing ID etc. Fail fast!
        return new ArrayList<>(triggered(list, optional));
    }

    /**
     * this method is called when an object wants to get a Resource.
     * <p>
     * Don't use the Resources provided as arguments, they are just the requests.
     * There is a timeout after 1 second.
     * </p>
     * @param list a list of resources without data
     * @param optional     if an event caused the action, it gets passed. It can also be null.
     * @return a list of resources with data
     */
    public abstract List<? extends Resource> triggered(List<? extends ResourceModel> list, Optional<EventModel> optional);
}
