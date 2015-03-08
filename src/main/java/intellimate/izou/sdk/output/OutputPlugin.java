package intellimate.izou.sdk.output;

import com.google.common.reflect.TypeToken;
import intellimate.izou.sdk.Context;

import java.util.List;

/**
 * an OutputPlugin without an Argument
 * @author Leander Kurscheidt
 * @version 1.0
 */
public abstract class OutputPlugin<T> extends OutputPluginArgument<T, Object> {
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
     * returns the argument for the OutputExtensions
     *
     * @return the argument
     */
    @Override
    public Object getArgument() {
        return null;
    }

    /**
     * returns the Type of the argument for the OutputExtensions, or null if none
     *
     * @return the type of the Argument
     */
    @Override
    public TypeToken<T> getArgumentType() {
        return null;
    }

    /**
     * method that uses tDoneList to generate a final output that will then be rendered.
     * @param data the data generated
     */
    @Override
    //more clarity
    public abstract void renderFinalOutput(List<T> data);
}
