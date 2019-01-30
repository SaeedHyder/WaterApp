package com.ingic.waterapp.viewholders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.ingic.waterapp.R;
import com.ingic.waterapp.activities.DockActivity;
import com.ingic.waterapp.entities.MyOrdersChildEntity;
import com.ingic.waterapp.global.AppConstants;
import com.ingic.waterapp.helpers.SimpleDividerItemDecoration;
import com.ingic.waterapp.interfaces.OnChildViewHolderItemClick;
import com.ingic.waterapp.ui.adapters.MyOrdersChildListAdapter;
import com.ingic.waterapp.ui.adapters.abstracts.RecyclerViewListAdapter;

public class MyProjectsChildVH extends ChildViewHolder {

    private RecyclerView mRecyclerView;
    RecyclerViewListAdapter adapter;

    private TextView mDeliveryDate;
    private TextView mDeliveryPeriod;
    private TextView tvStatus;
    private Button mbtnCancel, mbtnReorder;
    private Context mContext;
    private MyOrdersChildEntity mEntity;

    //For reorder and cancel
    private OnChildViewHolderItemClick childItemListener;


    public MyProjectsChildVH(@NonNull DockActivity context, @NonNull View itemView, OnChildViewHolderItemClick listener) {
        super(itemView);
        this.mContext = context;
        this.childItemListener = listener;

        mDeliveryDate = itemView.findViewById(R.id.tv_itemChild_deliveryDate);
        mDeliveryPeriod = itemView.findViewById(R.id.tv_itemChild_deliveryPeriod);
        mbtnCancel = itemView.findViewById(R.id.btn_itemChild_cancel);
        mbtnReorder = itemView.findViewById(R.id.btn_itemChild_reorder);
        tvStatus = itemView.findViewById(R.id.tv_status);

        mRecyclerView = itemView.findViewById(R.id.rv_itemChild_bottles);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(context));
        adapter = new MyOrdersChildListAdapter(context, null);
        mRecyclerView.setAdapter(adapter);
    }

    public void bind(@NonNull final MyOrdersChildEntity entity, final int parentPosition, int childPosition) {
        mEntity = entity;
        if (entity.getmWhichFragment().equalsIgnoreCase(AppConstants.IN_PROGRESS_ORDER)) {
            mbtnCancel.setVisibility(View.VISIBLE);
            mbtnReorder.setVisibility(View.GONE);
        } else if (entity.getmWhichFragment().equalsIgnoreCase(AppConstants.DELIVERED_ORDER)) {
            mbtnReorder.setVisibility(View.VISIBLE);
            mbtnCancel.setVisibility(View.GONE);
        }

        if (entity.getStatus().equals("2")) {
            mbtnReorder.setVisibility(View.GONE);
            tvStatus.setText(mContext.getResources().getString(R.string.cancelled));
        } else if (entity.getStatus().equals("0")) {
            mbtnCancel.setVisibility(View.VISIBLE);
            mbtnReorder.setVisibility(View.GONE);
            tvStatus.setText(mContext.getResources().getString(R.string.inProgress));
        } else {
            mbtnReorder.setVisibility(View.VISIBLE);
            mbtnCancel.setVisibility(View.GONE);
            tvStatus.setText(mContext.getResources().getString(R.string.delivered));
        }


        mbtnReorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childItemListener.onReorderClick(view, parentPosition, mEntity.getOrderId());
            }
        });

        mbtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childItemListener.onCancelOrderClick(view, parentPosition, mEntity.getOrderId());

            }
        });

        mDeliveryDate.setText(entity.getmDeliveryDate());
        mDeliveryPeriod.setText(entity.getmDeliveryPeriod());
        adapter.addAll(entity.getmList());

    }
}
