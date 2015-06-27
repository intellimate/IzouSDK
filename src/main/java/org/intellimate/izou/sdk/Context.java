package org.intellimate.izou.sdk;

import org.intellimate.izou.addon.AddOnModel;
import org.intellimate.izou.identification.Identifiable;
import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.identification.IllegalIDException;
import org.intellimate.izou.sdk.contentgenerator.EventListener;
import org.intellimate.izou.sdk.properties.PropertiesAssistant;
import org.intellimate.izou.sdk.specification.ContentGeneratorModel;
import org.intellimate.izou.sdk.specification.context.ContentGenerators;
import org.intellimate.izou.system.context.*;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.intellimate.izou.system.context.System;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

/**
 * @author Leander Kurscheidt
 * @version 1.0
 */
public class Context implements org.intellimate.izou.system.Context {
    private final PropertiesAssistant propertiesAssistant;
    private final org.intellimate.izou.system.Context context;
    private final org.intellimate.izou.sdk.specification.context.ThreadPool threadPool;
    private final ContentGenerators contentGenerators;

    /**
     * Creates a context for the addOn.
     * @param context the context to use
     */
    public Context(org.intellimate.izou.system.Context context) {
        this.context = context;
        threadPool = new ThreadPoolImpl();
        contentGenerators = new ContentGeneratorsImpl();
        propertiesAssistant = new PropertiesAssistant(this, getAddOn().getID());
    }

    /**
     * Gets the API used for the Properties
     *
     * @return Properties
     */
    public PropertiesAssistant getPropertiesAssistant() {
        return propertiesAssistant;
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
    public org.intellimate.izou.sdk.specification.context.ThreadPool getThreadPool() {
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

    @Override
    public System getSystem() {
        return context.getSystem();
    }

    /**
     * returns the API used to manage the ContentGenerators
     * @return ContentGenerator
     */
    public ContentGenerators getContentGenerators() {
        return contentGenerators;
    }

    /**
     * gets addOn
     *
     * @return the addOn
     */
    @Override
    public AddOnModel getAddOn() {
        return context.getAddOn();
    }

    private class ContentGeneratorsImpl implements ContentGenerators {

        /**
         * Register an ContentGenerator
         *
         * @param contentGenerator the contentGenerator to register
         * @throws IllegalIDException not implemented yet
         */
        @Override
        public void registerContentGenerator(ContentGeneratorModel contentGenerator) throws IllegalIDException {
            List<? extends EventListener> triggeredEvents = contentGenerator.getTriggeredEvents();
            if (triggeredEvents != null) {
                triggeredEvents.stream()
                        .filter(Objects::nonNull)
                        .forEach(propertiesAssistant.getEventPropertiesAssistant()::registerEventListener);
            }
            getResources().registerResourceBuilder(contentGenerator);
        }

        /**
         * Unregisters an ContentGenerator
         *
         * @param contentGenerator the ContentGenerator to unregister
         */
        @Override
        public void unregisterContentGenerator(ContentGeneratorModel contentGenerator) {
            List<? extends EventListener> triggeredEvents = contentGenerator.getTriggeredEvents();
            for (EventListener eventListener : triggeredEvents) {
                propertiesAssistant.getEventPropertiesAssistant().unregisterEventID(eventListener.getDescriptorID());
            }
            getResources().unregisterResourceBuilder(contentGenerator);
        }
    }

    private class ThreadPoolImpl implements org.intellimate.izou.sdk.specification.context.ThreadPool {
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
         * @throws org.intellimate.izou.identification.IllegalIDException not implemented yet
         */
        @Override
        public ExecutorService getThreadPool(Identifiable identifiable) throws IllegalIDException {
            return context.getThreadPool().getThreadPool(identifiable);
        }

        /**
         * tries everything to log the exception
         *
         * @param throwable the Throwable
         * @param target    an instance of the thing which has thrown the Exception
         */
        @Override
        public void handleThrowable(Throwable throwable, Object target) {
            context.getThreadPool().handleThrowable(throwable, target);
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
