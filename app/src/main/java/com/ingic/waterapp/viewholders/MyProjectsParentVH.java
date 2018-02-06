package com.ingic.waterapp.viewholders;


import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.ingic.waterapp.R;
import com.ingic.waterapp.activities.DockActivity;
import com.ingic.waterapp.entities.MyOrdersParentEntity;

public class MyProjectsParentVH extends ParentViewHolder {
    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 360f;
    private final RelativeLayout rl_top;


    private TextView mOrderId;
    private TextView mTotalAmount;


    public MyProjectsParentVH(final View itemView, final DockActivity docActivity) {
        super(itemView);
        itemView.setOnClickListener(null);
        rl_top = itemView.findViewById(R.id.rl_top);

        mOrderId = itemView.findViewById(R.id.tv_itemMyOrder_orderId);
        mTotalAmount = itemView.findViewById(R.id.tv_itemMyOrder_totalAmount);

        rl_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isExpanded()) collapseView();
                else expandView();
            }
        });

    }

    public void bind(@NonNull MyOrdersParentEntity entity) {
        mOrderId.setText(entity.getOrderId());
        mTotalAmount.setText(entity.getTotalAmount());
    }

    @SuppressLint("NewApi")
    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);
    }

    @Override
    public void onExpansionToggled(final boolean expanded) {
        super.onExpansionToggled(expanded);
    }
}
