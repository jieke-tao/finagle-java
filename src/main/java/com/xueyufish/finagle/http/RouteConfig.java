package com.xueyufish.finagle.http;

import com.xueyufish.finagle.router.Router;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.*;

public class RouteConfig {

    private Resource resource;

    private List<Router> routers = new ArrayList<>();

    private Logger logger = LoggerFactory.getLogger(RouteConfig.class);

    public List<Router> initRouter() throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        List<String> lines = new ArrayList<>();
        String tempString;
        while ((tempString = br.readLine()) != null) {
            lines.add(tempString);
        }
        br.close();

        lines.stream().filter(line -> (StringUtils.isNotBlank(line) && !StringUtils.trim(line).startsWith("#"))).forEach(line -> {
            logger.debug("read route line : {}", line);
            List<Router> router = createRouters(line);
            routers.addAll(router);
        });

        return routers;
    }

    private List<Router> createRouters(String confLine) {
        List<Router> routerList = new ArrayList<>();
        String[] confs = confLine.split("\\s+");
        String[] routes = Arrays.asList(confs).stream().filter(StringUtils::isNotBlank).toArray(String[]::new);
        String path = null;
        String controller = null;
        String method = null;
        if (routes.length == 2) {
            path = routes[0].trim();
            controller = routes[1].trim();
        } else if (routes.length == 3) {
            method = routes[0].trim();
            path = routes[1].trim();
            controller = routes[2].trim();
        }
        String action = null;
        if (controller.indexOf(":") > 0) {
            String[] controllerAndAction = controller.split(":");
            controller = controllerAndAction[0].trim();
            action = controllerAndAction[1].trim();
        }
        if (path.indexOf("{action}") > 0) {
            try {
                Class<?> clazz = Class.forName(controller);
                Set<String> excludedMethodName = this.buildExcludedMethodName();
                for (Method m : clazz.getMethods()) {
                    if (!excludedMethodName.contains(m.getName())) {
                        String p = path.replace("{action}", m.getName());
                        String a = m.getName();
                        routerList.add(new Router(p, controller, a, method));
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            routerList.add(new Router(path, controller, action, method));
        }

        return routerList;
    }

    private Set<String> buildExcludedMethodName() {
        Set<String> excludedMethodName = new HashSet<String>();
        Method[] methods2 = Object.class.getMethods();
        for (Method m : methods2) {
            excludedMethodName.add(m.getName());
        }
        return excludedMethodName;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
