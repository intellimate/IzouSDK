package org.intellimate.izou.sdk.util;

import org.intellimate.izou.identification.Identifiable;
import org.intellimate.izou.identification.IdentificationManager;
import org.intellimate.izou.identification.IdentificationManagerM;
import org.intellimate.izou.sdk.Context;

/**
 * This is the base-class for all AddOn-Modules, it provides various utility methods.
 *
 * @author Leander Kurscheidt
 * @version 1.0
 */
public abstract class AddOnModule implements ContextProvider, Loggable, LoggedExceptionCallback, Identifiable {
    private final Context context;
    private final String ID;

    /**
     * initializes the Module
     * @param context the current Context
     * @param ID the ID
     */
    public AddOnModule(Context context, String ID) {
        this.context = context;
        this.ID = ID;
        if(!IdentificationManagerM.getInstance().registerIdentification(this)) {
            context.getLogger().fatal("Failed to register with identification manager " + getID());
        }
    }

    /**
     * returns the instance of Context
     *
     * @return the instance of Context
     */
    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public String getID() {
        return ID;
    }
}
