package intellimate.izou.sdk;

import intellimate.izou.addon.AddOn;
import intellimate.izou.events.Event;
import intellimate.izou.identification.Identifiable;
import intellimate.izou.identification.Identification;
import intellimate.izou.identification.IllegalIDException;
import intellimate.izou.sdk.properties.PropertiesAssistant;
import intellimate.izou.sdk.specification.context.ContentGenerator;
import intellimate.izou.sdk.specification.context.ThreadPool;
import intellimate.izou.system.context.*;
import org.apache.logging.log4j.spi.ExtendedLogger;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

/**
 * @author Leander Kurscheidt
 * @version 1.0
 */
public class Context implements intellimate.izou.system.Context {
    private intellimate.izou.system.Context context;
    private final PropertiesImpl properties;
    private final ThreadPool threadPool;
    private final ContentGenerator contentGenerator;

    /**
     * Creates a context for the addOn. It is where
     */
    public Context(intellimate.izou.system.Context context) {
        this.context = context;
        properties = new PropertiesImpl(new PropertiesAssistant(this, getAddOn().getID()));
        threadPool = new ThreadPoolImpl();
        contentGenerator = new ContentGeneratorImpl();
    }

    /**
     * Gets the API used for the Properties
     *
     * @return Properties
     */
    public intellimate.izou.sdk.specification.context.IzouProperties getProperties() {
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
        return threadPool;
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
     * returns the API used to manage the ContentGenerators
     * @return ContentGenerator
     */
    public intellimate.izou.sdk.specification.context.ContentGenerator getContentGenerator() {
        return contentGenerator;
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

    /**
     * Adds the event ID of {@code value} to the PopularEvents.properties file with a key of {@code key}
     *
     * @param description a short description of what the event ID is for, should not be null
     * @param key the key with which to store the event ID, should not be null
     * @param value the complete event ID, should not be null
     */
    //TODO: move
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
    //TODO: move
    @Override
    public String getEventsID(String key) {
        return main.getEventPropertiesManager().getEventID(key);
    }

    private class ContentGeneratorImpl implements intellimate.izou.sdk.specification.context.ContentGenerator {
        /**
         * register an ContentGenerator
         * @param contentGenerator the contentGenerator to register
         * @throws IllegalIDException not implemented yet
         */
        @Override
        public void registerContentGenerator(intellimate.izou.sdk.specification.ContentGenerator contentGenerator)
                                                                                            throws IllegalIDException {
            List<? extends Event<?>> commonEvents = contentGenerator.announceEvents();
            for (Event event : commonEvents) {

            }
            //TODO: automatically register common events here! See contentgenerator.EventListener
            getResources().registerResourceBuilder(contentGenerator);
        }

        /**
         * Unregisters an ContentGenerator
         *
         * @param contentGenerator the ContentGenerator to unregister
         */
        @Override
        public void unregisterContentGenerator(intellimate.izou.sdk.specification.ContentGenerator contentGenerator) {
            getResources().unregisterResourceBuilder(contentGenerator);
        }
    }

    private static class PropertiesImpl implements intellimate.izou.sdk.specification.context.IzouProperties {
        private PropertiesAssistant propertiesAssistant;

        /**
         * Creates a new properties object within the context
         */
        public PropertiesImpl(PropertiesAssistant propertiesAssistant) {
            this.propertiesAssistant = propertiesAssistant;
        }

        /**
         * You should probably use getProperties() unless you have a very good reason not to.
         *
         * Searches for the property with the specified key in this property list.
         *
         * If the key is not found in this property list, the default property list, and its defaults, recursively, are
         * then checked. The method returns null if the property is not found.
         *
         * @param key the property key.
         * @return the value in this property list with the specified key value.
         */
        @Override
        public String getProperties(String key) {
            return propertiesAssistant.getProperties().getProperty(key);
        }

        /**
         * Returns an instance of Properties, if found
         *
         * @return an instance of Properties or null;
         */
        @Override
        public Properties getProperties() {
            return propertiesAssistant.getProperties();
        }

        /**
         * Gets the {@code propertiesAssistant}
         *
         * @return the {@code propertiesAssistant}
         */
        @Override
        public PropertiesAssistant getPropertiesAssistant() {
            return propertiesAssistant;
        }

        /**
         * Calls the HashTable method put.
         *
         * Provided for parallelism with the getProperty method. Enforces use of strings for
         *     * property keys and values. The value returned is the result of the HashTable call to put.

         * @param key the key to be placed into this property list.
         * @param value the value corresponding to key.
         */
        @Override
        public void setProperties(String key, String value) {
            this.propertiesAssistant.setProperties(key, value);
        }

        /**
         * Sets properties
         *
         * @param properties instance of properties, not null
         */
        @Override
        public void setProperties(Properties properties) {
            this.propertiesAssistant.setProperties(properties);
        }

        /**
         * Gets the path to properties file (the real properties file - as opposed to the {@code defaultProperties.txt} file)
         *
         * @return path to properties file
         */
        @Override
        public String getPropertiesPath() {
            return propertiesAssistant.getPropertiesPath();
        }

        /**
         * Sets the path to properties file (the real properties file - as opposed to the {@code defaultProperties.txt} file)
         *
         * @param propertiesPath to properties file
         */
        @Override
        public void setPropertiesPath(String propertiesPath) {
            this.propertiesAssistant.setPropertiesPath(propertiesPath);
        }

        /**
         * Gets the path to default properties file (the file which is copied into the real properties on start)
         *
         * @return path to default properties file
         */
        @Override
        public String getDefaultPropertiesPath() {
            return propertiesAssistant.getDefaultPropertiesPath();
        }
    }

    private class ThreadPoolImpl implements ThreadPool {
        private ExecutorService executorService = null;

        /**
         * returns a ThreadPool associated with the AddOn
         *
         * @return an instance of ExecutorService or Null if there is a problem with the Identifier
         */
        @Override
        public ExecutorService getThreadPool() {
            if (executorService != null) {
                return executorService;
            } else {
                try {
                    executorService = getThreadPool(getAddOn());
                } catch (IllegalIDException e) {
                    getLogger().error("Unable to obtain ExecutorService", e);
                    return null;
                }
                return executorService;
            }
        }

        /**
         * returns a NEW ThreadPool where all the IzouPlugins are running
         *
         * @param identifiable the Identifiable to set each created Task as the Source
         * @return an instance of ExecutorService
         * @throws intellimate.izou.identification.IllegalIDException not implemented yet
         */
        @Override
        public ExecutorService getThreadPool(Identifiable identifiable) throws IllegalIDException {
            return context.getThreadPool().getThreadPool(identifiable);
        }

        /**
         * returns the ID of the Manager
         */
        @Override
        public Identification getManagerIdentification() {
            return context.getThreadPool().getManagerIdentification();
        }
    }
}
