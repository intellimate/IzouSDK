package org.intellimate.izou.sdk.properties;

import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.contentgenerator.EventListener;
import org.intellimate.izou.sdk.events.CommonEvents;
import org.intellimate.izou.sdk.util.AddOnModule;
import org.intellimate.izou.system.file.ReloadableFile;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.Properties;
import java.util.function.Consumer;

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

    /**
     * Creates a new EventPropertiesAssistant
     * @param context the context to use
     * @param id the id of the addon
     */
    public EventPropertiesAssistant(Context context, String id) {
        super(context, id);
        properties = new Properties();
        try {
            createIzouPropertiesFiles();
            reloadFile(null);
        } catch (IOException e) {
            context.getLogger().error("Unable to initialize local_events.properties file", e);
        }

        try {
            getContext().getFiles().registerFileDir(getContext().getFiles().getPropertiesLocation().toPath(), getID(), this);
        } catch (IOException e) {
            error("Unable to register EventPropertiesAssistant with file manager " +
                    "(file reload service)", e);
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
        String propertiesPath = getContext().getFiles().getPropertiesLocation() + File.separator +
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
        BufferedWriter bufferedWriter;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(eventPropertiesPath, true);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(out));
            doWithLock(out.getChannel(), lock -> {
                unlockedReloadFile();
                if (getEventID(key) != null) {
                    return;
                }
                try {
                    bufferedWriter.write("\n\n" + key + "_DESCRIPTION = " + description + "\n" + key + " = " + value);
                    bufferedWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (FileNotFoundException e) {
            error("Unable find file", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                error("Unable to close lock", e);
            }
        }
    }

    /**
     * executes with a lock
     * @param channel the channel where the lock is acquired from
     * @param consumer the consumer to execute
     */
    private void doWithLock(FileChannel channel, Consumer<FileLock> consumer) {
        FileLock lock = null;
        try {
            while (lock == null) {
                try {
                    lock = channel.tryLock();
                } catch (OverlappingFileLockException e) {
                    Thread.sleep(500);
                }
            }
            consumer.accept(lock);
        } catch (IOException | InterruptedException e) {
            error("Unable to write", e);
        } finally {
            try {
                if (lock != null) {
                    lock.release();
                }
            } catch (IOException e) {
                error("Unable to close lock", e);
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

        FileOutputStream out = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            out = new FileOutputStream(eventPropertiesPath, true);

            final File tempFile = new File(eventPropertiesPath + "temp.properties");
            final BufferedReader readerFinal = new BufferedReader(new FileReader(eventPropertiesPath));
            final BufferedWriter writerFinal = new BufferedWriter(new FileWriter(tempFile));

            doWithLock(out.getChannel(), lock -> {
                unlockedReloadFile();
                if (getEventID(eventKey) != null) {
                    return;
                }

                try {
                    String currentLine = readerFinal.readLine();
                    while(currentLine != null) {
                        String trimmedLine = currentLine.trim();
                        if(trimmedLine.equals(eventKey + "_DESCRIPTION") || trimmedLine.equals(eventKey)) continue;
                        writerFinal.write(currentLine + System.getProperty("line.separator"));
                        currentLine = readerFinal.readLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            reader = readerFinal;
            writer = writerFinal;
            tempFile.renameTo(new File(eventPropertiesPath));
        } catch (IOException e) {
            error("Unable find file", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (writer != null) {
                    writer.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                error("Unable to close lock", e);
            }
        }
    }

    private void unlockedReloadFile() {
        Properties tmpProperties = new Properties();
        BufferedReader in = null;
        try {
            File eventFile = new File(eventPropertiesPath);
            FileInputStream fileInputStream = new FileInputStream(eventFile);
            in = new BufferedReader(new InputStreamReader(fileInputStream, "UTF8"));
            tmpProperties.load(in);
            this.properties = tmpProperties;
        } catch(IOException e) {
            error("Error while trying to load local_events.properties", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    error("Unable to close input stream", e);
                }
            }
        }
    }

    @Override
    public void reloadFile(String eventType) {
        try (FileOutputStream outputStream = new FileOutputStream(eventPropertiesPath, true)) {
            doWithLock(outputStream.getChannel(), lock ->  unlockedReloadFile());
        } catch (IOException e) {
            error("Unable to reload local_events.properties file", e);
        }
    }
}
