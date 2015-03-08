package intellimate.izou.sdk.output;

import com.google.common.reflect.TypeToken;
import intellimate.izou.events.Event;
import intellimate.izou.sdk.Context;

/**
 * an OutputExtension without an Argument
 * @author Leander Kurscheidt
 * @version 1.0
 */
public abstract class OutputExtension<T> extends OutputExtensionArgument<Object, T> {
    /**
     * creates a new outputExtension with a new id
     *
     * @param context  the context of the addon
     * @param id       the id to be set to the id of outputExtension
     * @param pluginId the ID of the Plugin the OutputExtension is associated with
     */
    public OutputExtension(Context context, String id, String pluginId) {
        super(context, id, pluginId);
    }

    /**
     * creates a new outputExtension with a new id
     *
     * @param id      the id to be set to the id of outputExtension
     * @param context the context of the addon
     */
    public OutputExtension(String id, Context context) {
        super(id, context);
    }

    /**
     * returns the ReturnType of the generic
     *
     * @return the type of the generic
     */
    @Override
    public TypeToken<T> getReturnType() {
        return null;
    }

    /**
     * generates the data for the given Event
     *
     * @param event the event to generate for
     * @param o     the optional argument
     * @return the result
     */
    @Override
    public T generate(Event event, Object o) {
        return generate(event);
    }

    /**
     * generates the data for the given Event
     *
     * @param event the event to generate for
     * @return the result
     */
    public abstract T generate(Event event);
}
