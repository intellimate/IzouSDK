package intellimate.izou.sdk.specification.context;

import intellimate.izou.sdk.properties.PropertiesAssistant;

import java.util.Properties;

/**
 * Specification for properties assistant
 */
public interface IzouProperties {

    String getProperties(String key);

    /**
     * Returns an Instance of Properties, if found
     *
     * @return an Instance of Properties or null;
     */
    Properties getProperties();

    /**
     * Gets the {@code propertiesAssistant}
     *
     * @return the {@code propertiesAssistant}
     */
    PropertiesAssistant getPropertiesAssistant();

    /**
     * Calls the HashTable method put.
     *
     * Provided for parallelism with the getProperty method. Enforces use of strings for
     *     * property keys and values. The value returned is the result of the HashTable call to put.

     * @param key the key to be placed into this property list.
     * @param value the value corresponding to key.
     */
    void setProperties(String key, String value);

    /**
     * Sets a new properties object
     *
     * @param properties the properties object to set
     */
    void setProperties(Properties properties);

    /**
     * Gets the path to properties file (the real properties file - as opposed to the {@code defaultProperties.txt} file)
     *
     * @return path to properties file
     */
    String getPropertiesPath();

    /**
     * Sets the path to properties file (the real properties file - as opposed to the {@code defaultProperties.txt} file)
     *
     * @param propertiesPath to properties file
     */
    void setPropertiesPath(String propertiesPath);

    /**
     * Gets the path to default properties file (the file which is copied into the real properties on start)
     *
     * @return path to default properties file
     */
    String getDefaultPropertiesPath();
}
