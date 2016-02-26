package com.xueyufish.finagle.http;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.alibaba.fastjson.JSONArray;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:finagle-server.xml", "classpath:finagle-client.xml"})
public class UserServiceTest {

    @Autowired
    HttpClient httpClient;

    @Test
    public void getUsers() {
        String users = httpClient.get("/test/users");
        JSONArray userArray = JSON.parseArray(users);
        Assert.assertEquals(userArray.size(), 4);
    }

    @Test
    public void getUser() {
        String userJson = httpClient.get("/test/user/1");
        User user = JSON.parseObject(userJson, User.class);
        Assert.assertEquals(user.getId(), 1);
        Assert.assertEquals(user.getName(), "小明");
        Assert.assertEquals(user.getAge(), 10);
    }

    @Test
    public void updateUser() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "小明");
        params.put("age", 10);
        User user = httpClient.post("/test/user/update", params, User.class);
        Assert.assertEquals(user.getId(), 1);
        Assert.assertEquals(user.getName(), "小明");
        Assert.assertEquals(user.getAge(), 10);
    }

}
