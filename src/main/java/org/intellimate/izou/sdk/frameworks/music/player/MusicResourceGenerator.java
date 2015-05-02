package org.intellimate.izou.sdk.frameworks.music.player;

import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.identification.IdentificationManager;
import org.intellimate.izou.resource.ResourceModel;
import org.intellimate.izou.sdk.frameworks.music.resources.*;
import org.intellimate.izou.sdk.frameworks.permanentSoundOutput.output.PermanentSoundResources;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * generates the Resources
 * @author LeanderK
 * @version 1.0
 */
public interface MusicResourceGenerator extends PermanentSoundResources, MusicProvider, MusicHelper {
    String PROVIDE_RESOURCE_ERROR = "Error while trying to provide resource: ";
    String PROVIDE_RESOURCE_ERROR_NOT_CAPABLE = PROVIDE_RESOURCE_ERROR + " not Capable to generate ";
    String PROVIDE_RESOURCE_ERROR_GENERATING = PROVIDE_RESOURCE_ERROR + " an Error occurred while trying to generate ";
    @Override
    default List<? extends ResourceModel> announceResources() {
        List<ResourceModel> list = new ArrayList<>();
        IdentificationManager.getInstance().getIdentification(this)
                .ifPresent(id -> {
                    list.add(new CapabilitiesResource(id));
                    list.add(new NowPlayingResource(id));
                    list.add(new ProgressResource(id));
                    list.add(new TrackInfoResource(id));
                    list.add(new VolumeResource(id));
                });
        list.addAll(PermanentSoundResources.super.announceResources());
        return list;
    }

    /**
     * generates the resources
     *
     * @param resourceModel the resourceModel
     * @param event         the Event
     * @return a Stream containing the resourceModel(s)
     */
    @Override
    default Optional<? extends ResourceModel> generateResource(ResourceModel resourceModel, Optional<EventModel> event) {
        switch (resourceModel.getResourceID()) {
            case CapabilitiesResource.RESOURCE_ID : return createCapabilitiesResource();
            case NowPlayingResource.ID: return createNowPlayingResource();
            case PlayerResource.RESOURCE_ID : return createPlayerResource();
            case ProgressResource.ID : return createProgressResource();
            case TrackInfoResource.RESOURCE_ID: return createTrackInfoResource();
            case VolumeResource.ID: return createVolumeResource();
            default: return PermanentSoundResources.super.generateResource(resourceModel, event);
        }
    }

    /**
     * generates the VolumeResource
     * @return the Resource
     */
    default Optional<? extends ResourceModel> createVolumeResource() {
        if (getVolume() == null) {
            getContext().getLogger().error(PROVIDE_RESOURCE_ERROR_GENERATING + "VolumeResource: returned null");
            return Optional.empty();
        }
        Optional<VolumeResource> playerResource = IdentificationManager.getInstance()
                .getIdentification(this)
                .map(id -> new VolumeResource(id, getVolume()));
        if (!playerResource.isPresent()) {
            getContext().getLogger().error(PROVIDE_RESOURCE_ERROR_GENERATING + "VolumeResource");
        }
        return playerResource;
    }

    /**
     * generates the ProgressResource
     * @return the Resource
     */
    default Optional<? extends ResourceModel> createTrackInfoResource() {
        if (!getCapabilities().providesTrackInfo()) {
            getContext().getLogger().error(PROVIDE_RESOURCE_ERROR_NOT_CAPABLE + "TrackInfo");
            return Optional.empty();
        } else if (getCurrentPlaylist() == null || getCurrentPlaylist().getCurrent() == null) {
            getContext().getLogger().error(PROVIDE_RESOURCE_ERROR_GENERATING + "TrackInfo: returned null");
            return Optional.empty();
        } else {
            Optional<TrackInfoResource> nowPlayingResource = IdentificationManager.getInstance()
                    .getIdentification(this)
                    .map(id -> new TrackInfoResource(id, getCurrentPlaylist().getCurrent()));
            if (!nowPlayingResource.isPresent()) {
                getContext().getLogger().error(PROVIDE_RESOURCE_ERROR_GENERATING + "TrackInfo");
            }
            return nowPlayingResource;
        }
    }

    /**
     * generates the ProgressResource
     * @return the Resource
     */
    default Optional<? extends ResourceModel> createProgressResource() {
        if (!getCapabilities().providesTrackInfo()) {
            getContext().getLogger().error(PROVIDE_RESOURCE_ERROR_NOT_CAPABLE + "ProgressResource");
            return Optional.empty();
        } else if (getCurrentProgress() == null) {
            getContext().getLogger().error(PROVIDE_RESOURCE_ERROR_GENERATING + "ProgressResource: returned null");
            return Optional.empty();
        } else {
            Optional<ProgressResource> nowPlayingResource = IdentificationManager.getInstance()
                    .getIdentification(this)
                    .map(id -> new ProgressResource(id, getCurrentProgress()));
            if (!nowPlayingResource.isPresent()) {
                getContext().getLogger().error(PROVIDE_RESOURCE_ERROR_GENERATING + "ProgressResource");
            }
            return nowPlayingResource;
        }
    }

    /**
     * generates the PlayerResource
     * @return the Resource
     */
    default Optional<? extends ResourceModel> createPlayerResource() {
        Optional<PlayerResource> playerResource = IdentificationManager.getInstance()
                .getIdentification(this)
                .map(PlayerResource::new);
        if (!playerResource.isPresent()) {
            getContext().getLogger().error(PROVIDE_RESOURCE_ERROR_GENERATING + "PlayerResource");
        }
        return playerResource;
    }

    /**
     * generates NowPlayingResource
     * @return the Resource
     */
    default Optional<? extends ResourceModel> createNowPlayingResource() {
        if (!getCapabilities().providesTrackInfo()) {
            getContext().getLogger().error(PROVIDE_RESOURCE_ERROR_NOT_CAPABLE + "NowPlayingResource");
            return Optional.empty();
        } else if (getCurrentPlaylist() == null) {
            getContext().getLogger().error(PROVIDE_RESOURCE_ERROR_GENERATING + "NowPlayingResource: returned null");
            return Optional.empty();
        } else {
            Optional<NowPlayingResource> nowPlayingResource = IdentificationManager.getInstance()
                    .getIdentification(this)
                    .map(id -> new NowPlayingResource(id, getCurrentPlaylist()));
            if (!nowPlayingResource.isPresent()) {
                getContext().getLogger().error(PROVIDE_RESOURCE_ERROR_GENERATING + "NowPlayingResource");
            }
            return nowPlayingResource;
        }
    }

    /**
     * creates the CapabilitiesResource
     * @return an optional which should contain the CapabilitiesResource
     */
    default Optional<? extends ResourceModel> createCapabilitiesResource() {
        if (getCurrentPlaylist() == null) {
            getContext().getLogger().error(PROVIDE_RESOURCE_ERROR_GENERATING + "Capabilities: returned null");
            return Optional.empty();
        }
        return IdentificationManager.getInstance().getIdentification(this)
                .map(id -> new CapabilitiesResource(id, getCapabilities()));
    }
}
