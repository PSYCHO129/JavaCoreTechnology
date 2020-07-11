package com.proxy;

/**
 * @author 小白i
 * @date 2020/7/11
 */
public interface UserManager {
    /**
     * 新增用户
     *
     * @param userName 用户名
     * @param password 密码
     */
    void addUser(String userName, String password);

    /**
     * 删除用户抽象方法
     *
     * @param userName 用户名
     */
    void delUser(String userName);

}
