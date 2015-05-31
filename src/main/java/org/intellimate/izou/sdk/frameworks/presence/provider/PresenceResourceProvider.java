package org.intellimate.izou.sdk.frameworks.presence.provider;

import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.identification.IdentificationManager;
import org.intellimate.izou.resource.ResourceModel;
import org.intellimate.izou.sdk.frameworks.common.resources.ResourcesProviderBase;
import org.intellimate.izou.sdk.frameworks.presence.resources.PresenceResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * provides the resources
 * @author LeanderK
 * @version 1.0
 */
public interface PresenceResourceProvider extends ResourcesProviderBase, PresenceProvider {
    @Override
    default List<? extends ResourceModel> announceResources() {
        List<ResourceModel> resources = new ArrayList<>();
        IdentificationManager.getInstance().getIdentification(this)
                .ifPresent(id -> {
                    resources.add(new PresenceResource(id));
                });
        return resources;
    }

    /**
     * generates the resources
     * @param resourceModel the resourceModel
     * @param event the Event
     * @return a Stream containing the resourceModel(s)
     */
    @Override
    default Optional<? extends ResourceModel> generateResource(ResourceModel resourceModel, Optional<EventModel> event) {
        if (resourceModel.getResourceID().equals(PresenceResource.ID)) {
            return createPresenceResource();
        } else {
            return ResourcesProviderBase.super.generateResource(resourceModel, event);
        }
    }

    /**
     * creates a PresenceResource ready to return
     * @return the resource
     */
    default Optional<PresenceResource> createPresenceResource() {
        return IdentificationManager.getInstance()
                .getIdentification(this)
                .map(id -> new PresenceResource(id, new Presence(getLevel(), isPresent(), isStrict(), isKnown())));
    }
}
