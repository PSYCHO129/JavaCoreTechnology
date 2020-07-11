package com.practice.readexcel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author 小白i
 * @date 2020/4/15
 */
public class PoiProductor implements Runnable {

    private final CountDownLatch doneSignal;


    public PoiProductor(CountDownLatch doneSignal) {

        this.doneSignal = doneSignal;

    }

    @Override
    public void run() {
        List list = null;
        String sql = null;
        int id = 0;
        while (true) {
            try {
                //synchronized (doneSignal) {
                if (null == list || list.size() < 1) {
                    id = 0;
                    if (PoiExcel.sqlQueue.size() > 0) {
                        sql = PoiExcel.sqlQueue.take();
                    } else {
                        break;
                    }
                }                //}
                if (null != list && list.size() > 0) {
                    id = getId(list);
                }
                list = getListData(sql, id);
                PoiExcel.queue.put(list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static int getId(List list) throws Exception {
        List<PoiEntity> _list = list;
        int id = _list.get(_list.size() - 1).getId();
        return id;
    }

    private static void addToList(List _list, ResultSet rs) throws Exception {
        List<PoiEntity> list = _list;
        while (rs.next()) {
            String dynamicType = getDynamicType(rs);
            String transStatus = getTransStatus(rs);
            PoiEntity usera = new PoiEntity();
            usera.setId((rs.getInt("id")));
            usera.setTraceNo((rs.getString("traceNo")));
            usera.setMerchantNum((rs.getString("merchantNum")));
            usera.setTotalFee(rs.getBigDecimal("totalFee"));
            usera.setMerchantName(rs.getString("merchantName"));
            // usera.setTerminalNum(rs.getString("terminalNum"));
            usera.setDynamicType(dynamicType);
            usera.setTransBegin(rs.getString("transBegin"));
            usera.setTransStatus(transStatus);
            usera.setRefundFee(rs.getBigDecimal("refundFee"));
            usera.setOutTransNo(rs.getString("outTransNo"));
            //  usera.setBankType(rs.getString("bankType"));
            usera.setRateFee(rs.getString("rateFee"));
            list.add(usera);
        }
    }

    private static String getTransStatus(ResultSet rs) throws SQLException {
        String transStatus = rs.getString("transStatus");
        /*  if (transStatus.contains("weixin")) {
        transStatus = "微信";
        }*/
        return transStatus;
    }

    private static String getDynamicType(ResultSet rs) throws SQLException {
        String dynamicType = rs.getString("dynamicType");
        /*  if (dynamicType.contains("weixin")) {            dynamicType = "微信支付";        }
               if (dynamicType.contains("alipay")) {            dynamicType = "支付宝支付";        }*/
        return dynamicType;
    }

    public static List getListData(String sql, int id) throws Exception {
        List _list = new ArrayList();
        PreparedStatement stmt = DbUtils.getStm();
        //posp_merchant_account_stats_detail_day_20190607
        sql = sql.replace("ownId", id + "");
        ResultSet rs = stmt.executeQuery(sql);
        if (null != rs) {
            addToList(_list, rs);
            return _list;
        } else {
            _list.clear();
            return null;
        }
    }
}
