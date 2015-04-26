package org.intellimate.izou.sdk.frameworks.permanentSoundOutput.output;

import org.intellimate.izou.identification.Identifiable;
import org.intellimate.izou.identification.IdentificationManager;
import org.intellimate.izou.sdk.frameworks.permanentSoundOutput.events.EndedEvent;
import org.intellimate.izou.sdk.frameworks.permanentSoundOutput.events.StartEvent;
import org.intellimate.izou.sdk.util.AddOnModule;
import org.intellimate.izou.sdk.util.ContextProvider;
import org.intellimate.izou.sdk.util.FireEvent;

import java.util.Optional;

/**
 * interface which provides
 * @author LeanderK
 * @version 1.0
 */
public interface PermanentSoundHelper extends Identifiable, FireEvent, ContextProvider {

    /**
     * fires an StartEvent
     * @param addOnModule the AddonModule
     */
    default void startSound(AddOnModule addOnModule) {
        Optional<StartEvent> startEvent = IdentificationManager.getInstance().getIdentification(addOnModule)
                .flatMap(id -> StartEvent.createStartEvent(addOnModule, id));
        if (!startEvent.isPresent()) {
            getContext().getLogger().error("unable to fire startEvent");
        } else {
            fire(startEvent.get(), 5);
        }
    }

    /**
     * fires an EndedEvent
     * @param addOnModule the addonModule
     */
    default void endSound(AddOnModule addOnModule) {
        Optional<EndedEvent> startEvent = IdentificationManager.getInstance().getIdentification(addOnModule)
                .flatMap(id -> EndedEvent.createEndedEvent(addOnModule, id));
        if (!startEvent.isPresent()) {
            getContext().getLogger().error("unable to fire startEvent");
        } else {
            fire(startEvent.get(), 5);
        }
    }
}
