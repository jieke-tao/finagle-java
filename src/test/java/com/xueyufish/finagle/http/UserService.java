package com.xueyufish.finagle.http;

import java.util.List;

public interface UserService {

    List<User> getUsers();

    User getUser();

    User updateUser(String name, int age);
}
