package com.practice.thread.reetrantlock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 小白i
 * @date 2020/3/29
 */
public class Demo1 {

    Lock lock = new ReentrantLock();

    /**
     * 定义方法
     */
    public void setLock(String name){
        //获取锁
        lock.lock();
        try {
            //
            System.out.println(name+"get lock");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //必须要释放锁
            lock.unlock();
        }
    }

}
