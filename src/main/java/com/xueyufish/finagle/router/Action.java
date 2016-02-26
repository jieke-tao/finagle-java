package com.xueyufish.finagle.router;


import java.lang.reflect.Method;
import java.util.List;

public class Action {

    private final Class<?> controllerClass;
    private final String path;
    private final Method method;
    private final String methodName;
    private final Object controller;
    private final List<RequestParam> params;
    private final String httpMethod;

    public Action(String path, Class<?> controllerClass, String httpMethod, Method method, String methodName, List<RequestParam> params, Object controller) {
        this.path = path;
        this.controllerClass = controllerClass;
        this.method = method;
        this.methodName = methodName;
        this.params = params;
        this.controller = controller;
        this.httpMethod = httpMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public String getPath() {
        return path;
    }

    public Method getMethod() {
        return method;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object getController() {
        return controller;
    }

    public List<RequestParam> getParams() {
        return params;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    @Override
    public String toString() {
        return "Action{" +
                "controllerClass=" + controllerClass +
                ", path='" + path + '\'' +
                ", method=" + method +
                ", methodName='" + methodName + '\'' +
                ", controller=" + controller +
                ", params=" + params +
                ", httpMethod='" + httpMethod + '\'' +
                '}';
    }
}
