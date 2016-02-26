package com.xueyufish.finagle.router;

import com.xueyufish.finagle.util.LogUtil;
import com.xueyufish.finagle.util.Classes;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RouteMapping {

    private final LogUtil logger = LogUtil.getLogger(RouteMapping.class);
    private static final String SLASH = "/";
    private List<Router> routes;

    public RouteMapping(List<Router> routes) {
        this.routes = routes;
    }

    private ActionChain root = ActionChain.create("/");

    public void buildActionChain() {

        for (Router router : this.routes) {

            Object object = router.getController();
            Class<?> controllerClass = router.getControllerClass();
            String path = router.getPath().trim();
            String[] paths = path.split(SLASH);
            ActionChain ac = root;
            for (int i = 0, len = paths.length; i < len; i++) {
                String p = paths[i];
                if (p != null && !p.trim().equals("")) {
                    if (ac.get(p) != null) {
                        ac = ac.get(p);
                    } else {
                        ActionChain newAc = ActionChain.create(p);
                        if (p.startsWith(":")) {
                            newAc.setPathParam(true);
                            newAc.setParam(p.substring(1));
                        }
                        ac.put(newAc);
                        ac = newAc;
                    }
                }
            }
            if (ac.getAction() != null) {
                logger.logError("the path:[{}]==>{} has already mapped to {}", path, router.getAction(), ac.getAction());
            } else {
                Method method = getMethodByName(controllerClass, router.getAction());
                if (method == null) {
                    logger.logError("controller==> {}, method [{}] not found. ", controllerClass, router.getAction());
                } else {
                    Action action = new Action(path, router.getControllerClass(), router.getMethod(), method, router.getAction(), getParameters(controllerClass, method), object);
                    ac.setAction(action);
                    logger.logInfo("mapping path [{}] to {}:{}",
                            path, controllerClass.getName(),
                            router.getAction());
                }
            }
        }
    }

    private Method getMethodByName(Class<?> clazz, String method) {
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            if (m.getName().equals(method)) {
                return m;
            }
        }
        return null;
    }

    private List<RequestParam> getParameters(Class<?> controllerClass,
                                             Method method) {
        List<RequestParam> params = new ArrayList<RequestParam>();
        String[] methodNames = Classes.getMethodParamNames(method);
        Class[] types = method.getParameterTypes();
        for (int i = 0; i < types.length; i++) {
            RequestParam param = new RequestParam();
            param.setName(methodNames[i]);
            param.setType(types[i]);
            params.add(param);
        }
        return params;
    }

    /**
     * Support four types of url 1: http://abc.com/controllerKey ---> 00 2:
     * http://abc.com/controllerKey/para ---> 01 3:
     * http://abc.com/controllerKey/method ---> 10 4:
     * http://abc.com/controllerKey/method/para ---> 11 The controllerKey can
     * also contains "/" Example: http://abc.com/uvw/xyz/method/para
     */
    public Action getAction(String url, Map<String, String> urlPara) {
        String[] urls = url.split(SLASH);
        List<String> paths = Stream.of(urls).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        ActionChain ac = root;
        Action action = null;
        for (int i = 0, len = paths.size(); i < len; i++) {
            String path = paths.get(i);
            ActionChain _ac = ac.get(path);
            if (_ac != null) {
                boolean hasAction = _ac.getAction() != null;
                boolean hasChild = _ac.hasChildren();
                if (_ac.isPathParam()) {
                    if (!hasAction && hasChild)
                        urlPara.put(_ac.getParam(), path);
                    else if (hasAction) {
                        urlPara.put(_ac.getParam(), url.substring(url.lastIndexOf(path)));
                    }
                }
            }else {
                break;
            }
            ac = _ac;
        }
        if (ac != null) {
            action = ac.getAction();
            logger.logDebug("Mapped {} with {}", url, action);
        } else {
            logger.logDebug("Mapped {} not found", url);
        }
        return action;
    }

}
