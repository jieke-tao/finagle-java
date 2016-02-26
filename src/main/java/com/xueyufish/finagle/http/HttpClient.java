package com.xueyufish.finagle.http;

import com.alibaba.fastjson.JSON;
import com.xueyufish.finagle.util.ClassesUtil;
import com.xueyufish.finagle.util.LogUtil;
import com.twitter.finagle.Service;
import com.twitter.util.Future;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.handler.codec.http.*;
import org.jboss.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpDataFactory;
import org.jboss.netty.util.CharsetUtil;
import org.springframework.beans.factory.DisposableBean;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HttpClient implements DisposableBean {

    private final LogUtil logger = LogUtil.getLogger(HttpClient.class);

    private final Service<HttpRequest, HttpResponse> service;

    private String rootPath;

    final HttpDataFactory factory = new DefaultHttpDataFactory(true);

    public HttpClient(Service<HttpRequest, HttpResponse> service, String rootPath) {
        if (rootPath == null) {
            this.rootPath = "/";
        } else if (!rootPath.startsWith("/")) {
            this.rootPath = "/" + rootPath;
        } else {
            this.rootPath = rootPath;
        }
        this.service = service;
    }

    private String wrapperPath(String url) {
        String path = null;
        if (rootPath.endsWith("/") && url.startsWith("/")) {
            path = rootPath + url.substring(1);
        } else if (!rootPath.endsWith("/") && !url.startsWith("/")) {
            path = rootPath + "/" + url;
        } else {
            path = rootPath + url;
        }
        return path;
    }

    public Future<HttpResponse> getFuture(String url) {
        HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1,
                HttpMethod.GET, wrapperPath(url));
        return service.apply(request);
    }

    public String get(String url) {
        logger.logDebug("Call service Http GET {} ", url);
        Future<HttpResponse> res = getFuture(url);
        HttpResponse response = res.get();
        if (response.getStatus().equals(HttpResponseStatus.OK)) {
            return res.get().getContent().toString(CharsetUtil.UTF_8);
        } else {
            throw new RuntimeException();
        }
    }

    public String get(String url, Map<String, Object> data) {
        if (url.indexOf("?") > 0) {
            url = url + "&" + toQueryString(data);
        } else {
            url = url + "?" + toQueryString(data);
        }
        return get(url);
    }

    public <T> T get(String url, Map<String, Object> data, Class<T> clazz) {
        if (url.indexOf("?") > 0) {
            url = url + "&" + toQueryString(data);
        } else {
            url = url + "?" + toQueryString(data);
        }
        return get(url, clazz);
    }

    public <T> T get(String url, Class<T> clazz) {
        String content = get(url);
        if (content != null)
            try {
                return JSON.parseObject(content, clazz);
            } catch (Exception e) {

                return null;
            }
        else
            return null;
    }

    public <T> List<T> getList(String url, Map<String, Object> data, Class<T> clazz) {
        if (url.indexOf("?") > 0) {
            url = url + "&" + toQueryString(data);
        } else {
            url = url + "?" + toQueryString(data);
        }
        return getList(url, clazz);
    }

    public <T> List<T> getList(String url, Class<T> clazz) {
        String content = get(url);
        if (content != null)
            try {
                return JSON.parseArray(content, clazz);
            } catch (Exception e) {

                return null;
            }
        else
            return null;
    }

    public HttpResponse postToFuture(String url, Map<String, Object> params) {
        HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1,
                HttpMethod.POST, wrapperPath(url));
        request.headers().add(HttpHeaders.Names.CONTENT_TYPE,
                "application/x-www-form-urlencoded");

        try {
            if (params.size() > 0) {
                HttpPostRequestEncoder postRequestEncoder = new HttpPostRequestEncoder(
                        factory, request, false);

                for (String k : params.keySet()) {
                    String v = Optional.ofNullable(
                            params.get(k))
                            .orElse("").toString();
                    logger.logDebug("[key={} , value={}]", k, v);
                    postRequestEncoder.addBodyAttribute(k, v);
                }
                request = postRequestEncoder.finalizeRequest();
            }
        } catch (HttpPostRequestEncoder.ErrorDataEncoderException e) {
            logger.logError("HttpPostRequestEncoder Error:", e);
        }


        HttpResponse res = service.apply(request).get();
        if (!res.getStatus().equals(HttpResponseStatus.OK)) {
            throw new RuntimeException(); // TODO 需要处理异常
        }
        return res;
    }

    public String post(String url, Map<String, Object> params) {
        HttpResponse res = postToFuture(url, params);
        String content = res.getContent().toString(CharsetUtil.UTF_8);
        return content;
    }

    public <T> T post(String url, Map<String, Object> params, Class<T> clazz) {
        String content = post(url, params);
        if(StringUtils.isBlank(content)){
        	return null;
        }
        return JSON.parseObject(content, clazz);
    }

    private String toQueryString(Map<String, Object> data) {
        List<String> params = new ArrayList<>();
        if (data != null && data.size() > 0) {
            data.keySet().stream().forEach(k -> {
                Object v = data.get(k);
                String vv;
                if (v == null) {
                    vv = "";
                } else if (ClassesUtil.isPrimitive(v.getClass()) || v.getClass() == String.class) {
                    vv = v.toString();
                } else {
                    vv = JSON.toJSONString(v);
                }
                try {
                    params.add(URLEncoder.encode(k, "UTF-8") + "=" + URLEncoder.encode(vv, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });
            return String.join("&", params);
        }
        return "";
    }

    @Override
    public void destroy() throws Exception {
        service.close();
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }
}
