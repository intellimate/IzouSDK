package org.intellimate.izou.sdk.server.properties;

import com.google.common.io.ByteStreams;
import org.intellimate.izou.identification.AddOnInformation;
import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.server.*;

import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * the default router used for serving the default-properties file.
 * @author LeanderK
 * @version 1.0
 */
public class PropertiesRouter extends Router {
    private Lock writeLock = new ReentrantLock();
    private final Consumer<File> validatorFunction;
    @SuppressWarnings("WeakerAccess")
    protected AddOnInformation addOnInformation;


    /**
     * creates a new Router
     *
     * @param context          the context to use
     * @param validatorFunction can be provided to check the syntax of the temporary submitted file,
     *                          should return empty if valid, or an exception if not
     *                          (the server will display the detail if it is an BadRequest).
     *
     */
    @SuppressWarnings("WeakerAccess")
    public PropertiesRouter(Context context, Consumer<File> validatorFunction) {
        super(context);
        this.validatorFunction = validatorFunction;

        addOnInformation = context.getAddOns().getAddOnInformation("org.intellimate.izou.sdk")
                .orElse(null);

        init();
    }

    /**
     * creates a new Router
     *
     * @param context          the context to use
     */
    public PropertiesRouter(Context context) {
        this(context, file -> Optional.empty());
    }

    @SuppressWarnings("WeakerAccess")
    protected void init() {
        routeInternal("/properties", route -> {
            route.get(request -> {
                File propertiesFile = getContext().getPropertiesAssistant().getPropertiesFile();
                return sendFile(request, propertiesFile)
                        .setContentType("text/plain");
            });

            route.patch(this::patchConfigFile);
        });

        routeInternal("/", route -> {
            route.get(this::redirect);
        });

        routeInternal("/index.html", route -> {
            route.get(this::redirect);
        });
    }

    /**
     * creates the redirect to the
     * @param request the request to handle
     * @return the redirect
     */
    @SuppressWarnings("WeakerAccess")
    protected Response redirect(Request request) {
        if (addOnInformation == null) {
            throw new InternalServerErrorException("Unable to get AddOnInformation for SDK");
        }
        String redirect = constructLinkToAddon(addOnInformation, "/");
        redirect = redirect + "?token=" + request.getToken()
                .orElseThrow(() -> new BadRequestException("unable to extract authentication token"));
        return createRedirect(redirect);
    }

    /**
     * the logic for the patch-operation on the config-file
     * @param request the request to work on
     * @return a response
     */
    @SuppressWarnings("WeakerAccess")
    protected Response patchConfigFile(Request request) {
        if (!request.getContentType().equals("text/plain")) {
            throw new BadRequestException("Content type must be: text/plain");
        }
        boolean locked = writeLock.tryLock();
        if (!locked) {
            throw new BadRequestException("Server is already processing another request");
        }
        FileOutputStream fileOutputStream = null;
        File tempFile = null;
        try {
            File propertiesDir = getContext().getPropertiesAssistant().getPropertiesFile().getParentFile();
            tempFile = new File(propertiesDir, "default.properties.tmp");
            if (!tempFile.exists()) {
                tempFile.createNewFile();
            }
            fileOutputStream = new FileOutputStream(tempFile);
            long copied = ByteStreams.copy(request.getData(), fileOutputStream);
            if (copied != request.getContentLength()) {
                throw new BadRequestException("Actual size does not match advertised size");
            }
            validatorFunction.accept(tempFile);
            File propertiesFile = getContext().getPropertiesAssistant().getPropertiesFile();
            Files.copy(tempFile.toPath(), propertiesFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return sendFile(request, propertiesFile)
                    .setContentType("text/plain");
        } catch (FileNotFoundException e) {
            throw new InternalServerErrorException("unable to create temp-file", e);
        } catch (IOException e) {
            throw new InternalServerErrorException("an internal server-error occured while saving the properties file", e);
        } finally {
            writeLock.unlock();
            if (tempFile != null) {
                tempFile.delete();
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    getContext().getLogger().error("unable to close OutputStream", e);
                }
            }
        }
    }
}
