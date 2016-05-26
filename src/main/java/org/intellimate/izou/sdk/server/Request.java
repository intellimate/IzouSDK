package org.intellimate.izou.sdk.server;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * @author LeanderK
 * @version 1.0
 */
public class Request implements org.intellimate.izou.server.Request {
    private final org.intellimate.izou.server.Request request;
    private final Matcher matcher;
    private final String shortenedUrl;

    public Request(org.intellimate.izou.server.Request request, Matcher matcher) {
        this.request = request;
        this.matcher = matcher;
        shortenedUrl = shortenUrl(request.getUrl());
    }

    public Request(org.intellimate.izou.server.Request request, Matcher matcher, String shortenedUrl) {
        this.request = request;
        this.matcher = matcher;
        this.shortenedUrl = shortenedUrl;
    }

    private String shortenUrl(String fullUrl) {
        String withOutTrailingSlash;
        if (fullUrl.endsWith("/")) {
            withOutTrailingSlash = fullUrl.substring(0, fullUrl.length());
        } else {
            withOutTrailingSlash = fullUrl;
        }
        String shortString;
        if (withOutTrailingSlash.startsWith("apps/dev")) {
            shortString = withOutTrailingSlash.replaceFirst("apps/dev/\\w+", "");
        } else {
            shortString = withOutTrailingSlash.replaceFirst("apps/\\d+", "");
        }
        return shortString;
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
     * returns the named group from the regex
     * @param name the name of the group
     * @return the resulting capture, or null
     */
    public String getGroup(String name) {
        return matcher.group(name);
    }

    /**
     * this string does not contain {@code /apps/id} or {@code /apps/dev/name} and also no slash at the end
     * @return the shortened url
     */
    public String getShortUrl() {
        return shortenedUrl;
    }

    public Request setMatcher(Matcher matcher) {
        return new Request(request, matcher, shortenedUrl);
    }
}
