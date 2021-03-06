package org.intellimate.izou.sdk.util;

import org.intellimate.izou.identification.*;
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
//TODO: Leander the return type optional is really unnecessary, i don't think the information resulting from it is
// useful. We should implement the tip and return CompletableFuture and log if there was an error obtaining the ID.
// The question is whether to archive this without breaking backwards compatibility (might turn really ugly and i like
// the current method name)
public interface ResourceUser extends ContextProvider, Identifiable {
    /**
     * generates the specified resource from the first matching ResourceBuilder (use the ID if you want to be sure).
     * tip: for a better coding experience: use the method
     * <pre>{@code
     * .orElse(CompletableFuture.completedFuture(new LinkedList<>()))
     * }</pre>
     * to handle the empty case.
     * @param resourceID the id of the resource
     * @return an Optional containing a future of a list of results
     */
    default Optional<CompletableFuture<List<ResourceModel>>> generateResource(String resourceID) {
        return generateResource(resourceID, null);
    }

    /**
     * generates the specified resource from the specified ResourceBuilder.
     * tip: for a better coding experience: use the method
     * <pre>{@code
     * .orElse(CompletableFuture.completedFuture(new LinkedList<>()))
     * }</pre>
     * to handle the empty case.
     * @param resourceID the id of the resource
     * @param provider the specified provider
     * @return an Optional containing a future of a list of results
     */
    default Optional<CompletableFuture<List<ResourceModel>>> generateResource(String resourceID, Identification provider) {
        return IdentificationManagerM.getInstance()
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
