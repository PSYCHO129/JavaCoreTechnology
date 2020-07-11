package com.practice.readexcel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.SheetUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 小白i
 * @date 2020/4/15
 */
public class PoiExcel {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    private static volatile SXSSFWorkbook wb = new SXSSFWorkbook();

    /**
     * 总行号
     */
    private static AtomicInteger rowNo = new AtomicInteger(0);

    /**
     * 页行号
     */
    private static AtomicInteger pageRowNo = new AtomicInteger(0);
    private static AtomicInteger count = new AtomicInteger(0);
    static BlockingQueue<List<PoiEntity>> queue = new ArrayBlockingQueue(5);
    static BlockingQueue<String> sqlQueue = new LinkedBlockingQueue();
    /**
     * excel 每个sheet大小
     */
    private static final int MAX_SHEET_PAGE_SIZE = 200000;
    /**
     * 查询总记录数
     */
    private static int TOTAL_COUNT;
    /**
     * 判断CountDownLatch，当所有线程运行结束，就写入到文件流中
     */
    private static AtomicInteger DONESIGNAL_COUNT;
    /**
     * 表名数组
     */
    private static List<String> tableNameArr = new ArrayList<String>();
    /**
     * 读取数据库中存储的数据行数
     */
    public static int PAGE_SIZE = 50000;
    /**
     * 工作表对象
     */
    protected static volatile SXSSFSheet sheet;
    /**
     * excel工作簿数组
     */
    protected static ArrayList<SXSSFSheet> sheetList = new ArrayList<SXSSFSheet>();
    /**
     * cell列数
     */
    private static final int cellLength = 10;
    private static final int windowSize = 200;

    public void customerData(CountDownLatch doneSignal) throws Exception {
        List list = queue.take();
        int len = list.size();
        for (int i = 0; i < len; i++) {
            Row row_value = null;
            synchronized (rowNo) {
                if (rowNo.get() % MAX_SHEET_PAGE_SIZE == 0) {
                    if (count.get() < sheetList.size()) {
                        sheet = sheetList.get(count.get());
                        pageRowNo.set(0);
                        //每当新建了工作表就将当前工作表的行号重置为0
                        createHeader();
                        setColumnWidthByType(sheet, cellLength);
                        count.incrementAndGet();
                    }
                }
                row_value = sheet.createRow(pageRowNo.incrementAndGet());
                rowNo.incrementAndGet();
            }
            addToRow(i, row_value, list);
        }
        System.out.println("sheet name " + sheet.getSheetName() + " rowNo " + rowNo + " DONESIGNAL_COUNT " + DONESIGNAL_COUNT);
        DONESIGNAL_COUNT.getAndAdd(-list.size());
        list.clear();
        if (DONESIGNAL_COUNT.get() == 0) {
            doneSignal.countDown();
        }
    }

    protected void addToRow(int i, Row row_value, List _list) {
        List<PoiEntity> list = _list;
        Cell cel0_value = row_value.createCell(0);
        cel0_value.setCellValue(list.get(i).getTraceNo());
        Cell cel2_value = row_value.createCell(1);
        cel2_value.setCellValue(list.get(i).getMerchantNum());
        Cell cel3_value = row_value.createCell(2);
        cel3_value.setCellValue(list.get(i).getTotalFee() + "");
        Cell cel4_value = row_value.createCell(3);
        cel4_value.setCellValue(list.get(i).getMerchantName());
        Cell cel5_value = row_value.createCell(4);
        cel5_value.setCellValue(list.get(i).getDynamicType());
        Cell cel6_value = row_value.createCell(5);
        cel6_value.setCellValue(list.get(i).getTransBegin());
        Cell cel7_value = row_value.createCell(6);
        cel7_value.setCellValue(list.get(i).getTransStatus());
        Cell cel8_value = row_value.createCell(7);
        cel8_value.setCellValue(list.get(i).getRefundFee() + "");
        Cell cel9_value = row_value.createCell(8);
        cel9_value.setCellValue(list.get(i).getOutTransNo());
        Cell cel10_value = row_value.createCell(9);
        cel10_value.setCellValue(list.get(i).getRateFee());
        //Cell cel11_value = row_value.createCell(10);
        // cel11_value.setCellValue(list.get(i).getBankType());
        // /*   Cell cel12_value = row_value.createCell(11);
        // cel2_value.setCellValue(list.get(i).getTerminalNum());*/
    }

