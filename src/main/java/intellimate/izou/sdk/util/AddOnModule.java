package intellimate.izou.sdk.util;

import intellimate.izou.sdk.Context;

/**
 * this is the base-class for all AddOn-Modules, it provides various utility methods.
 * @author Leander Kurscheidt
 * @version 1.0
 */
public abstract class AddOnModule implements ContextProvider {
    private Context context;

    public AddOnModule(Context context) {
        this.context = context;
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
}
