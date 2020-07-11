package com.practice.thread;

import com.practice.thread.entity.MyThread;

/**
 * @author 小白i
 * @date 2020/3/29
 */
public class ThreadDemo1 {

    /**
     * 测试
     * @param args 参数
     * @throws InterruptedException 异常信息
     */
    public static void main(String[] args) throws InterruptedException {
        Thread s1 = new Thread(new MyThread("111",5000));
        Thread s2 = new Thread(new MyThread("222",1000));
        Thread s3 = new Thread(new MyThread("333",3000));

        s1.start();
//        s1.join();

        s2.start();
//        s2.join();

        s3.start();
//        s3.join();
    }

}
