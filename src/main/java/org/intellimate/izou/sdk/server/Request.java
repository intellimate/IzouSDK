package org.intellimate.izou.sdk.server;

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

    public Request(org.intellimate.izou.server.Request request, Matcher matcher) {
        this.request = request;
        this.matcher = matcher;
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
    public byte[] getData() {
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
}
