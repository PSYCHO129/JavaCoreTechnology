package com.practice.rmitest;

import java.io.Serializable;

/**
 * @author 小白i
 * @date 2020/4/17
 */
public class User implements Serializable {

    // 该字段必须存在
    private static final long serialVersionUID = 42L;

    String name;
    int id;

    public User(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
