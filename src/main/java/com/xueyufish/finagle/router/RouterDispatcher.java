package com.xueyufish.finagle.router;

import com.xueyufish.finagle.util.LogUtil;
import com.twitter.util.*;
import org.jboss.netty.handler.codec.http.*;
import org.jboss.netty.handler.codec.http.multipart.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class RouterDispatcher {

    final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);
    private final LogUtil logger = LogUtil.getLogger(RouterDispatcher.class);
    private final String rootPath;

    private ActionInvoker actionInvoker = new ActionInvoker();
    final ExecutorServiceFuturePool futurePool = FuturePools.newFuturePool(Executors.newFixedThreadPool(50));

    public RouterDispatcher(String rootPath) {
        if (rootPath == null) {
            rootPath = "/";
        }
        this.rootPath = rootPath;
    }

    public Future<HttpResponse> dispatch(RouteMapping routeMapping, HttpRequest request) {
        final HttpResponse response = new DefaultHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

        return futurePool.apply(new Function0<HttpResponse>() {
            @Override
            public HttpResponse apply() {
                QueryStringDecoder query = new QueryStringDecoder(
                        request.getUri());
                HttpMethod httpMethod = request.getMethod();
                String path = query.getPath();
                long t1 = System.currentTimeMillis();
                Map<String, String> pathPara = new HashMap<>();
                Action action = findAction(path, routeMapping, pathPara);
                // check action
                if (!checkAction(action, request, response, path)) {
                    return response;
                } else {
                    Map<String, List<String>> params = scanAllRequestParam(request, query, httpMethod);
                    if (pathPara.size() > 0) {
                        pathPara.keySet().stream().forEach((k) -> {
                            params.put(k, Arrays.asList(pathPara.get(k)));
                        });
                    }
                    actionInvoker.invoke(action, params, response);
                }
                logger.logDebug("invoke cast time [{}]", (System.currentTimeMillis() - t1));
                return response;
            }
        });

    }

    private Action findAction(String path, RouteMapping routeMapping, Map<String, String> pathPara) {
        Action action = null;
        if (!rootPath.equals("/") && path.length() > 0) {
            String basePath;
            if (!rootPath.startsWith("/")) {
                basePath = "/" + rootPath;
            } else {
                basePath = rootPath;
            }
            if (path.startsWith(basePath)) {
                path = path.substring(basePath.length());
                action = routeMapping.getAction(path, pathPara);
            } else {
                action = routeMapping.getAction(path, pathPara);
            }
        } else {
            action = routeMapping.getAction(path, pathPara);
        }
        return action;
    }

    private Map<String, List<String>> scanAllRequestParam(HttpRequest request, QueryStringDecoder query, HttpMethod httpMethod) {
        Map<String, List<String>> params = new HashMap<>();
        final Map<String, List<String>> queryParameters = query.getParameters();
        queryParameters.entrySet().stream().forEach((e) -> {
            params.put(e.getKey(), e.getValue());
        });

        if (httpMethod.equals(HttpMethod.GET)) {

        } else if (httpMethod.equals(HttpMethod.POST)) {
            try {
                HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(factory, request);
                postDecoder.getBodyHttpDatas().stream().forEach(data -> {
                    if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                        Attribute a = (Attribute) data;
                        try {
                            params.put(a.getName(), Arrays.asList(a.getValue()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (HttpPostRequestDecoder.ErrorDataDecoderException e) {
                e.printStackTrace();
            } catch (HttpPostRequestDecoder.NotEnoughDataDecoderException e) {
                e.printStackTrace();
            }
        }
        return params;
    }

    private boolean checkAction(Action action, HttpRequest request, HttpResponse response, String path) {
        if ((action == null)) {
            response.setStatus(HttpResponseStatus.NOT_FOUND);
            return false;
        } else if (action.getHttpMethod() != null && !request
                .getMethod().getName()
                .equals(action.getHttpMethod())) {
            logger.logInfo(
                    "Request method [{}] not match defined in router {}.",
                    request.getMethod().getName(), action);
            response.setStatus(HttpResponseStatus.NOT_FOUND);
            return false;
        }
        return true;
    }

}
