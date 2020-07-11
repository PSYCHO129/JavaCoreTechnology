package com.practice.readexcel;

import java.util.concurrent.CountDownLatch;

/**
 * @author 小白i
 * @date 2020/4/15
 */
public class PoiWritor implements Runnable {

    private final CountDownLatch doneSignal;
    private PoiExcel poiExcel;

    public PoiWritor(CountDownLatch doneSignal, PoiExcel poiExcel) {
        this.doneSignal = doneSignal;
        this.poiExcel = poiExcel;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (doneSignal.getCount() <= 0) {
                    break;
                }
                poiExcel.customerData(doneSignal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } /*finally {            doneSignal.countDown();            System.out.println(" Count: " + doneSignal.getCount() + " thread name " + Thread.currentThread().getName());        }*/
    }
}
