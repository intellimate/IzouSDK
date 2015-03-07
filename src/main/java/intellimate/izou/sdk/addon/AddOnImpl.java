package intellimate.izou.sdk.addon;

import intellimate.izou.activator.Activator;
import intellimate.izou.addon.AddOn;
import intellimate.izou.events.EventsController;
import intellimate.izou.output.OutputExtension;
import intellimate.izou.output.OutputPlugin;
import intellimate.izouSDK.Context;
import intellimate.izouSDK.contentgenerator.ContentGenerator;
import ro.fortsoft.pf4j.PluginWrapper;

/**
 * All AddOns must extend this Class.
 *
 * It will be instantiated and its registering-methods will be called by the PluginManager.
 * This class has method for a properties-file named addOnID.properties (AddOnsID in the form: package.class)
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class AddOnImpl implements AddOn {
    @SuppressWarnings("FieldCanBeLocal")
    private final String addOnID;
    private Context context;
    private PluginWrapper plugin;

    /**
     * the default constructor for AddOns
     * @param addOnID the ID of the Plugin in the form: package.class
     */
    public AddOnImpl(String addOnID) {
        this.addOnID = addOnID;
    }

    /**
     * use this method to register your Modules
     */
    @Override
    public void register() {
        //TODO: register
    }

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
     * this method gets called when the task submitted to the ThreadPool crashes
     *
     * @deprecated since version 0.9.9.9 - use {@link intellimate.izou.system.context.ContextImplementation.Properties#getPropertiesContainer}
     * @param e the exception catched
     */
    @Override
    public void exceptionThrown(Exception e) {
        context.logger.getLogger().fatal("Addon: " + getID() + " crashed", e);
    }

    /**
     * Overwrite this method if you would like to change the location of the <i>defaultProperties.txt</i> file.
     * <p>
     *  All you have to do if you would like to change the location of the <i>defaultProperties.txt</i> file, is have
     *  this method return the desired path.
     * </p>
     * <p>
     *  If nothing should be changed, you can leave this method be; it will return null to indicate that the default
     *  location should be used, in which case your <i>defaultProperties.txt</i> file HAS to be in the resource folder
     *  of your project.
     * </p>
     * @return the new path to the <i>defaultProperties.txt</i> location, or null if nothing should be changed
     * (in which case you should ignore this method)
     */
    public String setUnusualDefaultPropertiesPath() {
        return null;
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
