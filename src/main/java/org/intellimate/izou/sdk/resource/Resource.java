package org.intellimate.izou.sdk.resource;

import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.resource.ResourceModel;

import java.util.ArrayList;
import java.util.List;

/**
 * An Resource is an object which is used to pass data from one part of the application to another.
 * Note! This Object is immutable!
 */
public class Resource<T> implements ResourceModel<T> {
    private final String resourceID;
    private final Identification provider;
    private final Identification consumer;
    private final T resource;

    /**
     * creates a new Resource.
     * @param resourceID the ID of the Resource
     */
    public Resource(String resourceID) {
        this(resourceID, null, null, null);
    }

    /**
     * creates a new Resource.
     * @param resourceID the ID of the Resource
     * @param provider the Provider of the Resource
     */
    public Resource(String resourceID, Identification provider) {
        this(resourceID, provider, null, null);
    }

    /**
     * creates a new Resource.
     * @param resourceID the ID of the Resource
     * @param provider the Provider of the Resource
     * @param t the resource
     */
    public Resource(String resourceID, Identification provider, T t) {
        this(resourceID, provider, t, null);
    }

    /**
     * creates a new Resource.
     * @param resourceID the ID of the Resource
     * @param consumer the ID of the Consumer
     * @param t the resource
     */
    public Resource(String resourceID, T t, Identification consumer) {
        this(resourceID, null, t, consumer);
    }


    /**
     * creates a new Resource.
     * @param resourceID the ID of the Resource
     * @param provider the Provider of the Resource
     * @param consumer the ID of the Consumer
     */
    public Resource(String resourceID, Identification provider, Identification consumer) {
        this(resourceID, provider, null, consumer);
    }

    /**
     * creates a new Resource.
     * @param resourceID the ID of the Resource
     * @param provider the Provider of the Resource
     * @param t the resource
     * @param consumer the ID of the Consumer
     */
    public Resource(String resourceID, Identification provider, T t, Identification consumer) {
        this.resourceID = resourceID;
        this.provider = provider;
        this.resource = t;
        this.consumer = consumer;
    }

    /**
     * returns the associated Resource data if set.
     * This method is thread-safe.
     * @return null or resource data
     */
    @Override
    public T getResource() {
        return resource;
    }

    /**
     * sets the Resource data.
     * <p>
     * Note! this Object is immutable!
     * </p>
     * @param resource the data to set
     * @return the Resource
     */
    public Resource<T> setResource(T resource) {
        return new Resource<>(resourceID, provider, resource, consumer);
    }

    /**
     * returns the ID of the Resource.
     * This method is thread-safe.
     * @return a String containing the ID of the resource
     */
    @Override
    public String getResourceID() {
        return resourceID;
    }

    /**
     * returns the provider of the Resource.
     * This method is thread-safe.
     * @return an Identification describing the provider of the Resource or null if not set
     */
    @Override
    public Identification getProvider() {
        return provider;
    }

    /**
     * returns whether a provider is set
     * @return true if this resource has an provider, false if not
     */
    @Override
    public boolean hasProvider() {
        return provider != null;
    }

    /**
     * sets who should or has provided the Resource Object.
     * <p>
     * Note! this Object is immutable!
     * </p>
     * @param provider an Identification describing the provider of the Resource
     * @return the Resource
     */
    public Resource<T> setProvider(Identification provider) {
        return new Resource<>(resourceID, provider, resource, consumer);
    }

    /**
     * returns the consumer of the object (if set).
     * @return null or an Identification describing the consumer of the Resource
     */
    @Override
    public Identification getConsumer() {
        return consumer;
    }

    /**
     * sets who should or has consumed the Resource Object.
     * <p>
     * Note! this Object is immutable!
     * </p>
     * @param consumer an Identification describing the consumer of the Resource
     * @return new Resource
     */
    public Resource<T> setConsumer(Identification consumer) {
        return new Resource<>(resourceID, provider, resource, consumer);
    }

    /**
     * creates a list with this Element in it.
     * @return a list
     */
    public List<Resource> toList() {
        List<Resource> resourceList = new ArrayList<>();
        resourceList.add(this);
        return resourceList;
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
        return resourceID;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "resourceID='" + resourceID + '\'' +
                ", provider=" + provider +
                ", consumer=" + consumer +
                ", resource=" + resource +
                '}';
    }
}
