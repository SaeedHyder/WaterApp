package com.ingic.waterapp.interfaces;

import android.view.View;

public interface OnChildViewHolderItemClick {
    void onReorderClick(View view, int position ,int orderId);
    void onCancelOrderClick(View view, int position,int orderId);
}