package org.intellimate.izou.sdk.server;

import org.intellimate.izou.identification.AddOnInformation;
import org.intellimate.izou.sdk.Context;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * @author LeanderK
 * @version 1.0
 */
public class Request implements org.intellimate.izou.server.Request {
    private final org.intellimate.izou.server.Request request;
    private final Matcher matcher;
    private final String shortenedUrl;
    private final AddOnInformation source;
    private final String token;
    private final Map<String, List<String>> queryParams;

    public Request(org.intellimate.izou.server.Request request, Matcher matcher, Context context) {
        this(request, matcher, null, context);
    }

    private Request(org.intellimate.izou.server.Request request, Matcher matcher, String shortenedUrl, Context context) {
        this.request = request;
        this.matcher = matcher;
        if (shortenedUrl == null) {
            this.shortenedUrl = shortenUrl(request.getUrl());
        } else {
            this.shortenedUrl = shortenedUrl;
        }
        List<String> sourceList = request.getParams().get("app");
        if (sourceList == null || sourceList.isEmpty()) {
            source = null;
        } else {
            String source = sourceList.get(0);
            int id = -1;
            try {
                id = Integer.parseInt(source);
            } catch (NumberFormatException e) {}
            if (id != -1) {
                this.source = context.getAddOns().getAddOnInformation(id)
                        .orElse(null);
            } else {
                this.source = context.getAddOns().getAddOnInformation(source)
                        .orElse(null);
            }
        }
        List<String> tokenList = request.getParams().get("token");
        if (tokenList != null && !tokenList.isEmpty()) {
            this.token = tokenList.get(0);
        } else {
            this.token = null;
        }

        queryParams = getQueryMap(request.getUrl());
    }

    public Request(org.intellimate.izou.server.Request request, Matcher matcher, String shortenedUrl,
                   AddOnInformation source, String token, Map<String, List<String>> queryParams) {
        this.request = request;
        this.matcher = matcher;
        this.shortenedUrl = shortenedUrl;
        this.source = source;
        this.token = token;
        this.queryParams = queryParams;
    }

    private String shortenUrl(String fullUrl) {
        String withoutQuery = fullUrl;
        int index = fullUrl.indexOf("?");
        if (index != -1) {
            withoutQuery = fullUrl.substring(0, index);
        }
        String withOutTrailingSlash;
        if (withoutQuery.endsWith("/")) {
            withOutTrailingSlash = withoutQuery.substring(0, fullUrl.length());
        } else {
            withOutTrailingSlash = withoutQuery;
        }
        String shortString;
        if (withOutTrailingSlash.startsWith("/apps/dev")) {
            shortString = withOutTrailingSlash.replaceFirst("/apps/dev/\\w+", "");
        } else {
            shortString = withOutTrailingSlash.replaceFirst("/apps/\\d+", "");
        }
        if (shortString.isEmpty()) {
            shortString = "/";
        }
        return shortString;
    }

    private Map<String, List<String>> getQueryMap(String url) {
        int index = url.indexOf("?");
        if (index != -1 && index != (url.length() -1)) {
            String queryParams = url.substring(index + 1, url.length());
            String[] split = queryParams.split("&");

            return Arrays.stream(split)
                    .map(argument -> argument.split("="))
                    .filter(arguments -> arguments.length <= 2)
                    .filter(arguments -> arguments.length >= 1)
                    .collect(Collectors.toMap(
                            (String[] arguments) -> {
                                try {
                                    return URLDecoder.decode(arguments[0], "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    throw new InternalServerErrorException("Unable to decode query parameter: "+arguments[0], e);
                                }
                            },
                            (String[] arguments) -> {
                                List<String> arrayList = new ArrayList<>();
                                if (arguments.length == 1) {
                                    return arrayList;
                                } else {
                                    try {
                                        arrayList.add(URLDecoder.decode(arguments[1], "UTF-8"));
                                    } catch (UnsupportedEncodingException e) {
                                        throw new InternalServerErrorException("Unable to decode query parameter: "+arguments[1], e);
                                    }
                                    return arrayList;
                                }
                            },
                            (arrayList, arrayList2) -> {
                                arrayList.addAll(arrayList2);
                                return arrayList;
                            }
                    ));

        } else {
            return new HashMap<>();
        }
    }

    @Override
    public String getUrl() {
        return request.getUrl();
    }

    @Override
    public Map<String, List<String>> getParams() {
        return request.getParams();
    }

    @Override
    public String getMethod() {
        return request.getMethod();
    }

    @Override
    public String getContentType() {
        return request.getContentType();
    }

    @Override
    public int getContentLength() {
        return request.getContentLength();
    }

    @Override
    public InputStream getData() {
        return request.getData();
    }

    /**
     * returns the source of the request, if it came from an app
     * @return the information about the Addon
     */
    public Optional<AddOnInformation> getSource() {
        return Optional.ofNullable(source);
    }

    /**
     * returns the Access token of the app, only present if the app is calling itself!
     * @return the token if the request comes from the app itself
     */
    public Optional<String> getToken() {
        return Optional.ofNullable(token);
    }

    /**
     * returns the named group from the regex
     * @param name the name of the group
     * @return the resulting capture, or null
     */
    public String getGroup(String name) {
        return matcher.group(name);
    }

    /**
     * this string does not contain {@code /apps/id} or {@code /apps/dev/name} and also no slash at the end, also no query params
     * @return the shortened url
     */
    public String getShortUrl() {
        return shortenedUrl;
    }

    /**
     * sets the matcher for the request
     * @param matcher the matcher
     * @return the request
     */
    public Request setMatcher(Matcher matcher) {
        return new Request(request, matcher, shortenedUrl, source, token, queryParams);
    }
}
