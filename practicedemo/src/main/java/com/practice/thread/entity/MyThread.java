package com.practice.thread.entity;

/**
 * @author 小白i
 * @date 2020/3/29
 */
public class MyThread implements Runnable {

    private long time;

    public MyThread() {
    }

    public MyThread(String name, long time) {
        Thread.currentThread().setName(name);
        this.time = time;
    }

    @Override
    public void run() {
        try {
            String info = "我是线程："+Thread.currentThread().getName();
            System.out.println(info + "开始等待。。。");
            if (time != 0) {

                Thread.sleep(time);

            } else {
                Thread.sleep(5000);
            }
            System.out.println(info + "结束等待。。。");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
