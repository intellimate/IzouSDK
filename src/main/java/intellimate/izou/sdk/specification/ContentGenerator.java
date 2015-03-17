package intellimate.izou.sdk.specification;

import intellimate.izou.resource.ResourceBuilder;
import intellimate.izou.sdk.contentgenerator.EventListener;
import intellimate.izou.sdk.resource.Resource;

import java.util.List;

/**
 * The Task of an ContentGenerator is to generate a Resources-Object when a Event it subscribed to was fired.
 * <p>
 *     When an Event this ContentGenerator subscribed to was fired, the ContentGeneratorManager will run the instance
 *     of it in a ThreadPool and generate(String eventID) will be called.
 * </p>
 * @author Leander Kurscheidt
 * @version 1.0
 */
public interface ContentGenerator extends ResourceBuilder {
    /**
     * this method returns a List of EventListener, which indicate for which Events the ContentGenerator should be
     * triggered.
     * @return a List of EventListeners
     */
    List<EventListener> getTriggeredEvents();

    /**
     * This method is called to register what resources the object provides.
     * just pass a List of Resources without Data in it.
     *
     * @return a List containing the resources the object provides
     */
    //makes it more coherent with getTriggeredEvents
    List<Resource> getTriggeredResources();
}
