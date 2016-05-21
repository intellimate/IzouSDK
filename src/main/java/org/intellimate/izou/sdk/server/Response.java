package org.intellimate.izou.sdk.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LeanderK
 * @version 1.0
 */
public class Response implements org.intellimate.izou.server.Response {
    private final int status;
    private final Map<String, List<String>> headers;
    private final String contentType;
    private final byte[] data;

    public Response() {
        this(-1, new HashMap<>(), null, null);
    }

    public Response(int status, Map<String, List<String>> headers, String contentType, byte[] data) {
        this.status = status;
        this.headers = headers;
        this.contentType = contentType;
        this.data = data;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public byte[] getData() {
        return data;
    }

    public boolean isValidResponse() {
        return status != -1 && contentType != null && !contentType.isEmpty() && data != null && data.length != 0;
    }
}
