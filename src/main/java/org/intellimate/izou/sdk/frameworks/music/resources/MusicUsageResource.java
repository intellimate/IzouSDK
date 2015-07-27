package org.intellimate.izou.sdk.frameworks.music.resources;

import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.resource.ResourceModel;
import org.intellimate.izou.sdk.resource.Resource;

/**
 * this resource signals that an addon requests playing.
 * <p>
 * It is mostly used for non-permanent Music-Usage which blocks the Event in the Output lifecycle.<br>
 * For example if you are an alarm you can add this resource to an Event and it will play the alarm sound, blocking
 * the execution for other outputPlugins.
 * </p>
 * @author LeanderK
 * @version 1.0
 */
public class MusicUsageResource extends Resource<Boolean> {
    @SuppressWarnings("SpellCheckingInspection")
    public static final String ID = "izou.music.resource.permanent";

    /**
     * creates a new Resource.
     *
     * @param provider   the Provider of the Resource
     * @param permanent   true if permanent, false if not
     */
    public MusicUsageResource(Identification provider, Boolean permanent) {
        super(ID, provider, permanent);
    }

    /**
     * gets whether the request is permanent (=permanent resource is available and true)
     * @param eventModel the EventModel
     * @return true if permanent
     */
    public static boolean isPermanent(EventModel eventModel) {
        if (eventModel.getListResourceContainer().containsResourcesFromSource(ID)) {
            return eventModel
                    .getListResourceContainer()
                    .provideResource(ID)
                    .stream()
                    .map(ResourceModel::getResource)
                    .filter(ob -> ob instanceof Boolean)
                    .map(ob -> (Boolean) ob)
                    .findAny()
                    .orElse(false);

        } else {
            return false;
        }
    }

    /**
     * returns true if the resource is true, otherwise returns false
     * @param resourceModel the resourceModel
     * @return true if permanent
     */
    public static boolean isPermanent(ResourceModel resourceModel) {
        Object resource = resourceModel.getResource();
        try {
            return (Boolean) resource;
        } catch (ClassCastException e) {
            return false;
        }
    }
}
