package org.intellimate.izou.sdk.server;

import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * represents an response.
 * <p>
 * this class is immutable!
 * </p>
 * @author LeanderK
 * @version 1.0
 */
public class Response implements org.intellimate.izou.server.Response {
    private final int status;
    private final Map<String, List<String>> headers;
    private final String contentType;
    private final long dataSize;
    private final InputStream stream;

    public Response() {
        this(-1, new HashMap<>(), null, -1, null);
    }

    public Response(int status, Map<String, List<String>> headers, String contentType, String data) {
        this(status, headers, contentType, data.getBytes(Charset.forName("UTF-8")));
    }

    public Response(int status, Map<String, List<String>> headers, String contentType, byte[] data) {
        this(status, headers, contentType, data.length, new ByteArrayInputStream(data));
    }

    public Response(int status, Map<String, List<String>> headers, String contentType, long dataSize, InputStream stream) {
        this.status = status;
        this.headers = headers;
        this.contentType = contentType;
        this.dataSize = dataSize;
        this.stream = stream;
    }

    @Override
    public int getStatus() {
        return status;
    }

    /**
     * sets the status
     * @param status the status to set
     * @return a new Response with the applied status
     */
    @SuppressWarnings("unused")
    public Response setStaus(int status) {
        return new Response(status, headers, contentType, dataSize, stream);
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    /**
     * sets the headers
     * @param headers the headers to set
     * @return a new Response with the applied headers
     */
    @SuppressWarnings("unused")
    public Response setHeaders(Map<String, List<String>> headers) {
        return new Response(status, headers, contentType, dataSize, stream);
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    /**
     * sets the content-type
     * @param contentType the content-type to set
     * @return a new Response with the applied content-type
     */
    @SuppressWarnings("unused")
    public Response setContentType(String contentType) {
        return new Response(status, headers, contentType, dataSize, stream);
    }

    @Override
    public long getDataSize() {
        return dataSize;
    }

    @Override
    public InputStream getData() {
        return stream;
    }

    /**
     * sets the data
     * @param data the data to set
     * @return a new Response with the applied data
     */
    public Response setData(byte[] data) {
        return new Response(status, headers, contentType, data.length, new ByteArrayInputStream(data));
    }

    /**
     * sets the data
     * @param dataSize the size of the data
     * @param stream the stream
     * @return a new Response with the applied data
     */
    @SuppressWarnings("unused")
    public Response setData(int dataSize, InputStream stream) {
        return new Response(status, headers, contentType, dataSize, stream);
    }

    /**
     * performs basic validity checking for the response
     * @return true if valid, false if not
     */
    @SuppressWarnings("WeakerAccess")
    public boolean isValidResponse() {
        return status != -1 && contentType != null && !contentType.isEmpty() && dataSize != -1 && stream != null;
    }
}
