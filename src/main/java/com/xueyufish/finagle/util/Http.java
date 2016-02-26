package com.xueyufish.finagle.util;

import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

public class Http {

    private final ClientConfig clientConfig = new ClientConfig();
    private final Client client;
    private final WebTarget webTarget;
    private final Map<String, String> params = new HashMap<>();
    private final Map<String, Object> headers = new HashMap<>();
    private MediaType mediaType;

    private Http(String url, Class<?>... filters) {
        if (filters != null) {
            for (Class<?> f : filters) {
                clientConfig.register(f);
            }
        }
        client = ClientBuilder.newClient(clientConfig);
        webTarget = client.target(url);
    }

    public static Http target(String url, Class<?>... filters) {
        Http http = new Http(url, filters);
        return http;
    }

    public Http filter(Class<?>... filters) {
        if (filters != null) {
            for (Class<?> f : filters) {
                client.register(f);
            }
        }
        return this;
    }

    public Http path(String path) {
        this.webTarget.path(path);
        return this;
    }

    public Http param(String param, Object o) {
        this.params.put(param, o == null ? "" : o.toString());
        return this;
    }

    public Http header(String k, Object v) {
        this.headers.put(k, v);
        return this;
    }

    public Http request(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    public <T> T get(Class<T> clazz) {
        return doGet().get(clazz);
    }

    public <T> T post(Class<T> clazz) {
        return post(clazz, MediaType.APPLICATION_FORM_URLENCODED_TYPE);
    }

    public <T> T post(Class<T> clazz, MediaType mediaType) {
        return doPost(mediaType).invoke(clazz);
    }

    private Invocation doPost(MediaType mediaType) {
        Form form = new Form();
        params.keySet().stream().forEach((p) -> {
            form.param(p, params.get(p));
        });
        Invocation.Builder builder = this.webTarget.request(this.mediaType);
        headers.keySet().stream().forEach((h) -> {
            builder.header(h, headers.get(h));
        });
        return builder.buildPost(Entity.entity(form, mediaType));
    }

    private Invocation.Builder doGet() {
        params.keySet().stream().forEach((p) -> {
            this.webTarget.queryParam(p, params.get(p));
        });
        Invocation.Builder builder = this.webTarget.request(mediaType);
        headers.keySet().stream().forEach((h) -> {
            builder.header(h, headers.get(h));
        });
        return builder;
    }



}
