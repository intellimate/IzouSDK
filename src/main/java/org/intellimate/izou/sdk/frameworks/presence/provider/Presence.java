package org.intellimate.izou.sdk.frameworks.presence.provider;

import org.intellimate.izou.resource.ResourceModel;

import java.util.HashMap;
import java.util.Optional;

/**
 * this class holds all important information about the Presence of the user
 * @author LeanderK
 * @version 1.0
 */
public class Presence {
    public static final String LEVEL_DESCRIPTOR = "izou.presence.provider.presence.level";
    private final PresenceIndicatorLevel level;
    public static final String PRESENT_DESCRIPTOR = "izou.presence.provider.presence.present";
    private final boolean present;
    public static final String STRICT_DESCRIPTOR = "izou.presence.provider.presence.strict";
    private final boolean strict;
    public static final String KNOWN_DESCRIPTOR = "izou.presence.provider.presence.known";
    private final boolean known;

    /**
     * returns a new Presence-Object
     * @param level the level of the Presence (mostly used internally)
     * @param present whether it is present
     * @param strict whether it is strict
     * @param known whether it is known that the user caused this
     */
    public Presence(PresenceIndicatorLevel level, boolean present, boolean strict, boolean known) {
        this.level = level;
        this.present = present;
        this.strict = strict;
        this.known = known;
    }

    /**
     * returns the (vague) level of the reliability of the data. Mostly used internally)
     * @return the Level
     */
    public PresenceIndicatorLevel getLevel() {
        return level;
    }

    /**
     * whether it is present AND known
     * @return true if present
     */
    public boolean isPresent() {
        if (known) return present;
        else return false;
    }

    /**
     * whether it is strict (very high probability that the user is around)
     * @return true if strict
     */
    public boolean isStrict() {
        return strict;
    }

    /**
     * whether it is known that the user cause the Event
     * @return true if known
     */
    public boolean isKnown() {
        return known;
    }

    /**
     * exports the Presence to a HashMap
     * @return the resulting HashMap
     */
    public HashMap<String, Object> export() {
        HashMap<String, Object> data = new HashMap<>();
        data.put(LEVEL_DESCRIPTOR, level.name());
        data.put(PRESENT_DESCRIPTOR, present);
        data.put(STRICT_DESCRIPTOR, strict);
        data.put(KNOWN_DESCRIPTOR, known);
        return data;
    }

    /**
     * imports (if no errors occurred) the Presence from the ResourceModel
     * @param resourceModel the resourcemodel to import from
     * @return the (optional) presence
     */
    public static Optional<Presence> importPresence(ResourceModel resourceModel) {
        Object resource = resourceModel.getResource();
        try {
            //noinspection unchecked
            HashMap<String, Object> data = (HashMap<String, Object>) resource;
            PresenceIndicatorLevel level;
            try {
                level = PresenceIndicatorLevel.valueOf((String) data.get(LEVEL_DESCRIPTOR));
            } catch (IllegalArgumentException e) {
                level = PresenceIndicatorLevel.VERY_WEAK;
            }
            boolean present = (boolean) data.get(PRESENT_DESCRIPTOR);
            boolean strict = (boolean) data.get(STRICT_DESCRIPTOR);
            boolean known = (boolean) data.get(KNOWN_DESCRIPTOR);
            return Optional.of(new Presence(level, present, strict, known));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
