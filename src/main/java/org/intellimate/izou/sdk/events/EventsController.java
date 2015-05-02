package org.intellimate.izou.sdk.events;

import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.events.EventsControllerModel;
import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.util.AddOnModule;

/**
 * represents an EventsController used for Controlling the dispatching of Events
 * @author LeanderK
 * @version 1.0
 */
public abstract class EventsController extends AddOnModule implements EventsControllerModel {

    public EventsController(Context context, String ID) {
        super(context, ID);
    }

    @Override
    public boolean controlEventDispatcher(EventModel eventModel) {
        //noinspection SimplifiableIfStatement
        if (eventModel.getType().equals(CommonEvents.Type.NOTIFICATION_TYPE)
                || eventModel.containsDescriptor("izou.alarm"))
            return true;
        return controlEvents(eventModel);
    }

    /**
     * Controls whether the fired Event should be dispatched to all the listeners. This method should execute quickly
     * @param eventModel the Event
     * @return true if it should dispatch, false if not
     */
    public abstract boolean controlEvents(EventModel eventModel);
}
