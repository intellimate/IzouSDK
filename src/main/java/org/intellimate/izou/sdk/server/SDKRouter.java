package org.intellimate.izou.sdk.server;

import freemarker.template.*;
import org.intellimate.izou.identification.AddOnInformation;
import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.server.properties.PropertiesRouter;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * the router active in the sdk
 * @author LeanderK
 * @version 1.0
 */
public class SDKRouter extends Router {
    private final Configuration cfg;

    /**
     * creates a new Router
     *
     * @param context          the context to use
     */
    public SDKRouter(Context context) {
        super(context);

        cfg = new Configuration(new Version(2,3,24));
        cfg.setClassForTemplateLoading(SDKRouter.class, "server/freemarker");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setLocale(Locale.US);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        route("/template/properties", route -> {
            route.get(this::getHtmlBaseFile);
        });

        route("/", route -> {
            route.get(this::getResponse);
        });

        route("/index.html", route -> {
            route.get(this::getResponse);
        });
    }

    private Response getResponse(Request request) {
        URL resource = getClass().getClassLoader().getResource("server/static/greetings.html");
        try {
            File file = new File(resource.toURI());
            return sendFile(request, file);
        } catch (URISyntaxException e) {
            throw new InternalServerErrorException("Unable to open file", e);
        }
    }

    private Response getHtmlBaseFile(Request request) {
        AddOnInformation addOnInformation = request.getSource()
                .orElseThrow(() -> new BadRequestException("this route is only intended for apps"));

        List<String> tokenList = request.getParams().get("token");
        if (tokenList == null || tokenList.isEmpty()) {
            throw new BadRequestException("query parameter token is missing");
        }
        String token = tokenList.get(0);
        if (token == null || token.isEmpty()) {
            throw new BadRequestException("query parameter token is missing");
        }

        String urlProperties = constructLinkToAddon(addOnInformation, "/properties");
        String urlDescription = constructLinkToAddon(addOnInformation, "/description");

        Map<String, Object> input = new HashMap<String, Object>();
        input.put("app_name", addOnInformation.getArtifactID());
        input.put("url_properties", urlProperties);
        input.put("url_description", urlDescription);
        input.put("token", token);

        Template template;
        try {
            template = cfg.getTemplate("properties.ftl");
        } catch (IOException e) {
            throw new InternalServerErrorException("unable to load template", e);
        }

        StringWriter stringWriter = new StringWriter();
        try {
            template.process(input, stringWriter);
        } catch (TemplateException | IOException e) {
            throw new InternalServerErrorException("an error occured while templating", e);
        }
        String html = stringWriter.toString();
        return new Response(200, new HashMap<>(), "text/html", html);
    }
}
