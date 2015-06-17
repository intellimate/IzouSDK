package org.intellimate.izou.sdk.frameworks.presence.resources;

import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.resource.ResourceModel;
import org.intellimate.izou.sdk.resource.Resource;

import java.util.Optional;

/**
 * this Resource holds an integer containing the Seconds passed since the User was last encountered
 * @author LeanderK
 * @version 1.0
 */
public class LastEncountered extends Resource<Long> {
    public static final String ID = "izou.presence.resources.lastencountered";

    /**
     * creates a new Resource.
     *
     * @param provider   the Provider of the Resource
     * @param timePassed the time passed in seconds
     */
    public LastEncountered(Identification provider, Long timePassed) {
        super(ID, provider, timePassed);
    }

    public Optional<Long> getTimePassed(EventModel eventModel) {
        if (eventModel.getListResourceContainer().containsResourcesFromSource(ID)) {
            return eventModel
                    .getListResourceContainer()
                    .provideResource(ID)
                    .stream()
                    .map(ResourceModel::getResource)
                    .filter(ob -> ob instanceof Long)
                    .map(ob -> (Long) ob)
                    .findAny();
        } else {
            return Optional.empty();
        }
    }
}
