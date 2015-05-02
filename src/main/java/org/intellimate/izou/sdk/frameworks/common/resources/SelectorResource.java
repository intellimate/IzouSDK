package org.intellimate.izou.sdk.frameworks.common.resources;

import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.identification.Identifiable;
import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.resource.ResourceModel;
import org.intellimate.izou.sdk.resource.Resource;

import java.util.Collections;
import java.util.Optional;

/**
 * A resource which holds an Identification used for example to select the Player
 * @author LeanderK
 * @version 1.0
 */
public class SelectorResource extends Resource<Identification> {
    public static final String RESOURCE_ID = "izou.common.resource.selector";

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param consumer   the Provider of the Resource
     */
    public SelectorResource(Identification consumer) {
        super(RESOURCE_ID, null, null, consumer);
    }

    /**
     * creates a new Resource.
     * This method is thread-safe.
     *
     * @param consumer       the consumer of the Resource
     * @param identification the resource
     */
    public SelectorResource(Identification consumer, Identification identification) {
        super(RESOURCE_ID, identification, identification, consumer);
    }

    /**
     * returns true if the identifiable is the target of the EventModel
     * @param eventModel the EventModel to check
     * @param identifiable the identifiable to check against
     * @return maybe true if found
     */
    public static Optional<Boolean> isTarget(EventModel eventModel, Identifiable identifiable) {
        if (eventModel.getListResourceContainer()
                .providesResource(Collections.singletonList(SelectorResource.RESOURCE_ID))) {
               return Optional.of(eventModel.getListResourceContainer()
                       .provideResource(SelectorResource.RESOURCE_ID)
                       .stream()
                       .map(ResourceModel::getResource)
                       .filter(resource -> resource instanceof Identification)
                       .map(object -> (Identification) object)
                       .anyMatch(identifiable::isOwner));
        } else {
            return Optional.empty();
        }
    }

    /**
     * returns true if the identifiable is the target of the EventModel
     * @param eventModel the EventModel to check
     * @param identification the identification to check against
     * @return maybe true if found
     */
    public static Optional<Boolean> isTarget(EventModel eventModel, Identification identification) {
        if (eventModel.getListResourceContainer()
                .providesResource(Collections.singletonList(SelectorResource.RESOURCE_ID))) {
            return Optional.of(eventModel.getListResourceContainer()
                    .provideResource(SelectorResource.RESOURCE_ID)
                    .stream()
                    .map(ResourceModel::getResource)
                    .filter(resource -> resource instanceof Identification)
                    .map(object -> (Identification) object)
                    .anyMatch(identification::equals));
        } else {
            return Optional.empty();
        }
    }
}
