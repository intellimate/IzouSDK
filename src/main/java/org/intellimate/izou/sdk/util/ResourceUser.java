package org.intellimate.izou.sdk.util;

import org.intellimate.izou.identification.Identifiable;
import org.intellimate.izou.identification.Identification;
import org.intellimate.izou.identification.IdentificationManager;
import org.intellimate.izou.identification.IllegalIDException;
import org.intellimate.izou.resource.ResourceModel;
import org.intellimate.izou.sdk.resource.Resource;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * provides various utility-methods to receive Resources
 * @author Leander Kurscheidt
 * @version 1.0
 */
public interface ResourceUser extends ContextProvider, Identifiable {
    /**
     * generates the specified resource from the first matching ResourceBuilder (use the ID if you want to be sure)
     * <p>
     * tip: for a better coding experience: use the method
     * <code>.orElse(CompletableFuture.completedFuture(new LinkedList<>()))</code>
     * to handle the empty case
     * </p>
     * @param resourceID the id of the resource
     * @return an Optional containing a future of a list of results
     */
    default Optional<CompletableFuture<List<ResourceModel>>> generateResource(String resourceID) {
        return generateResource(resourceID, null);
    }

    /**
     * generates the specified resource from the specified ResourceBuilder.
     * <p>
     * tip: for a better coding experience: use the method
     * <code>.orElse(CompletableFuture.completedFuture(new LinkedList<>()))</code>
     * to handle the empty case
     * </p>
     * @param resourceID the id of the resource
     * @param provider the specified provider
     * @return an Optional containing a future of a list of results
     */
    default Optional<CompletableFuture<List<ResourceModel>>> generateResource(String resourceID, Identification provider) {
        return IdentificationManager.getInstance()
                .getIdentification(this)
                .map(id -> new Resource(resourceID, provider, id))
                .flatMap(resource -> {
                    try {
                        return getContext().getResources().generateResource(resource);
                    } catch (IllegalIDException e) {
                        getContext().getLogger().error("unable to generate Resource", e);
                        return Optional.empty();
                    }
                });
    }
}
