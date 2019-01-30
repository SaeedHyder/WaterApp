package com.ingic.waterapp.entities;


import java.util.List;

public class MyOrdersChildEntity {
    private String mWhichFragment;
    private String mDeliveryDate;
    private String mDeliveryPeriod;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    private int orderId;
    private List<MyOrdersChildListEntity> mList;


    public MyOrdersChildEntity(String whichFragment,int orderId, String deliveryDate, String deliveryPeriod, List<MyOrdersChildListEntity> mList,String status) {

        this.mWhichFragment = whichFragment;
        this.orderId = orderId;
        this.mDeliveryDate = deliveryDate;
        this.mDeliveryPeriod = deliveryPeriod;
        this.mList = mList;
           this.status = status;
    }



    public List<MyOrdersChildListEntity> getmList() {
        return mList;
    }

    public void setmList(List<MyOrdersChildListEntity> mList) {
        this.mList = mList;
    }

    public String getmDeliveryDate() {
        return mDeliveryDate;
    }

    public void setmDeliveryDate(String mDeliveryDate) {
        this.mDeliveryDate = mDeliveryDate;
    }

    public String getmDeliveryPeriod() {
        return mDeliveryPeriod;
    }

    public void setmDeliveryPeriod(String mDeliveryPeriod) {
        this.mDeliveryPeriod = mDeliveryPeriod;
    }

    public String getmWhichFragment() {
        return mWhichFragment;
    }

    public void setmWhichFragment(String mWhichFragment) {
        this.mWhichFragment = mWhichFragment;
    }
}

