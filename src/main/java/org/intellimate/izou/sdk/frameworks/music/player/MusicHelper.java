package org.intellimate.izou.sdk.frameworks.music.player;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.identification.IdentificationManager;
import org.intellimate.izou.sdk.frameworks.music.events.PlayerError;
import org.intellimate.izou.sdk.frameworks.music.events.PlayerUpdate;
import org.intellimate.izou.sdk.frameworks.music.resources.PlaylistResource;
import org.intellimate.izou.sdk.frameworks.music.resources.ProgressResource;
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
        getContext().getLogger().warn("creating start music event without Informations");
        startSound(addOnModule, null, null, null);
    }

    /**
     * fires an StartEvent
     *
     * @param addOnModule the AddonModule
     */
    default void startSound(AddOnModule addOnModule, Playlist playlist, Progress progress, Volume volume) {
        Optional<PlayerUpdate> startEvent = IdentificationManager.getInstance().getIdentification(addOnModule)
                .flatMap(id -> PlayerUpdate.createPlayerUpdate(addOnModule, id));
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
            if (volume != null) {
                startEvent.get().addResource(new VolumeResource(id.get(), volume));
            }
            fire(startEvent.get(), 5);
        }
    }

    default void playerError(String message, AddOnModule addOnModule) {
        Optional<PlayerError> playerError = IdentificationManager.getInstance().getIdentification(this)
                .flatMap(id -> PlayerError.createMusicPlayerError(addOnModule, id, message));
        if (!playerError.isPresent()) {
            getContext().getLogger().error("unable to fire PlayerError");
        } else {
            fire(playerError.get(), 5);
        }
    }
}
