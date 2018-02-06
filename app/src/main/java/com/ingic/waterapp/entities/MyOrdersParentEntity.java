package com.ingic.waterapp.entities;


import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

public class MyOrdersParentEntity implements Parent<MyOrdersChildEntity> {
    private final List<MyOrdersChildEntity> childList;
    private String orderId;
    private String totalAmount;


    public MyOrdersParentEntity(String orderId, String totalAmount, List<MyOrdersChildEntity> list) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        childList = list;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public MyOrdersChildEntity getChild(int position) {
//        if (childList == null || childList.size() < position) {
//            return null;
//        } else
        return childList.get(position);
    }

    @Override
    public List<MyOrdersChildEntity> getChildList() {
        return childList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
