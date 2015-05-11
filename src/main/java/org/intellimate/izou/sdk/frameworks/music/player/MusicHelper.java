package org.intellimate.izou.sdk.frameworks.music.player;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.identification.IdentificationManager;
import org.intellimate.izou.sdk.events.Event;
import org.intellimate.izou.sdk.frameworks.common.resources.SelectorResource;
import org.intellimate.izou.sdk.frameworks.music.events.PlayerError;
import org.intellimate.izou.sdk.frameworks.music.events.PlayerUpdate;
import org.intellimate.izou.sdk.frameworks.music.resources.*;
import org.intellimate.izou.sdk.frameworks.permanentSoundOutput.events.StartEvent;
import org.intellimate.izou.sdk.frameworks.permanentSoundOutput.output.PermanentSoundHelper;

import java.util.Optional;

/**
 * here lies some utility-methods for the music-player
 * @author LeanderK
 * @version 1.0
 */
public interface MusicHelper extends PermanentSoundHelper {
    /**
     * fires an StartEvent
     *
     */
    @Override
    default void startedSound() {
        getContext().getLogger().warn("creating start music event without Information");
        startedSound(null, null, null, null);
    }

    /**
     * fires an StartEvent
     *
     * @param playlist the playlist or null
     * @param progress the progress or null
     * @param trackInfo the trackInfo or null
     * @param volume the volume or null
     */
    default void startedSound(Playlist playlist, Progress progress, TrackInfo trackInfo, Volume volume) {
        Optional<Event> startEvent = IdentificationManager.getInstance().getIdentification(this)
                .flatMap(PlayerUpdate::createPlayerUpdate)
                .map(event -> {
                    event.addDescriptor(StartEvent.ID);
                    return event;
                });
        Optional<Identification> id = IdentificationManager.getInstance().getIdentification(this);
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
     */
    default void playerError(String message) {
        Optional<PlayerError> playerError = IdentificationManager.getInstance().getIdentification(this)
                .flatMap(id -> PlayerError.createMusicPlayerError(id, message));
        if (!playerError.isPresent()) {
            getContext().getLogger().error("unable to fire PlayerError");
        } else {
            fire(playerError.get(), 5);
        }
    }

    /**
     * fires an Music-Player-Error
     * @param message the message
     * @param target the one who caused the error
     */
    default void playerError(String message, Identification target) {
        Optional<PlayerError> playerError = IdentificationManager.getInstance().getIdentification(this)
                .flatMap(id -> PlayerError.createMusicPlayerError(id, message));
        Optional<Identification> id = IdentificationManager.getInstance().getIdentification(this);
        if (!playerError.isPresent() || !id.isPresent()) {
            getContext().getLogger().error("unable to fire PlayerError");
        } else {
            playerError.get().addResource(new SelectorResource(id.get(), target));
            fire(playerError.get(), 5);
        }
    }

    /**
     * fires an update event which notifies that parameters have changed
     * @param playlist the optional playlist
     * @param progress the optional progress
     * @param trackInfo the optional trackInfo
     * @param volume the optional volume
     */
    default void updatePlayInfo(Playlist playlist, Progress progress, TrackInfo trackInfo, Volume volume) {
        Optional<PlayerUpdate> updateEvent = IdentificationManager.getInstance().getIdentification(this)
                .flatMap(PlayerUpdate::createPlayerUpdate);
        Optional<Identification> id = IdentificationManager.getInstance().getIdentification(this);
        if (!updateEvent.isPresent() || !id.isPresent()) {
            getContext().getLogger().error("unable to fire PlayerUpdate");
        } else {
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
     * @param playlist the optional playlist
     */
    default void updatePlayInfo(Playlist playlist) {
        updatePlayInfo(playlist, null, null, null);
    }

    /**
     * fires an update event which notifies that parameters have changed
     * @param progress the optional progress
     */
    default void updatePlayInfo(Progress progress) {
        updatePlayInfo(null, progress, null, null);
    }

    /**
     * fires an update event which notifies that parameters have changed
     * @param trackInfo the optional trackInfo
     */
    default void updatePlayInfo(TrackInfo trackInfo) {
        updatePlayInfo(null, null, trackInfo, null);
    }

    /**
     * fires an update event which notifies that parameters have changed
     * @param volume the optional volume
     */
    default void updatePlayInfo(Volume volume) {
        updatePlayInfo(null, null, null, volume);
    }

    /**
     * updates the PlaybackState
     * @param playbackState the playbackState
     */
    default void updateStateInfo(PlaybackState playbackState) {
        if (playbackState == null)
            return;
        Optional<PlayerUpdate> updateEvent = IdentificationManager.getInstance().getIdentification(this)
                .flatMap(PlayerUpdate::createPlayerUpdate);
        Optional<Identification> id = IdentificationManager.getInstance().getIdentification(this);
        if (!updateEvent.isPresent() || !id.isPresent()) {
            getContext().getLogger().error("unable to fire PlayerUpdate");
        } else {
            updateEvent.get().addResource(new PlaybackStateResource(id.get(), playbackState));
            fire(updateEvent.get(), 5);
        }
    }
}
