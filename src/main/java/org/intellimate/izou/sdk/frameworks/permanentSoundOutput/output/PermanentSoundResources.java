package org.intellimate.izou.sdk.frameworks.permanentSoundOutput.output;

import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.identification.IdentificationManager;
import org.intellimate.izou.identification.IdentificationManagerM;
import org.intellimate.izou.resource.ResourceModel;
import org.intellimate.izou.sdk.frameworks.common.resources.ResourcesProviderBase;
import org.intellimate.izou.sdk.frameworks.permanentSoundOutput.resource.UsingSoundResource;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * implements methods to automatically create Resources
 * @author LeanderK
 * @version 1.0
 */
public interface PermanentSoundResources extends ResourcesProviderBase, PermanentSoundUsed {

    @Override
    default List<? extends ResourceModel> announceResources() {
        List<ResourceModel> resources = new ArrayList<>();
        IdentificationManagerM.getInstance().getIdentification(this)
                .map(UsingSoundResource::new)
                .ifPresent(resources::add);
        return resources;
    }

    @Override
    default List<ResourceModel> provideResource(List<? extends ResourceModel> list, Optional<EventModel> event) {
        if (isOutputRunning()) {
            return ResourcesProviderBase.super.provideResource(list, event);
        } else {
            return new LinkedList<>();
        }
    }

    /**
     * generates the resources
     * @param resourceModel the resourceModel
     * @param event the Event
     * @return a Stream containing the resourceModel(s)
     */
    @Override
    default Optional<? extends ResourceModel> generateResource(ResourceModel resourceModel, Optional<EventModel> event) {
        if (resourceModel.getResourceID().equals(UsingSoundResource.ID)) {
            return createUsingSoundResource();
        } else {
            return ResourcesProviderBase.super.generateResource(resourceModel, event);
        }
    }

    /**
     * creates a UsingSoundResource ready to return
     * @return a list with (when no error happens) one resource
     */
    default Optional<UsingSoundResource> createUsingSoundResource() {
        return IdentificationManagerM.getInstance()
                .getIdentification(this)
                .map(UsingSoundResource::new);
    }
}
