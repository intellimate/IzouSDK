package org.intellimate.izou.sdk.properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.contentgenerator.EventListener;
import org.intellimate.izou.sdk.events.CommonEvents;
import org.intellimate.izou.sdk.util.AddOnModule;
import org.intellimate.izou.system.file.ReloadableFile;

import java.io.*;
import java.util.Properties;

/**
 * EventPropertiesManager manages all events written in the local_events.properties file. You can register (add)
 * events to the file, and get events from the file. The file pretty much serves as a hub for event IDs.
 */
public class EventPropertiesAssistant extends AddOnModule implements ReloadableFile {

    /**
     * The path to the local_events.properties file
     */
    private final String eventPropertiesPath = getContext().getFiles().getPropertiesLocation() + File.separator +
            "local_events.properties";
    private Properties properties;
    private final Logger fileLogger = LogManager.getLogger(this.getClass());

    /**
     * Creates a new EventPropertiesManager
     * @param context the context to use
     * @param id the id of the addon
     */
    public EventPropertiesAssistant(Context context, String id) {
        super(context, id + ".EventPropertiesAssistant");
        properties = new Properties();
        try {
            createIzouPropertiesFiles();
            reloadFile(null);
        } catch (IOException e) {
            context.getLogger().error("Unable to initialize local_events.properties file", e);
        }
        registerStandardEvents();
    }

    /**
     * registers the standard-events
     */
    private void registerStandardEvents() {
        CommonEvents.Descriptors.stopListener(this).ifPresent(this::registerEventListener);
        CommonEvents.Presence.generalLeavingListener(this).ifPresent(this::registerEventListener);
        CommonEvents.Presence.generalListener(this).ifPresent(this::registerEventListener);
        CommonEvents.Presence.leavingListener(this).ifPresent(this::registerEventListener);
        CommonEvents.Presence.presenceListener(this).ifPresent(this::registerEventListener);
        CommonEvents.Presence.strictLeavingListener(this).ifPresent(this::registerEventListener);
        CommonEvents.Presence.strictListener(this).ifPresent(this::registerEventListener);
        CommonEvents.Response.fullResponseListener(this).ifPresent(this::registerEventListener);
        CommonEvents.Response.majorResponseListener(this).ifPresent(this::registerEventListener);
        CommonEvents.Response.minorResponseListener(this).ifPresent(this::registerEventListener);
        CommonEvents.Type.notificationListener(this).ifPresent(this::registerEventListener);
        CommonEvents.Type.responseListener(this).ifPresent(this::registerEventListener);
    }

    private void createIzouPropertiesFiles() throws IOException {
        String propertiesPath = getContext().getFiles().getPropertiesLocation() + File.separator + "properties" + File.separator +
                "local_events.properties";

        File file = new File(propertiesPath);
        BufferedWriter bufferedWriterInit = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
                bufferedWriterInit = new BufferedWriter(new FileWriter(propertiesPath));
                bufferedWriterInit.write("# You can use this file to store an event ID with a key, or shortcut, " +
                        " so that others can easily access and\n# fire it using the key");
            }
        } catch (IOException e) {
            //error("unable to create the local_events file", e);
        } finally {
            if(bufferedWriterInit != null)
                bufferedWriterInit.close();
        }
    }

    /**
     * gets the String containing the Properties-Path
     * @return a String
     */
    public String getEventPropertiesPath() {
        return eventPropertiesPath;
    }

    /**
     * Gets the full event ID associated with the key {@code key}
     *
     * @param key the key of the full event ID
     * @return the complete the event ID, or null if none is found
     */
    public String getEventID(String key) {
        return (String) properties.get(key);
    }

    /**
     * Registers or adds an event to the local_events.properties file with the informations found in the EventListener
     *
     * @param eventListener the eventListener to add
     */
    public void registerEventListener(EventListener eventListener) {
        registerEventID(eventListener.getDescription(),
                eventListener.getDescriptorID(), eventListener.getDescriptor());
    }

    /**
     * Registers or adds an event to the local_events.properties file
     *
     * @param description a simple description of the Event
     * @param key the key with which to store the event ID
     * @param value the complete event ID
     */
    public void registerEventID(String description, String key, String value) {
        if (getEventID(key) != null) {
            //results in spamming the log
            //fileLogger.debug("Did not add " + key + " event ID to local_events.properties because it already exists");
            return;
        }

        BufferedWriter bufferedWriterInit = null;
        try {
            bufferedWriterInit = new BufferedWriter(new FileWriter(eventPropertiesPath, true));
        } catch (IOException e) {
            fileLogger.error("Unable to create buffered writer", e);
        }
        try {
            if (bufferedWriterInit != null) {
                bufferedWriterInit.write("\n\n" + key + "_DESCRIPTION = " + description + "\n" + key + " = " + value);
            }
        } catch (IOException e) {
            fileLogger.error("Unable to write to local_events.properties file", e);
        } finally {
            try {
                if (bufferedWriterInit != null) {
                    bufferedWriterInit.close();
                }
            } catch (IOException e) {
                fileLogger.error("Unable to close buffered writer", e);
            }
        }
    }

    /**
     * Unregisters or deletes an event from the local_events.properties file
     *
     * @param eventKey the key under which the complete event ID is stored in the properties file
     */
    public void unregisterEventID(String eventKey) {
        properties.remove(eventKey + "_DESCRIPTION");
        properties.remove(eventKey);

        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(eventPropertiesPath, true));
        } catch (IOException e) {
            fileLogger.error("Unable to create buffered writer", e);
        }

        try {
            if (bufferedWriter != null) {
                properties.store(bufferedWriter, null);
            }
        } catch (IOException e) {
            fileLogger.error("Unable to delete the event from the properties file", e);
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                fileLogger.error("Unable to close buffered writer", e);
            }
        }

        reloadFile(null);
    }


    @Override
    public void reloadFile(String eventType) {
        Properties temp = new Properties();
        BufferedReader in = null;
        try {
            File properties = new File(eventPropertiesPath);
            in = new BufferedReader(new InputStreamReader(new FileInputStream(properties), "UTF8"));
            temp.load(in);
            this.properties = temp;
        } catch(IOException e) {
            fileLogger.error("Error while trying to load local_events.properties", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    fileLogger.error("Unable to close input stream", e);
                }
            }
        }
    }
}
