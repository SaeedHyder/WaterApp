package com.ingic.waterapp.entities;


import java.util.List;

public class MyProjectsChildEntity {
    private String mWhichFragment;
    private String mDeliveryDate, mDeliveryPeriod;
    private List<MyprojectsChildListEntity> mList;

    public MyProjectsChildEntity(String whichFragment, String deliveryDate, String deliveryPeriod, List<MyprojectsChildListEntity> mList) {

        this.mWhichFragment = whichFragment;
        this.mDeliveryDate = deliveryDate;
        this.mDeliveryPeriod = deliveryPeriod;
        this.mList = mList;
    }

    public List<MyprojectsChildListEntity> getmList() {
        return mList;
    }

    public void setmList(List<MyprojectsChildListEntity> mList) {
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

