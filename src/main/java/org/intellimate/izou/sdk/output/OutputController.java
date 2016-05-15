package org.intellimate.izou.sdk.output;

import org.intellimate.izou.output.OutputControllerManager;
import org.intellimate.izou.output.OutputControllerModel;
import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.util.AddOnModule;

/**
 * <p>
 *     An OutputController allows you to control external output devices that are attached to Izou.
 * </p>
 * <p>
 *     For example, a music addon might control what content is played on a TV, but it might not
 *     control the state of the TV (whether it is turned on or off), what mode the TV is in etc. So prior
 *     to the playback of music, the output plugin might call the output controller for the TV and tell it to turn
 *     itself on and to switch to the audio playback mode so that the music can actually be heard.
 * </p>
 * <p>
 *     Thus the OutputController controls the state of an external output device.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public abstract class OutputController extends AddOnModule implements OutputControllerModel {
    public static String controllerID = null;

    /**
     * Creates a new OutputController.
     *
     * @param context The {@link Context} of the AddOn.
     * @param id The ID of the OutputController.
     */
    public OutputController(Context context, String id)  {
        super(context, id);
        controllerID = id;
    }

    /**
     * Gets the controllerID of this OutputController. It can be used to retrieve the OutputController from
     * the {@link OutputControllerManager}.
     *
     * @return The controllerID of this OutputController.
     */
    public static String getControllerID() {
        return controllerID;
    }
}
