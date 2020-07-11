package com.practice.rmitest;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author 小白i
 * @date 2020/4/17
 */
public interface UserHandler extends Remote {

    String getUserName(int id) throws RemoteException;
    int getUserCount() throws RemoteException;
    User getUserByName(String name) throws RemoteException;
}
