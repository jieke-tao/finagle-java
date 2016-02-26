package com.xueyufish.finagle.router;

import com.alibaba.fastjson.JSON;
import com.xueyufish.finagle.util.ClassesUtil;
import com.xueyufish.finagle.util.LogUtil;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.util.CharsetUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ActionInvoker {

    private final LogUtil logger = LogUtil.getLogger(ActionInvoker.class);

    public void invoke(Action action, Map<String, List<String>> params, HttpResponse response) {

        List<Object> pp = new ArrayList<>();

        for (RequestParam param : action.getParams()) {
            String paramName = param.getName();
            Class<?> type = param.getType();
            List<String> values = params.get(paramName);
            String value = (values != null) && (values.size() > 0) ? values
                    .get(0) : null;
            if (ClassesUtil.isPrimitive(type)) {
                pp.add(ClassesUtil.primitiveConverter(type, value));
            } else if (type == String.class) {
                pp.add(value);
            } else {
                pp.add(null);
            }
        }

        try {
            logger.logDebug("Params: {}", pp);
            Object result = action.getMethod().invoke(
                    action.getController(), pp.toArray());
            sendHttpResponse(response,
                    result);
        } catch (Exception e) {
            logger.logError("action[{}] invoke error.", action);
            logger.logError(e.getMessage(), e);
            response.setStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private void sendHttpResponse(HttpResponse response, Object content) {
        String resultString = null;
        if (content == null) {

        } else if (content.getClass().isPrimitive()) {
            resultString = String.valueOf(content);
        } else if (content instanceof String) {
            resultString = content.toString();
        } else {
            resultString = JSON.toJSONString(content);
        }
        if (resultString != null)
            response.setContent(ChannelBuffers.copiedBuffer(resultString,
                    CharsetUtil.UTF_8));
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE,
                "text/plain; charset=UTF-8");
    }
}
