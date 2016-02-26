package com.xueyufish.finagle.http;

import com.xueyufish.finagle.util.LogUtil;
import com.xueyufish.finagle.router.RouteMapping;
import com.xueyufish.finagle.router.Router;
import com.xueyufish.finagle.router.RouterDispatcher;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import com.twitter.finagle.ListeningServer;
import com.twitter.finagle.Service;
import com.twitter.finagle.builder.ServerBuilder;
import com.twitter.finagle.builder.ServerConfig;
import com.twitter.finagle.http.Http;
import com.twitter.util.Future;

import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

public class HttpServerBuilder implements InitializingBean,
        ApplicationContextAware, DisposableBean {

    private final LogUtil logger = LogUtil
            .getLogger(HttpServerBuilder.class);

    private String serverName = "fish-server";
    private String host;
    private int port;
    private String rootPath = "/";

    private ServerBuilder<HttpRequest, HttpResponse, ServerConfig.Yes, ServerConfig.Yes, ServerConfig.Yes> serverBuilder;
    private ListeningServer server;
    private RouteConfig routeConfig;
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.initHttpService();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void initHttpService() throws IOException {
        Assert.notNull(this.host);
        this.serverBuilder = ServerBuilder.get().codec(Http.get())
                .name(serverName)
                .bindTo(new InetSocketAddress(this.host, this.port));

        this.logger.logInfo("Starting HTTP Server ...");

        List<Router> routers = this.routeConfig.initRouter();
        for (Router router : routers) {
            Object bean = this.applicationContext.getBean(router
                    .getControllerClass());
            router.setController(bean);
        }

        final RouteMapping routeMapping = new RouteMapping(routers);
        routeMapping.buildActionChain();

        final RouterDispatcher dispatcher = new RouterDispatcher(rootPath);
        Service<HttpRequest, HttpResponse> service = new Service<HttpRequest, HttpResponse>() {
            @SuppressWarnings("unchecked")
            @Override
            public Future<HttpResponse> apply(HttpRequest request) {
                return dispatcher.dispatch(routeMapping, request);
            }
        };

        this.server = ServerBuilder.safeBuild(service, this.serverBuilder);
        this.logger.logInfo("HTTP Server started with {}:{} [{}]", this.host,
                this.port, this.rootPath);
    }


    @Override
    public void destroy() throws Exception {
        this.server.close();
    }

    public String getServerName() {
        return this.serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public RouteConfig getRouteConfig() {
        return this.routeConfig;
    }

    public void setRouteConfig(RouteConfig routeConfig) {
        this.routeConfig = routeConfig;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }
}
