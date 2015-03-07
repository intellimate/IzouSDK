package intellimate.izou.sdk.addon;

import intellimate.izou.activator.Activator;
import intellimate.izou.events.EventsController;
import intellimate.izou.identification.IllegalIDException;
import intellimate.izou.output.OutputExtension;
import intellimate.izou.output.OutputPlugin;
import intellimate.izou.sdk.Context;
import intellimate.izou.sdk.specification.ContentGenerator;
import intellimate.izou.sdk.util.ContextProvider;
import intellimate.izou.sdk.util.Loggable;
import intellimate.izou.sdk.util.LoggedExceptionCallback;
import ro.fortsoft.pf4j.PluginWrapper;

/**
 * All AddOns must extend this Class.
 *
 * It will be instantiated and its registering-methods will be called by the PluginManager.
 * This class has method for a properties-file named addOnID.properties (AddOnsID in the form: package.class)
 */
public abstract class AddOn implements intellimate.izou.addon.AddOn, ContextProvider, Loggable, LoggedExceptionCallback {
    private final String addOnID;
    private Context context;
    private PluginWrapper plugin;

    /**
     * the default constructor for AddOns
     * @param addOnID the ID of the Plugin in the form: package.class
     */
    public AddOn(String addOnID) {
        this.addOnID = addOnID;
    }

    /**
     * this method is used to register the modules
     */
    @Override
    public void register() {
        for (ContentGenerator contentGenerator : registerContentGenerator()) {
            try {
                getContext().getResources().registerResourceBuilder(contentGenerator);
            } catch (IllegalIDException e) {
                context.getLogger().fatal("Illegal Id for Module: " + contentGenerator.getID(), e);
            }
        }

        for (EventsController eventsController : registerEventController()) {
            try {
                getContext().getEvents().distributor().registerEventsController(eventsController);
            } catch (IllegalIDException e) {
                context.getLogger().fatal("Illegal Id for Module: " + eventsController.getID(), e);
            }
        }

        for (OutputPlugin outputPlugin : registerOutputPlugin()) {
            try {
                getContext().getOutput().addOutputPlugin(outputPlugin);
            } catch (IllegalIDException e) {
                context.getLogger().fatal("Illegal Id for Module: " + outputPlugin.getID(), e);
            }
        }

        for (OutputExtension outputExtension : registerOutputExtension()) {
            try {
                getContext().getOutput().addOutputExtension(outputExtension);
            } catch (IllegalIDException e) {
                context.getLogger().fatal("Illegal Id for Module: " + outputExtension.getID(), e);
            }
        }

        for (Activator activator : registerActivator()) {
            try {
                getContext().getActivators().addActivator(activator);
            } catch (IllegalIDException e) {
                context.getLogger().fatal("Illegal Id for Module: " + activator.getID(), e);
            }
        }
    }

    /**
     * this method gets called before registering
     */
    public abstract void prepare();

    /**
     * use this method to register (if needed) your Activators.
     * @return Array containing Instances of Activators
     */
    public abstract Activator[] registerActivator();

    /**
     * use this method to register (if needed) your ContentGenerators.
     * @return Array containing Instances of ContentGenerators
     */
    public abstract ContentGenerator[] registerContentGenerator();

    /**
     * use this method to register (if needed) your EventControllers.
     * @return Array containing Instances of EventControllers
     */
    public abstract EventsController[] registerEventController();

    /**
     * use this method to register (if needed) your OutputPlugins.
     * @return Array containing Instances of OutputPlugins
     */
    public abstract OutputPlugin[] registerOutputPlugin();

    /**
     * use this method to register (if needed) your Output.
     * @return Array containing Instances of OutputExtensions
     */
    public abstract OutputExtension[] registerOutputExtension();

    /**
     * Internal initiation of addOn - fake constructor, comes before prepare
     * @param context the context to initialize with
     */
    @Override
    public void initAddOn(intellimate.izou.system.Context context) {
        this.context = new Context(context);
    }

    /**
     * returns the Context of the AddOn.
     *
     * Context provides some general Communications.
     *
     * @return an instance of Context.
     */
    @Override
    public Context getContext() {
        return context;
    }

    /**
     * gets the associated Plugin.
     * @return the Plugin.
     */
    @Override
    public PluginWrapper getPlugin() {
        return plugin;
    }

    /**
     * sets the Plugin IF it is not already set.
     * @param plugin the plugin
     */
    @Override
    public void setPlugin(PluginWrapper plugin) {
        this.plugin = plugin;
    }

    /**
     * An ID must always be unique.
     * A Class like Activator or OutputPlugin can just provide their .class.getCanonicalName()
     * If you have to implement this interface multiple times, just concatenate unique Strings to
     * .class.getCanonicalName()
     *
     * @return A String containing an ID
     */
    @Override
    public String getID() {
        return addOnID;
    }
}
