package org.intellimate.izou.sdk.frameworks.music.resources;

import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.resource.ResourceModel;
import org.intellimate.izou.sdk.resource.Resource;

/**
 * if this classes resource is true the music-request was meant to be permanent.
 * @author LeanderK
 * @version 1.0
 */
public class PermanentResource extends Resource<Boolean> {
    @SuppressWarnings("SpellCheckingInspection")
    public static final String ID = "izou.music.resource.permanent";

    /**
     * creates a new Resource.
     *
     * @param provider   the Provider of the Resource
     * @param permanent   true if permanent, false if not
     */
    public PermanentResource(Identification provider, Boolean permanent) {
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
}
