package com.practice.rmitest;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author 小白i
 * @date 2020/4/17
 */
public class UserHandlerImpl extends UnicastRemoteObject implements UserHandler {

    public UserHandlerImpl() throws RemoteException {
        super();
    }

    @Override
    public String getUserName(int id) throws RemoteException {
        return "0.0";
    }

    @Override
    public int getUserCount() throws RemoteException {
        return 1;
    }

    @Override
    public User getUserByName(String name) throws RemoteException {
        return new User("z_y.sdc", 20);
    }
}