    /**
     * 定义表头
     */
    protected void createHeader() {
        Row row = sheet.createRow(0);
        Cell cel0 = row.createCell(0);
        cel0.setCellValue("traceNo");
        Cell cel2 = row.createCell(1);
        cel2.setCellValue("merchantNum");
        Cell cel3 = row.createCell(2);
        cel3.setCellValue("totalFee");
        Cell cel4 = row.createCell(3);
        cel4.setCellValue("merchantName");
        Cell cel5 = row.createCell(4);
        cel5.setCellValue("dynamicType");
        Cell cel6 = row.createCell(5);
        cel6.setCellValue(" transBegin ");
        Cell cel7 = row.createCell(6);
        cel7.setCellValue("transStatus");
        Cell cel8 = row.createCell(7);
        cel8.setCellValue("refundFee");
        Cell cel9 = row.createCell(8);
        cel9.setCellValue("outTransNo");
        Cell cel10 = row.createCell(9);
        cel10.setCellValue("rateFee");
        //Cell cel11 = row.createCell(10);
        // cel11.setCellValue("bankType");
        /*   Cell cel2 = row.createCell(11);        cel2.setCellValue("terminalNum");*/
    }

    protected static int getListCount(List tableNameArr) throws Exception {
        PreparedStatement stmt = DbUtils.getStm();
        int listCount = 0;
        for (int i = 0; i < tableNameArr.size(); i++) {
            String queryCount = "select count(1) from  " + tableNameArr.get(i);
            ResultSet rsTotal = stmt.executeQuery(queryCount);
            rsTotal.next();
            listCount += rsTotal.getInt(1);
        }
        return listCount;
    }

    /**
     * 生成工作簿
     * @param sheet
     */
    protected static void setSheet(SXSSFSheet sheet) {
        int size = 1;
        if (TOTAL_COUNT > MAX_SHEET_PAGE_SIZE) {
            size = TOTAL_COUNT / MAX_SHEET_PAGE_SIZE == 0 ? TOTAL_COUNT / MAX_SHEET_PAGE_SIZE : (TOTAL_COUNT / MAX_SHEET_PAGE_SIZE) + 1;
        }
        for (int i = 0; i < size; i++) {
            /**建立新的sheet对象*/
            sheet = wb.createSheet("我的第" + i + "个工作簿");
            sheet.setRandomAccessWindowSize(windowSize);
            /**动态指定当前的工作表*/
            sheet = wb.getSheetAt(i);
            sheetList.add(sheet);
        }
    }

    /**
     * 根据类型指定excel文件的列宽
     */
    private void setColumnWidthByType(SXSSFSheet sheet, int titleLength) {
        sheet.trackAllColumnsForAutoSizing();
        for (int i = 0; i < titleLength; i++) {
            int columnWidth = sheet.getRow(0).getCell(i).getStringCellValue().length();
            //获取表头的宽度
            int autowidth = (int) SheetUtil.getColumnWidth(sheet, i, false, 1, sheet.getLastRowNum());
            if (columnWidth > autowidth) {
                sheet.setColumnWidth(i, (int) 400.0D * (columnWidth + 1));
            } else {
                sheet.autoSizeColumn(i);
            }
        }
    }

    /**
     * 添加表名     *     * @param dateList
     */
    private static void addTable(List<String> dateList) {
        dateList.forEach(date -> {
            /**兴业*/
            tableNameArr.add("xingye_bill_download_day_101590267206_" + date);
            tableNameArr.add("xingye_bill_download_day_101540080217_" + date);
            /**汇付*/
            tableNameArr.add("posp_huifu_detail_day_" + date);
        });
    }

