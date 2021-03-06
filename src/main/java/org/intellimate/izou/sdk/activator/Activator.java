package org.intellimate.izou.sdk.activator;

import org.intellimate.izou.activator.ActivatorModel;
import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.util.AddOnModule;
import org.intellimate.izou.sdk.util.FireEvent;

/**
 * The Task of an Activator is to listen for whatever you choose to implement and fires events to notify a change.
 * <p>
 * The Activator always runs in the Background, just overwrite activatorStarts(). To use Activator simply extend from it
 * and hand an instance over to the ActivatorManager.
 */
public abstract class Activator extends AddOnModule implements ActivatorModel, FireEvent {
    private boolean stop = false;

    public Activator(Context context, String ID) {
        super(context, ID);
    }

    @Override
    public Boolean call() throws Exception {
        while (!stop) {
            activatorStarts();
            Thread.sleep(10);
        }
        return false;
    }

    /**
     * This method will be called in a loop.
     */
    public abstract void activatorStarts();

    /**
     * stops the activator.
     */
    public void stop() {
        stop = true;
    }
}
