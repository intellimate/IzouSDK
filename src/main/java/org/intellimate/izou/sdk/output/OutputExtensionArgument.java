package org.intellimate.izou.sdk.output;

import com.google.common.reflect.TypeToken;
import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.output.OutputExtensionModel;
import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.util.AddOnModule;

import java.util.ArrayList;
import java.util.List;

/**
 * OutputExtension's purpose is to take resourceData and convert it into another data format so that it can be rendered correctly
 * by the output-plugin. These objects are represented in the form of future objects that are stored in tDoneList
 */
public abstract class OutputExtensionArgument<T, X> extends AddOnModule implements OutputExtensionModel<T, X> {

    /**
     * the id of the outputPlugin the outputExtension belongs to
     */
    private String pluginId;
    /**
     * the type argument for the Data you want to return
     */
    private final TypeToken<T> returnTypeToken;
    /**
     * the type argument for the Data you want to give the OutputExtensions as an argument
     */
    private final TypeToken<X> argumentTypeToken;

    /**
     * a list of all the resource's which the outputExtension would like to receive theoretically to work with
     */
    private List<String> resourceIdWishList;

    /**
     * creates a new outputExtension with a new id
     * @param context the context of the addon
     * @param id the id to be set to the id of outputExtension
     */
    public OutputExtensionArgument(Context context, String id, String pluginId) {
        this(id, context);
        this.pluginId = pluginId;
    }

    /**
     * creates a new outputExtension with a new id
     * @param id the id to be set to the id of outputExtension
     * @param context the context of the addon
     */
    public OutputExtensionArgument(String id, Context context) {
        super(context, id);
        resourceIdWishList = new ArrayList<>();
        this.returnTypeToken = new TypeToken<T>(getClass()) {};
        this.argumentTypeToken = new TypeToken<X>(getClass()) {};
    }

    /**
     * returns its resourceIdWishList
     *
     * @return a List containing the resourceIDs
     */
    public List<String> getResourceIdWishList() {
        return resourceIdWishList;
    }

    /**
     * adds id of resource to the wish list
     *
     * @param id the id of the resource to be added to the wish list
     */
    public void addResourceIdToWishList(String id) {
        resourceIdWishList.add(id);
    }

    /**
     * removes resourceId from the resourceIdWishList
     *
     * @param id the id of the resource to be removed
     */
    public void removeResourceIdFromWishList(String id) {
        resourceIdWishList.remove(id);
    }

    /**
     * gets the id of the output-plugin the outputExtension belongs to
     *
     * @return id of the output-plugin the outputExtension belongs to
     */
    @Override
    public String getPluginId() {
        return pluginId;
    }

    /**
     * sets the ID of the OutputPlugin the OutputExtension belongs to
     * @param pluginId the ID of the Plugin
     */
    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }

    /**
     * returns the ReturnType of the generic
     *
     * @return the type of the generic
     */
    @Override
    public TypeToken<T> getReturnType() {
        return returnTypeToken;
    }

    /**
     * returns the Type of the Argument or null if none
     *
     * @return the type of the argument
     */
    @Override
    public TypeToken<X> getArgumentType() {
        return argumentTypeToken;
    }

    /**
     * Checks if the outputExtension can execute with the current event
     *
     * @param event the event to check against
     * @return the state of whether the outputExtension can execute with the current event
     */
    @Override
    public boolean canRun(EventModel event) {
        //noinspection SimplifiableIfStatement
        if (event != null) {
            return event.getListResourceContainer().providesResource(getResourceIdWishList());
        }
        return false;
    }

    /**
     * generates the data for the given Event
     *
     * @param event the event to generate for
     * @param x     the optional argument
     * @return the result
     */
    @Override
    //more clarity
    public abstract T generate(EventModel event, X x);
}

