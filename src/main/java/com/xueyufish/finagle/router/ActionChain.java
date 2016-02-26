package com.xueyufish.finagle.router;


import java.util.HashMap;
import java.util.Map;

public class ActionChain {

    private String path;
    private ActionChain parent;
    private Map<String, ActionChain> children = new HashMap<>();
    private ActionChain pathChildren = null;

    private Action action;
    private boolean pathParam;
    private String param;

    public ActionChain(String path){
        this.path = path;
    }

    public static ActionChain create(String p) {
        return new ActionChain(p);
    }

    public ActionChain put(ActionChain actionChain) {
        if(actionChain.isPathParam()){
            pathChildren = actionChain;
        }else{
            children.put(actionChain.getPath(), actionChain);
        }
        return this;
    }

    public ActionChain get(String path){
        ActionChain ac = children.get(path);
        if(ac == null){
            ac = pathChildren;
        }
        return ac;
    }

    public boolean hasChildren(){
        return children.size() > 0;
    }

    public ActionChain getParent() {
        return parent;
    }

    public void setParent(ActionChain parent) {
        this.parent = parent;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setPathParam(boolean pathParam) {
        this.pathParam = pathParam;
    }

    public boolean isPathParam() {
        return pathParam;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getParam() {
        return param;
    }
}
