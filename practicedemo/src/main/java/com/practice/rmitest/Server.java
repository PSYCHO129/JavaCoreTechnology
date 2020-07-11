package com.practice.rmitest;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author 小白i
 * @date 2020/4/17
 */
public class Server {

    public static void main(String[] args) {
        UserHandler userHandler = null;
        try {
            //这一种方法是调用jdk实现的方法来启动注册表
            Registry registry = LocateRegistry.createRegistry(1099);
            userHandler = new UserHandlerImpl();
            registry.rebind("user",userHandler);

            //第二种需要自己在target/class目录下运行start rmiregistry
            /*userHandler = new UserHandlerImpl();
            Naming.rebind("user", userHandler);*/
            System.out.println(" rmi server is ready ...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
