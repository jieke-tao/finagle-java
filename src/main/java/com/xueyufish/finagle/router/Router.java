package com.xueyufish.finagle.router;

public class Router {

    private String path;
    private Class<?> controllerClass;
    private String action;
    private Object controller;
    private String method;

    public Router(String path, String controller, String action, String method) {
        this.path = path;
        try {
            this.controllerClass = Class.forName(controller.trim());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.action = action;
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public void setControllerClass(Class<?> controllerClass) {
        this.controllerClass = controllerClass;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }


    public void setController(Object controller) {
        this.controller = controller;
    }

    public Object getController() {
        return controller;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
