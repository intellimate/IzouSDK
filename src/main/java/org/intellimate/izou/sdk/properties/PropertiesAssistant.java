package org.intellimate.izou.sdk.properties;

import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.util.AddOnModule;
import org.intellimate.izou.system.file.FileSystemManager;
import org.intellimate.izou.system.file.ReloadableFile;

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Manages property files, and is also a {@link ReloadableFile}
 *
 * <p>Unlike most manager classes in Izou, the PropertiesManager is included in every {@code AddOn} instance</p>
 */
public class PropertiesAssistant extends AddOnModule implements ReloadableFile {
    private final Context context;
    private String propertiesPath;
    private String defaultPropertiesPath;
    private Properties properties;
    private final EventPropertiesAssistant assistant;

    public PropertiesAssistant(Context context, String addOnID) {
        super(context, addOnID + ".PropertiesAssistant");
        this.context = context;
        this.properties = new Properties();
        this.propertiesPath = null;
        this.defaultPropertiesPath = null;
        this.assistant = new EventPropertiesAssistant(context, addOnID + ".EventPropertiesAssistant");
        this.defaultPropertiesPath = FileSystemManager.FULL_WORKING_DIRECTORY + File.separator + "lib"
                 + getContext().getAddOn().getPlugin().getPluginPath() + File.separator + "classes" + File.separator
                + "default_properties.txt";
        initProperties();
    }

    /**
     * Gets the EventPropertiesAssistant
     *
     * @return the EventPropertiesAssistant
     */
    public EventPropertiesAssistant getEventPropertiesAssistant() {
        return assistant;
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
    public String getProperties(String key) {
        return properties.getProperty(key);
    }

    /**
     * Gets the properties object
     *
     * @return the properties object
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Calls the HashTable method put.
     *
     * Provided for parallelism with the getProperty method. Enforces use of strings for
     *     * property keys and values. The value returned is the result of the HashTable call to put.

     * @param key the key to be placed into this property list.
     * @param value the value corresponding to key.
     */
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    /**
     * Sets properties
     *
     * @param properties instance of properties, not null
     */
    public void setProperties(Properties properties) {
        if(properties == null) {
            return;
        }
        this.properties = properties;
    }


    /**
     * Gets the path to properties file (the real properties file - as opposed to the {@code defaultProperties.txt} file)
     *
     * @return path to properties file
     */
    public String getPropertiesPath() {
        return propertiesPath;
    }

    /**
     * Sets the path to properties file (the real properties file - as opposed to the {@code defaultProperties.txt} file)
     *
     * @param propertiesPath to properties file
     */
    public void setPropertiesPath(String propertiesPath) {
        this.propertiesPath = propertiesPath;
    }

    /**
     * Gets the path to default properties file path (the file which is copied into the real properties on start)
     *
     * @return path to default properties file
     */
    public String getDefaultPropertiesPath() {
        return defaultPropertiesPath;
    }

    /**
     * Initializes properties in the addOn. Creates new properties file using default properties.
     */
    public void initProperties() {
        String propertiesPathTemp;
        try {
            propertiesPathTemp = new File(".").getCanonicalPath() + File.separator + "properties" + File.separator
                    + getContext().getAddOn().getID() + ".properties";
        } catch (IOException e) {
            propertiesPathTemp = null;
            context.getLogger().error("Error while trying to build the propertiesPathTemp", e);
        }

        propertiesPath = propertiesPathTemp;
        File propertiesFile = new File(propertiesPath);
        if (!propertiesFile.exists()) try {
            propertiesFile.createNewFile();
        } catch (IOException e) {
            context.getLogger().error("Error while trying to create the new Properties file", e);
        }

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(propertiesFile),
                    "UTF8"));
            try {
                properties.load(in);
            } catch (IOException e) {
                context.getLogger().error("unable to load the InputStream for the PropertiesFile",e);
            }
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            context.getLogger().error("Error while trying to read Properties-File", e);
        }

        if (defaultPropertiesPath != null && new File(defaultPropertiesPath).exists()) {
            @SuppressWarnings("unchecked")
            Enumeration<String> keys = (Enumeration<String>)properties.propertyNames();

            if (!keys.hasMoreElements()) {
                try {
                    createDefaultPropertyFile(defaultPropertiesPath);
                } catch (IOException e) {
                    context.getLogger().error("Error while trying to copy the Default-Properties File", e);
                }

                if (new File(defaultPropertiesPath).exists() && !writeToPropertiesFile(defaultPropertiesPath)) return;
                reloadProperties();
            }
        }
    }

    /**
     * Writes defaultPropertiesFile.txt to real properties file
     * This is done so that the final user never has to worry about property file initialization
     *
     * @param defaultPropsPath path to defaultPropertyFile.txt (or where it should be created)
     * @return true if operation has succeeded, else false
     */
    private boolean writeToPropertiesFile(String defaultPropsPath) {
        return context.getFiles().writeToFile(defaultPropsPath, propertiesPath);
    }

    /**
     * Creates a defaultPropertyFile.txt in case it does not exist yet. In case it is used by an addOn,
     * it copies its content into the real properties file every time the addOn is launched.
     *
     * It is impossible to get the properties file on default, that way the user should not have to worry about
     * the property file's initial content.
     *
     * @param defaultPropsPath path to defaultPropertyFile.txt (or where it should be created)
     * @throws java.io.IOException is thrown by bufferedWriter
     */
    private void createDefaultPropertyFile(String defaultPropsPath) throws IOException {
        context.getFiles().createDefaultFile(defaultPropsPath, "# Properties should always be in the "
                + "form of: \"key = value\"");
    }

    /**
     * reloads the propertiesFile into the propertiesContainer
     */
    private void reloadProperties() {
        Properties temp = new Properties();
        BufferedReader bufferedReader = null;
        try {
            File properties = new File(propertiesPath);
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(properties), "UTF8"));
            temp.load(bufferedReader);
            this.properties = temp;
        } catch(IOException e) {
            context.getLogger().error("Error while trying to load the Properties-File: "
                    + propertiesPath, e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    context.getLogger().error("Unable to close input stream", e);
                }
            }
        }
    }

    @Override
    public void reloadFile(String eventType) {
        reloadProperties();
    }
}
