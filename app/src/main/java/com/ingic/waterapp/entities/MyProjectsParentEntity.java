package com.ingic.waterapp.entities;


import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

public class MyProjectsParentEntity implements Parent<MyProjectsChildEntity> {
    private final List<MyProjectsChildEntity> childList;
    private int img;
    private String orderId;
    private String totalAmount;


    public MyProjectsParentEntity(String orderId, String totalAmount, List<MyProjectsChildEntity> list) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        childList = list;
    }

    public String getOrderId() {
        return orderId;
    }

    public int getImg() {
        return img;
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

    public MyProjectsChildEntity getChild(int position) {
//        if (childList == null || childList.size() < position) {
//            return null;
//        } else
        return childList.get(position);
    }

    @Override
    public List<MyProjectsChildEntity> getChildList() {
        return childList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
