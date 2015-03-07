package intellimate.izou.sdk.contentgenerator;

import intellimate.izou.events.Event;
import intellimate.izou.resource.Resource;
import intellimate.izou.sdk.Context;
import intellimate.izou.sdk.util.AddOnModule;

import java.util.List;
import java.util.Optional;

/**
 * The Task of an ContentGenerator is to generate a Resources-Object when a Event it subscribed to was fired.
 * <p>
 *     When an Event this ContentGenerator subscribed to was fired, the ContentGeneratorManager will run the instance
 *     of it in a ThreadPool and generate(String eventID) will be called.
 * </p>
 */
public abstract class ContentGenerator extends AddOnModule implements intellimate.izou.sdk.specification.ContentGenerator {

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
     * this method is called when an object wants to get a Resource.
     * <p>
     * Don't use the Resources provided as arguments, they are just the requests.
     * There is a timeout after 1 second.
     * </p>
     * @param resources a list of resources without data
     * @param event     if an event caused the action, it gets passed. It can also be null.
     * @return a list of resources with data
     */
    @Override
    //TODO: helper method check if arguments get returned
    public abstract List<Resource> provideResource(List<Resource> resources, Optional<Event> event);
}
