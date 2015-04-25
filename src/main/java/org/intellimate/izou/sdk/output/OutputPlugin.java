package org.intellimate.izou.sdk.output;

import com.google.common.reflect.TypeToken;
import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.sdk.Context;

import java.util.List;

/**
 * an OutputPlugin without an Argument
 * @param <T> the return type
 * @author Leander Kurscheidt
 * @version 1.0
 */
public abstract class OutputPlugin<T> extends OutputPluginArgument<Object, T> {
    /**
     * creates a new output-plugin with a new id
     *
     * @param context context
     * @param id      the id of the new output-plugin
     */
    public OutputPlugin(Context context, String id) {
        super(context, id);
    }



    /**
     * returns the Type of the argument for the OutputExtensions, or null if none
     *
     * @return the type of the Argument
     */
    @Override
    public TypeToken<Object> getArgumentType() {
        return null;
    }

    /**
     * returns the argument for the OutputExtensions
     *
     * @return the argument
     */
    @Override
    public Object getArgument() {
        return null;
    }

    /**
     * method that uses the data from the OutputExtensions to generate a final output that will then be rendered.
     *
     * @param data the data generated
     */
    @Override
    public abstract void renderFinalOutput(List<T> data, EventModel eventModel);
}
