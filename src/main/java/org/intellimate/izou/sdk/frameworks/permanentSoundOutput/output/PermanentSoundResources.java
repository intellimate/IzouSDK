package org.intellimate.izou.sdk.frameworks.permanentSoundOutput.output;

import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.identification.IdentificationManager;
import org.intellimate.izou.identification.IllegalIDException;
import org.intellimate.izou.resource.ResourceBuilderModel;
import org.intellimate.izou.resource.ResourceModel;
import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.frameworks.permanentSoundOutput.resource.UsingSoundResource;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * implements methods to automatically create Resources
 * @author LeanderK
 * @version 1.0
 */
public interface PermanentSoundResources extends ResourceBuilderModel, PermanentSoundUsed {
    /**
     * initialises the Interface
     * @param context the context to use
     */
    default void resourcesInit(Context context) {
        try {
            context.getResources().registerResourceBuilder(this);
        } catch (IllegalIDException e) {
            context.getLogger().error("unable to register ResourceBuilder", e);
        }
    }

    @Override
    default List<? extends ResourceModel> announceResources() {
        List<ResourceModel> resources = new ArrayList<>();
        IdentificationManager.getInstance().getIdentification(this)
                .map(UsingSoundResource::new)
                .ifPresent(resources::add);
        return resources;
    }

    @Override
    default List<? extends EventModel<?>> announceEvents() {
        return null;
    }

    @Override
    default List<ResourceModel> provideResource(List<? extends ResourceModel> list, Optional<EventModel> event) {
        if (isOutputRunning()) {
            return list.stream()
                    .map(resourceModel -> generateResource(resourceModel, event))
                    .filter(Optional::isPresent)
                    .map(optional -> (ResourceModel) optional.get())
                    .collect(Collectors.toList());
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
    default Optional<? extends ResourceModel> generateResource(ResourceModel resourceModel, Optional<EventModel> event) {
        if (resourceModel.getResourceID().equals(UsingSoundResource.ID)) {
            return createUsingSoundResource();
        } else {
            return Optional.empty();
        }
    }

    /**
     * creates a UsingSoundResource ready to return
     * @return a list with (when no error happens) one resource
     */
    default Optional<UsingSoundResource> createUsingSoundResource() {
        return IdentificationManager.getInstance()
                .getIdentification(this)
                .map(UsingSoundResource::new);
    }
}
