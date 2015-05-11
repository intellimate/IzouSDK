package org.intellimate.izou.sdk.frameworks.presence.provider.template;

import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.events.EventsController;
import org.intellimate.izou.sdk.frameworks.presence.provider.Presence;
import org.intellimate.izou.sdk.frameworks.presence.provider.PresenceHelper;
import org.intellimate.izou.sdk.frameworks.presence.provider.PresenceIndicatorLevel;
import org.intellimate.izou.sdk.frameworks.presence.provider.PresenceResourceProvider;
import org.intellimate.izou.sdk.frameworks.presence.resources.PresenceResource;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * the base template class for addons able to conclude about constant presence.
 * <p>
 * If the addon is based on encounters with the user and not able to conclude constant presence from the information,
 * it should pass not extend this class, if not (for example if it monitors wifi) it can. They can just fire the
 * Presence-Event on encountering.
 * It is expected that addons extending this class are pretty sure that the user is around and not a random person.
 * </p>
 * @author LeanderK
 * @version 1.0
 */
@SuppressWarnings("unused")
public abstract class PresenceConstant extends EventsController implements PresenceHelper, PresenceResourceProvider {
    private boolean present = false;
    private  boolean globalPresent = false;
    private boolean globalStrictPresent = false;
    private final boolean strict;
    private final PresenceIndicatorLevel level;
    /**
     * if the PresenceIndicatorLevel is under Weak, it will control events if it is still (one of) the most Vague
     */
    private AtomicBoolean mostVague = new AtomicBoolean(true);

    /**
     * @param context the context
     * @param ID      the ID
     * @param strict  whether it is strict (very high probability that the user is around)
     * @param level   the level
     * @param constant if the addon is based on encounters with the user and not able to conclude constant presence from
     *                 the information, it should pass false, if not (for example if it monitors wifi) it should return true.
     *
     */
    public PresenceConstant(Context context, String ID, boolean strict, PresenceIndicatorLevel level, boolean constant) {
        super(context, ID);
        this.strict = strict;
        this.level = level;
    }

    /**
     * Controls whether the fired Event should be dispatched to all the listeners. This method should execute quickly
     *
     * @param eventModel the Event
     * @return true if it should dispatch, false if not
     */
    @Override
    public boolean controlEvents(EventModel eventModel) {
        if (level.compareTo(PresenceIndicatorLevel.WEAK) >= 0) {
            return present;
        } else //noinspection SimplifiableIfStatement
            if (level.compareTo(PresenceIndicatorLevel.WEAK) < 0 && mostVague.get()) {
            return present;
        } else {
            return true;
        }
    }

    /**
     * true if the addon can guarantee that the user is around, false if not
     *
     * @return true if it can guarantee it, false if not
     */
    @Override
    public boolean isStrict() {
        return strict;
    }

    /**
     * gets the PresenceIndicatorLevel of the addon (mainly used for communication between presence-providing addons)
     *
     * @return the Level
     */
    @Override
    public PresenceIndicatorLevel getLevel() {
        return level;
    }

    /**
     * returns true if the user might be/is present
     *
     * @return true if present
     */
    @Override
    public boolean isPresent() {
        return present;
    }

    /**
     * when this method is called the present-status was changed
     *
     * @param present true if present, false if not
     */
    @Override
    public void setGlobalPresent(boolean present) {
        globalPresent = present;
    }

    /**
     * returns true if the user is first encountered in the current mode
     */
    @Override
    public boolean isFirstEncountering() {
        if (strict && !globalStrictPresent) {
            return true;
        } else if (!globalPresent) {
            return true;
        }
        return false;
    }

    /**
     * when this method is called, the strict-present status was changed
     *
     * @param present true if present, false if not
     */
    @Override
    public void setGlobalStrictPresent(boolean present) {
        globalStrictPresent = present;
    }

    /**
     * sets the presence
     * @param present true for presence, false if not
     */
    public void setPresence(boolean present) {
        if (this.present == present)
            return;
        this.present = present;
        updateVague();
        if (present) {
            firePresence(true);
        } else {
            fireLeaving();
        }
    }

    /**
     * updates the boolean whether it is the mode vague
     */
    private void updateVague() {
        generateResource(PresenceResource.ID)
            .orElse(CompletableFuture.completedFuture(new ArrayList<>()))
            .thenAccept(list -> mostVague.set(list.stream()
                            .map(Presence::importPresence)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .map(Presence::getLevel)
                            .noneMatch(level -> level.compareTo(getLevel()) > 0))
            );
    }
}
