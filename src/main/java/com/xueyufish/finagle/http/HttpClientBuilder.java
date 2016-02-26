package com.xueyufish.finagle.http;

import com.xueyufish.finagle.util.LogUtil;
import com.twitter.finagle.Http;
import com.twitter.finagle.Service;
import com.twitter.util.Await;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class HttpClientBuilder implements InitializingBean, FactoryBean<HttpClient>, DisposableBean {

    private LogUtil logger = LogUtil.getLogger(HttpClientBuilder.class);
    private String hosts;
    private int hostConnectionLimit = 1;
    private int retries = 3;
    private int connectTimeout = 5;
    private int connectionTimeout = 5;

    private String rootPath = "/";

    private Service<HttpRequest, HttpResponse> service;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(hosts);
        service = Http.newService(hosts);
    }

    @Override
    public void destroy() throws Exception {
        logger.logDebug("Close Finagle");
        Await.ready(service.close());
    }

    @Override
    public HttpClient getObject() throws Exception {
        return new HttpClient(service, rootPath);
    }

    @Override
    public Class<?> getObjectType() {
        return HttpClient.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public int getHostConnectionLimit() {
        return hostConnectionLimit;
    }

    public void setHostConnectionLimit(int hostConnectionLimit) {
        this.hostConnectionLimit = hostConnectionLimit;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }
}
