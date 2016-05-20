package org.intellimate.izou.sdk.frameworks.common.resources;

import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.identification.IdentificationManager;
import org.intellimate.izou.identification.IdentificationManagerM;
import org.intellimate.izou.identification.IllegalIDException;
import org.intellimate.izou.resource.ResourceBuilderModel;
import org.intellimate.izou.resource.ResourceModel;
import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.frameworks.permanentSoundOutput.resource.UsingSoundResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * base interface to build the resource-providing logic upon (if the project is big enough to have it moved out of
 * the classes)
 * @author LeanderK
 * @version 1.0
 */
public interface ResourcesProviderBase extends ResourceBuilderModel {
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
        IdentificationManagerM.getInstance().getIdentification(this)
                .map(UsingSoundResource::new)
                .ifPresent(resources::add);
        return resources;
    }

    @Override
    default List<? extends EventModel<?>> announceEvents() {
        return new ArrayList<>();
    }

    @Override
    default List<ResourceModel> provideResource(List<? extends ResourceModel> list, Optional<EventModel> event) {
        return list.stream()
                .map(resourceModel -> generateResource(resourceModel, event))
                .filter(Optional::isPresent)
                .map(optional -> (ResourceModel) optional.get())
                .collect(Collectors.toList());
    }

    /**
     * generates the resources
     * @param resourceModel the resourceModel
     * @param event the Event
     * @return a Stream containing the resourceModel(s)
     */
    default Optional<? extends ResourceModel> generateResource(ResourceModel resourceModel, Optional<EventModel> event) {
        return Optional.empty();
    }
}
