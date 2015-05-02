package org.intellimate.izou.sdk.frameworks.music.player;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.identification.IdentificationManager;
import org.intellimate.izou.sdk.events.Event;
import org.intellimate.izou.sdk.frameworks.common.resources.SelectorResource;
import org.intellimate.izou.sdk.frameworks.music.events.PlayerError;
import org.intellimate.izou.sdk.frameworks.music.events.PlayerUpdate;
import org.intellimate.izou.sdk.frameworks.music.resources.PlaylistResource;
import org.intellimate.izou.sdk.frameworks.music.resources.ProgressResource;
import org.intellimate.izou.sdk.frameworks.music.resources.TrackInfoResource;
import org.intellimate.izou.sdk.frameworks.music.resources.VolumeResource;
import org.intellimate.izou.sdk.frameworks.permanentSoundOutput.events.StartEvent;
import org.intellimate.izou.sdk.frameworks.permanentSoundOutput.output.PermanentSoundHelper;
import org.intellimate.izou.sdk.util.AddOnModule;

import java.util.Optional;

/**
 * @author LeanderK
 * @version 1.0
 */
public interface MusicHelper extends PermanentSoundHelper {
    /**
     * fires an StartEvent
     *
     * @param addOnModule the AddonModule
     */
    @Override
    default void startSound(AddOnModule addOnModule) {
        getContext().getLogger().warn("creating start music event without Information");
        startSound(addOnModule, null, null, null, null);
    }

    /**
     * fires an StartEvent
     *
     * @param addOnModule the AddonModule
     */
    default void startSound(AddOnModule addOnModule, Playlist playlist, Progress progress, TrackInfo trackInfo, Volume volume) {
        Optional<Event> startEvent = IdentificationManager.getInstance().getIdentification(addOnModule)
                .flatMap(id -> PlayerUpdate.createPlayerUpdate(addOnModule, id))
                .map(event -> event.addDescriptor(StartEvent.ID));
        Optional<Identification> id = IdentificationManager.getInstance().getIdentification(addOnModule);
        if (!startEvent.isPresent() || !id.isPresent()) {
            getContext().getLogger().error("unable to fire PlayerUpdate");
        } else {
            startEvent.get().addDescriptor(StartEvent.ID);
            if (playlist != null) {
                startEvent.get().addResource(new PlaylistResource(id.get(), playlist));
            }
            if (progress != null) {
                startEvent.get().addResource(new ProgressResource(id.get(), progress));
            }
            if (trackInfo != null) {
                startEvent.get().addResource(new TrackInfoResource(id.get(), trackInfo));
            }
            if (volume != null) {
                startEvent.get().addResource(new VolumeResource(id.get(), volume));
            }
            fire(startEvent.get(), 5);
        }
    }

    /**
     * fires an Music-Player-Error
     * @param message the message
     * @param addOnModule the addonModule which should fire it
     */
    default void playerError(String message, AddOnModule addOnModule) {
        Optional<PlayerError> playerError = IdentificationManager.getInstance().getIdentification(this)
                .flatMap(id -> PlayerError.createMusicPlayerError(addOnModule, id, message));
        if (!playerError.isPresent()) {
            getContext().getLogger().error("unable to fire PlayerError");
        } else {
            fire(playerError.get(), 5);
        }
    }

    /**
     * fires an Music-Player-Error
     * @param message the message
     * @param addOnModule the addonModule which should fire it
     * @param target the one who caused the error
     */
    default void playerError(String message, AddOnModule addOnModule, Identification target) {
        Optional<PlayerError> playerError = IdentificationManager.getInstance().getIdentification(this)
                .flatMap(id -> PlayerError.createMusicPlayerError(addOnModule, id, message));
        Optional<Identification> id = IdentificationManager.getInstance().getIdentification(addOnModule);
        if (!playerError.isPresent() || !id.isPresent()) {
            getContext().getLogger().error("unable to fire PlayerError");
        } else {
            playerError.get().addResource(new SelectorResource(id.get(), target));
            fire(playerError.get(), 5);
        }
    }

    /**
     * fires an update event which notifies that parameters have changed
     * @param addOnModule the AddonModule responsible
     * @param playlist the optional playlist
     * @param progress the optional progress
     * @param trackInfo the optional trackInfo
     * @param volume the optional volume
     */
    default void updatePlayInfo(AddOnModule addOnModule, Playlist playlist, Progress progress, TrackInfo trackInfo, Volume volume) {
        Optional<PlayerUpdate> updateEvent = IdentificationManager.getInstance().getIdentification(addOnModule)
                .flatMap(id -> PlayerUpdate.createPlayerUpdate(addOnModule, id));
        Optional<Identification> id = IdentificationManager.getInstance().getIdentification(addOnModule);
        if (!updateEvent.isPresent() || !id.isPresent()) {
            getContext().getLogger().error("unable to fire PlayerUpdate");
        } else {
            updateEvent.get().addDescriptor(StartEvent.ID);
            if (playlist != null) {
                updateEvent.get().addResource(new PlaylistResource(id.get(), playlist));
            }
            if (progress != null) {
                updateEvent.get().addResource(new ProgressResource(id.get(), progress));
            }
            if (trackInfo != null) {
                updateEvent.get().addResource(new TrackInfoResource(id.get(), trackInfo));
            }
            if (volume != null) {
                updateEvent.get().addResource(new VolumeResource(id.get(), volume));
            }
            fire(updateEvent.get(), 5);
        }
    }

    /**
     * fires an update event which notifies that parameters have changed
     * @param addOnModule the AddonModule responsible
     * @param playlist the optional playlist
     */
    default void updatePlayInfo(AddOnModule addOnModule, Playlist playlist) {
        updatePlayInfo(addOnModule, playlist, null, null, null);
    }

    /**
     * fires an update event which notifies that parameters have changed
     * @param addOnModule the AddonModule responsible
     * @param progress the optional progress
     */
    default void updatePlayInfo(AddOnModule addOnModule, Progress progress) {
        updatePlayInfo(addOnModule, null, progress, null, null);
    }

    /**
     * fires an update event which notifies that parameters have changed
     * @param addOnModule the AddonModule responsible
     * @param trackInfo the optional trackInfo
     */
    default void updatePlayInfo(AddOnModule addOnModule, TrackInfo trackInfo) {
        updatePlayInfo(addOnModule, null, null, trackInfo, null);
    }

    /**
     * fires an update event which notifies that parameters have changed
     * @param addOnModule the AddonModule responsible
     * @param volume the optional volume
     */
    default void updatePlayInfo(AddOnModule addOnModule, Volume volume) {
        updatePlayInfo(addOnModule, null, null, null, volume);
    }
}
