package intellimate.izou.sdk.util;

import intellimate.izou.identification.Identifiable;
import intellimate.izou.identification.Identification;
import intellimate.izou.identification.IdentificationManager;
import intellimate.izou.sdk.resource.Resource;

import java.util.Optional;

/**
 * provides various utility-methods to create Resources
 * @author Leander Kurscheidt
 * @version 1.0
 */
public interface ResourceCreator extends ContextProvider, Identifiable {
    /**
     * creates a new Resource with the specified ID (=Name)
     * @param id the id
     * @param <T> the data-type of the resource
     * @return an Optional which can contain a Resource
     */
    default <T> Optional<Resource<T>> createResource(String id) {
        return createResource(id, null, null);
    }

    /**
     * creates a new Resource with the specified ID (=Name)
     * @param id the id
     * @param consumer the consumer of the Resource
     * @param <T> the data-type of the resource
     * @return an Optional which can contain a Resource
     */
    default <T> Optional<Resource<T>> createResource(String id, Identification consumer) {
        return createResource(id, null, consumer);
    }

    /**
     * creates a new Resource with the specified ID (=Name)
     * @param id the id
     * @param t the data
     * @param <T> the data-type of the resource
     * @return an Optional which can contain a Resource
     */
    default <T> Optional<Resource<T>> createResource(String id, T t) {
        return createResource(id, t, null);
    }

    /**
     * creates a new Resource with the specified ID (=Name)
     * @param id the id
     * @param t the data
     * @param consumer the consumer of the Resource
     * @param <T> the data-type of the resource
     * @return an Optional which can contain a Resource
     */
    default <T> Optional<Resource<T>> createResource(String id, T t, Identification consumer) {
        if (id == null || id.isEmpty()) {
            getContext().getLogger().error("resource id is null or empty");
            return Optional.empty();
        }
        Optional<Resource<T>> generated = IdentificationManager.getInstance().getIdentification(this)
                .map(idProvider -> new Resource<>(id, idProvider, t, consumer));
        if (!generated.isPresent())
            getContext().getLogger().error("unable to generate Resource");
        return generated;
    }
}
