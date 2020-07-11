package com.practice.readexcel;

import java.math.BigDecimal;

/**
 * @author 小白i
 * @date 2020/4/15
 */
public class PoiEntity {

    private Integer id;
    private String merchantNum;
    private String merchantName;
    private String terminalNum;
    private String dynamicType;
    private String transBegin;
    private String transStatus;
    private BigDecimal totalFee;
    private BigDecimal refundFee;
    private String outTransNo;
    private String bankType;
    private String rateFee;
    private String traceNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMerchantNum() {
        return merchantNum;
    }

    public void setMerchantNum(String merchantNum) {
        this.merchantNum = merchantNum;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getTerminalNum() {
        return terminalNum;
    }

    public void setTerminalNum(String terminalNum) {
        this.terminalNum = terminalNum;
    }

    public String getDynamicType() {
        return dynamicType;
    }

    public void setDynamicType(String dynamicType) {
        this.dynamicType = dynamicType;
    }

    public String getTransBegin() {
        return transBegin;
    }

    public void setTransBegin(String transBegin) {
        this.transBegin = transBegin;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public BigDecimal getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(BigDecimal refundFee) {
        this.refundFee = refundFee;
    }

    public String getOutTransNo() {
        return outTransNo;
    }

    public void setOutTransNo(String outTransNo) {
        this.outTransNo = outTransNo;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getRateFee() {
        return rateFee;
    }

    public void setRateFee(String rateFee) {
        this.rateFee = rateFee;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }
}