package com.br.shopping.dao.domain;

import java.util.Date;

public class OrderInfo {
    private Integer orderNo;

    private String customerId;

    private String orderName;

    private Float payAmt;

    private Date createTime;

    private String payState;

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId == null ? null : customerId.trim();
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName == null ? null : orderName.trim();
    }

    public Float getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(Float payAmt) {
        this.payAmt = payAmt;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState == null ? null : payState.trim();
    }
}