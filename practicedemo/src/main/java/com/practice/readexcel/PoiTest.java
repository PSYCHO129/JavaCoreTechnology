package com.practice.readexcel;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 小白i
 * @date 2020/4/15
 */
public class PoiTest {

    public static void main(String[] args) {
        //使用线程池进行线程管理
        ExecutorService es = Executors.newFixedThreadPool(20);
        //new ThreadPoolExecutor(5,20,0L, TimeUnit.MICROSECONDS)

        //使用计数栅栏
        CountDownLatch doneSignal = new CountDownLatch(1);
        String xlsFile = "d:/poiSXXFSBigData" + "2019010" + ".xlsx";
        PoiExcel.multiThreadWrite(es, doneSignal, xlsFile);
        es.shutdown();
    }
}
