package intellimate.izou.sdk;

import intellimate.izou.addon.AddOn;
import intellimate.izou.system.context.*;
import org.apache.logging.log4j.spi.ExtendedLogger;

/**
 * @author Leander Kurscheidt
 * @version 1.0
 */
public class Context implements intellimate.izou.system.Context {
    private intellimate.izou.system.Context context;
    private final Properties properties;
    /**
     */
    public Context(intellimate.izou.system.Context context) {
        this.context = context;
        properties = new Properties(new PropertiesAssistant(this, getAddOn().getID()));
    }

    /**
     * returns the API used fo the Properties
     * @return Properties
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * returns the API used for interaction with Events
     *
     * @return Events
     */
    @Override
    public Events getEvents() {
        return context.getEvents();
    }

    /**
     * returns the API used for interaction with Resource
     *
     * @return Resource
     */
    @Override
    public Resources getResources() {
        return context.getResources();
    }

    /**
     * returns the API used for interaction with Files
     *
     * @return Files
     */
    @Override
    public Files getFiles() {
        return context.getFiles();
    }

    /**
     * returns the API used to log
     * @return Logger
     */
    @Override
    public ExtendedLogger getLogger() {
        return context.getLogger();
    }

    /**
     * returns the API used to manage the ThreadPool
     *
     * @return ThreadPool
     */
    @Override
    public ThreadPool getThreadPool() {
        return context.getThreadPool();
    }

    /**
     * returns the API to manage the Activators
     *
     * @return Activator
     */
    @Override
    public Activators getActivators() {
        return context.getActivators();
    }

    /**
     * returns the API used to manage the OutputPlugins and OutputExtensions
     *
     * @return Output
     */
    @Override
    public Output getOutput() {
        return context.getOutput();
    }

    /**
     * gets addOn
     *
     * @return the addOn
     */
    @Override
    public AddOn getAddOn() {
        return context.getAddOn();
    }

    //TODO: add this to Events
    /**
     * Adds the event ID of {@code value} to the PopularEvents.properties file with a key of {@code key}
     *
     * @param description a short description of what the event ID is for, should not be null
     * @param key the key with which to store the event ID, should not be null
     * @param value the complete event ID, should not be null
     */
    @Override
    public void addEventIDToPropertiesFile(String description, String key, String value) {
        main.getEventPropertiesManager().registerEventID(description, key, value);
    }

    /**
     * Gets the full event ID associated with the key {@code key}
     *
     * @param key the key of the full event ID
     * @return the complete the event ID, or null if none is found
     */
    @Override
    public String getEventsID(String key) {
        return main.getEventPropertiesManager().getEventID(key);
    }



    public class Properties {
        private PropertiesAssistant propertiesManager;

        /**
         * Creates a new properties object within the context
         *
         */
        public Properties(PropertiesAssistant propertiesAssistant) {
            this.propertiesManager = propertiesAssistant;
        }

        /**
         * You should probably use getPropertiesContainer() unless you have a very good reason not to.
         *
         * Searches for the property with the specified key in this property list.
         *
         * If the key is not found in this property list, the default property list, and its defaults, recursively, are
         * then checked. The method returns null if the property is not found.
         *
         * @param key the property key.
         * @return the value in this property list with the specified key value.
         */
        public String getProperties(String key) {
            return propertiesManager.getPropertiesContainer().getProperties().getProperty(key);
        }

        /**
         * Returns an Instance of Properties, if found
         *
         * @return an Instance of Properties or null;
         */
        public PropertiesContainer getPropertiesContainer() {
            return propertiesManager.getPropertiesContainer();
        }

        /**
         * Gets the {@code propertiesManger}
         *
         * @return the {@code propertiesManger}
         */
        public PropertiesAssistant getPropertiesManger() {
            return propertiesManager;
        }

        /**
         * Calls the HashTable method put.
         *
         * Provided for parallelism with the getProperty method. Enforces use of strings for
         *     * property keys and values. The value returned is the result of the HashTable call to put.

         * @param key the key to be placed into this property list.
         * @param value the value corresponding to key.
         */
        public void setProperties(String key, String value) {
            this.propertiesManager.getPropertiesContainer().getProperties().setProperty(key, value);
        }

        /**
         * Sets properties
         *
         * @param properties instance of properties, not null
         */
        public void setProperties(java.util.Properties properties) {
            if(properties == null) return;
            this.propertiesManager.setProperties(properties);
        }

        /**
         * Sets properties-container
         *
         * @param propertiesContainer the properties-container
         */
        public void setPropertiesContainer(PropertiesContainer propertiesContainer) {
            if(propertiesContainer == null) return;
            this.propertiesManager.setPropertiesContainer(propertiesContainer);
        }

        /**
         * Gets the path to properties file (the real properties file - as opposed to the {@code defaultProperties.txt} file)
         *
         * @return path to properties file
         */
        public String getPropertiesPath() {
            return propertiesManager.getPropertiesPath();
        }

        /**
         * Sets the path to properties file (the real properties file - as opposed to the {@code defaultProperties.txt} file)
         *
         * @param propertiesPath to properties file
         */
        public void setPropertiesPath(String propertiesPath) {
            this.propertiesManager.setPropertiesPath(propertiesPath);
        }

        /**
         * Gets the path to default properties file (the file which is copied into the real properties on start)
         *
         * @return path to default properties file
         */
        public String getDefaultPropertiesPath() {
            return propertiesManager.getDefaultPropertiesPath();
        }
    }
}
