package com.xueyufish.finagle.http;

import com.xueyufish.finagle.FinagleService;

import java.util.ArrayList;
import java.util.List;

@FinagleService(name = "userService", path = "/userService")
public class UserServiceImpl implements UserService {

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<User>();
        users.add(new User(1, "小明", 10));
        users.add(new User(2, "小强", 12));
        users.add(new User(3, "小方", 14));
        users.add(new User(4, "小圆", 16));
        return users;
    }

    @Override
    public User getUser() {
        return new User(1, "小明", 10);
    }

    @Override
    public User updateUser(String name, int age) {
        return new User(1, name, age);
    }
}
