package org.intellimate.izou.sdk;


import org.intellimate.izou.activator.ActivatorModel;
import org.intellimate.izou.events.EventsControllerModel;
import org.intellimate.izou.output.OutputControllerModel;
import org.intellimate.izou.output.OutputExtensionModel;
import org.intellimate.izou.output.OutputPluginModel;
import org.intellimate.izou.sdk.addon.AddOn;
import org.intellimate.izou.sdk.contentgenerator.ContentGenerator;
import org.intellimate.izou.sdk.server.SDKRouter;

/**
 * @author LeanderK
 * @version 1.0
 */
@SuppressWarnings("WeakerAccess")
public class AddOnImpl extends AddOn {
    public AddOnImpl() {
        super(AddOnImpl.class.getCanonicalName());
    }

    /**
     * This method gets called before registering
     */
    @Override
    public void prepare() {
        setRouter(new SDKRouter(getContext()));
    }

    @Override
    public ActivatorModel[] registerActivator() {
        return new ActivatorModel[0];
    }

    @Override
    public ContentGenerator[] registerContentGenerator() {
        return new ContentGenerator[0];
    }

    @Override
    public EventsControllerModel[] registerEventController() {
        return new EventsControllerModel[0];
    }

    @Override
    public OutputPluginModel[] registerOutputPlugin() {
        return new OutputPluginModel[0];
    }

    @Override
    public OutputExtensionModel[] registerOutputExtension() {
        return new OutputExtensionModel[0];
    }

    @Override
    public OutputControllerModel[] registerOutputController() {
        return new OutputControllerModel[0];
    }
}