    /**
     * 添加sql
     * @param dateList
     * @return
     */
    private static List<String> getSqlList(List<String> dateList, String where) {
        List<String> listSql = new ArrayList<>();
        dateList.forEach(date -> {
            /**兴业*/
            String sql1 = "select id,merchant_num merchantNum,merchant_name merchantName,trans_type dynamicType,trans_time transBegin" + "                ,trans_status transStatus,total_fee totalFee,refund_fee refundFee,third_merchant_num outTransNo," + "                rate_fee rateFee ,trace_num traceNo from  xingye_bill_download_day_101590267206_" + date + " WHERE  id >  ownId   " + where + "  ORDER BY id ASC  LIMIT  " + PAGE_SIZE;
            String sql2 = "select id,merchant_num merchantNum,merchant_name merchantName,trans_type dynamicType,trans_time transBegin" + "                ,trans_status transStatus,total_fee totalFee,refund_fee refundFee,third_merchant_num outTransNo," + "                rate_fee rateFee ,trace_num traceNo from  xingye_bill_download_day_101540080217_" + date + " WHERE  id >   ownId   " + where + "   ORDER BY id ASC  LIMIT  " + PAGE_SIZE;            /**汇付*/
            String sql3 = "select id,merchant_num merchantNum,merchant_name merchantName ,trade_type dynamicType,DATE_FORMAT(trade_date,'%Y-%m-%d-%H-%i-%s') transBegin " + "                ,trade_status transStatus,total_fee totalFee,recorded_money refundFee,outside_order_num outTransNo, " + "                rate_fee rateFee ,trace_num traceNo  from  posp_huifu_detail_day_" + date + " WHERE  id >   ownId   " + where + "   ORDER BY id ASC  LIMIT  " + PAGE_SIZE;
            listSql.add(sql1);
            listSql.add(sql2);
            listSql.add(sql3);
        });
        return listSql;
    }

    /**
     * 得到两个日期之间的天数，数组
     * @param dBegin
     * @param dEnd     *
     * @return
     */
    public static List<String> findDates(Date dBegin, Date dEnd) {
        List lDate = new ArrayList();
        lDate.add(sdf.format(dBegin));
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(sdf.format(calBegin.getTime()));
        }
        return lDate;
    }

    /**
     * 使用多线程进行Excel写操作，提高写入效率。
     */
    public static void multiThreadWrite(ExecutorService es, CountDownLatch doneSignal, String xlsFile) {
        try {
            /**预生产数据*/
            long startTime = System.currentTimeMillis();
            //开始时间
            Date begin = sdf.parse("20190602");
            Date end = sdf.parse("20190602");
            List<String> dateList = findDates(begin, end);
            addTable(dateList);
            TOTAL_COUNT = getListCount(tableNameArr);
            DONESIGNAL_COUNT = new AtomicInteger(TOTAL_COUNT);
            setSheet(sheet);
            sheet = sheetList.get(0);
            String where = "";
            getSqlList(dateList, where).forEach(sql -> {
                try {
                    sqlQueue.put(sql);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            /**多线程处理数据*/
            int size = sqlQueue.size();
            /**最多创建10个线程，确保线程不在开始时就被阻塞*/
            if (size > 0) {
                size = size > 12 ? 10 : size;
                for (int i = 0; i < size; i++) {
                    es.submit(new PoiProductor(doneSignal));
                }
            }
            PoiExcel poiExcel = new PoiExcel();
            es.submit(new PoiWritor(doneSignal, poiExcel));
            doneSignal.await();
            System.out.println("read  finish execute time: " + (System.currentTimeMillis() - startTime) / 1000 + " s");
            FileOutputStream os = new FileOutputStream(xlsFile);
            wb.write(os);
            os.flush();
            os.close();
            System.out.println(" outPutStream finish execute time: " + (System.currentTimeMillis() - startTime) / 1000 + " s");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}