package com.practice.client;

import com.practice.rmitest.UserHandler;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * @author 小白i
 * @date 2020/4/17
 */
public class Client {


    public static void main(String[] args) {
        try{
            UserHandler handler = (UserHandler) Naming.lookup("user");
            int count = handler.getUserCount();
            String name = handler.getUserName(1);
            System.out.println("name: " + name);
            System.out.println("count: " + count);
            System.out.println("user: " + handler.getUserByName("lmy86263"));
        } catch(NotBoundException e){
            e.printStackTrace();
        } catch(MalformedURLException e){
            e.printStackTrace();
        } catch(RemoteException e){
            e.printStackTrace();
        }
    }
}
