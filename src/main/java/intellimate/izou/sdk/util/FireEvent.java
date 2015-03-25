package intellimate.izou.sdk.util;

import intellimate.izou.events.MultipleEventsException;
import intellimate.izou.identification.Identifiable;
import intellimate.izou.identification.IdentificationManager;
import intellimate.izou.identification.IllegalIDException;
import intellimate.izou.sdk.events.Event;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Leander Kurscheidt
 * @version 1.0
 */
public interface FireEvent extends ContextProvider, Identifiable {

    /**
     * tries to fire an Event 5 times, returns true if succeed.
     * <p>
     * If there is currently another Event getting processed, it will wait for 100 milliseconds and try for 5 times.
     * </p>
     * @param type the type of the Event (See static Strings in IzouSDK Events)
     * @param descriptor the single descriptor of the Event
     * @return true if fired, false if unable
     */
    default boolean fire(String type, String descriptor) {
        return fire(type, Arrays.asList(descriptor), 5);
    }

    /**
     * tries to fire an Event 5 times, returns true if succeed.
     * <p>
     * If there is currently another Event getting processed, it will wait for 100 milliseconds and try for 5 times.
     * </p>
     * @param type the type of the Event (See static Strings in IzouSDK Events)
     * @param descriptors the Descriptors of the Event
     * @return true if fired, false if unable
     */
    default boolean fire(String type, List<String> descriptors) {
        return fire(type, descriptors, 5);
    }

    /**
     * tries to fire an an Event specified times, returns true if succeed.
     * <p>
     * If there is currently another Event getting processed, it will wait for 100 milliseconds and try for retry-times.
     * </p>
     * @param type the type of the Event (See static Strings in IzouSDK Events)
     * @param descriptors the Descriptors of the Event
     * @param retry how many times it should try
     * @return true if fired, false if unable
     */
    default boolean fire(String type, List<String> descriptors, int retry) {
        Optional<Event> event = IdentificationManager.getInstance().getIdentification(this)
                .flatMap(id -> intellimate.izou.sdk.events.Event.createEvent(type, id, descriptors));
        if (!event.isPresent()) {
            getContext().getLogger().error("unable to obtain ID");
            return false;
        }
        int counter = 0;
        while (counter < retry) {
            try {
                getContext().getEvents().fireEvent(event.get());
                return true;
            } catch (MultipleEventsException e) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    throw new RuntimeException(e);
                }
            } catch (IllegalIDException e) {
                //maybe change in future SDK-Versions? currently not implemented in Izou
                getContext().getLogger().error("Illegal ID!", e);
            }
        }
        return false;
    }
}
