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
import com.ingic.waterapp.entities.MyProjectsChildEntity;
import com.ingic.waterapp.global.AppConstants;
import com.ingic.waterapp.helpers.SimpleDividerItemDecoration;
import com.ingic.waterapp.helpers.UIHelper;
import com.ingic.waterapp.interfaces.OnViewHolderClick;
import com.ingic.waterapp.ui.adapters.MyOrdersChildListAdapter;
import com.ingic.waterapp.ui.adapters.abstracts.RecyclerViewListAdapter;

public class MyProjectsChildVH extends ChildViewHolder implements OnViewHolderClick {

    private RecyclerView mRecyclerView;
    RecyclerViewListAdapter adapter;

    private TextView mDeliveryDate;
    private TextView mDeliveryPeriod;
    private Button mbtnCancel, mbtnReorder;
    private Context mContext;

    public MyProjectsChildVH(@NonNull Context context, @NonNull View itemView) {
        super(itemView);
        this.mContext = context;
        mDeliveryDate = itemView.findViewById(R.id.tv_itemChild_deliveryDate);
        mDeliveryPeriod = itemView.findViewById(R.id.tv_itemChild_deliveryPeriod);
        mbtnCancel = itemView.findViewById(R.id.btn_itemChild_cancel);
        mbtnReorder = itemView.findViewById(R.id.btn_itemChild_reorder);


        mRecyclerView = itemView.findViewById(R.id.rv_itemChild_bottles);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(context));
        adapter = new MyOrdersChildListAdapter(context, this);
        mRecyclerView.setAdapter(adapter);

//        ArrayList<String> list = new ArrayList<>();
//        list.add("");
//        list.add("");
//        list.add("");
//        list.add("");
//
//        adapter.addAll(list);


//        mDeliveryPeriod = itemView.findViewById(R.id.tv_itemRecyclerView_date);
    }

    public void bind(@NonNull MyProjectsChildEntity entity) {
        if (entity.getmWhichFragment().equalsIgnoreCase(AppConstants.IN_PROGRESS_ORDER)) {
            mbtnCancel.setVisibility(View.VISIBLE);
            mbtnReorder.setVisibility(View.GONE);
        } else if (entity.getmWhichFragment().equalsIgnoreCase(AppConstants.DELIVERED_ORDER)) {
            mbtnReorder.setVisibility(View.VISIBLE);
            mbtnCancel.setVisibility(View.GONE);
        }

        mbtnReorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.showShortToastInCenter(mContext, mContext.getResources().getString(R.string.will_be_implemented_in_beta));
            }
        });

        mbtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.showShortToastInCenter(mContext, mContext.getResources().getString(R.string.will_be_implemented_in_beta));
            }
        });

        mDeliveryDate.setText(entity.getmDeliveryDate());
        mDeliveryPeriod.setText(entity.getmDeliveryPeriod());
        adapter.addAll(entity.getmList());
//        mDeliveryPeriod.setText(entity.getmList().get(position).getdate());
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
