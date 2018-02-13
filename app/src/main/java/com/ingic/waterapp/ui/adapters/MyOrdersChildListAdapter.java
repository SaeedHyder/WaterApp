package com.ingic.waterapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingic.waterapp.R;
import com.ingic.waterapp.entities.MyOrdersChildListEntity;
import com.ingic.waterapp.helpers.ImageLoaderHelper;
import com.ingic.waterapp.helpers.TextViewHelper;
import com.ingic.waterapp.interfaces.OnViewHolderClick;
import com.ingic.waterapp.ui.adapters.abstracts.RecyclerViewListAdapter;


public class MyOrdersChildListAdapter extends RecyclerViewListAdapter<MyOrdersChildListEntity> {
    private Context context;

    public MyOrdersChildListAdapter(Context context, OnViewHolderClick listener) {
        super(context, listener);
        this.context = context;
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.item_child_recyclerview, viewGroup, false);
    }


    @Override
    protected void bindView(MyOrdersChildListEntity item, RecyclerviewViewHolder viewHolder) {
        if (item != null) {
            final int position = viewHolder.getAdapterPosition();
            ImageView textImg = (ImageView) viewHolder.getView(R.id.img_itemChidRv_bottle);
            TextView textTitle = (TextView) viewHolder.getView(R.id.tv_itemChidRv_BottleName);
            TextView textAmount = (TextView) viewHolder.getView(R.id.tv_itemChidRv_amount);
            TextView textQuantity = (TextView) viewHolder.getView(R.id.tv_itemChidRv_quantity);
            TextView textUnitAmount = (TextView) viewHolder.getView(R.id.tv_itemChidRv_ltr);

            TextViewHelper.setText(textTitle, item.getName());
            TextViewHelper.setText(textAmount, item.getPrice());
            TextViewHelper.setText(textQuantity, item.getQuantity());
            TextViewHelper.setText(textUnitAmount, item.getLiter() + " " + item.getUnit());
            ImageLoaderHelper.loadImageWithPicasso(context, item.getImgUrl(), textImg);

//            img.setImageBitmap(ImageLoaderHelper.getRoundedBitmap(getContext() ,getContext().getResources().getDrawable(R.drawable.placeholder_image)));
//            img.setBackgroundResource(item.getPicture());
//            TextViewHelper.setText(textTitle,item.getTitle());
//            TextViewHelper.setText(textDesc,item.getDesc());
        }
    }

//    @Override
//    protected void bindView(ServicesFragment.ServicesModel item, RecyclerviewViewHolder viewHolder) {
//        if (item != null) {
//            final int position = viewHolder.getAdapterPosition();
//            ImageView img = (ImageView) viewHolder.getView(R.id.img_itemNews);
//            TextView textTitle = (TextView) viewHolder.getView(R.id.tv_itemNews_title);
//            TextView textDesc = (TextView) viewHolder.getView(R.id.tv_itemNews_description);
//
//            textDesignation.setVisibility(View.VISIBLE);
//            img.setBackgroundResource(item.getPicture());
//            TextViewHelper.setText(textTitle,item.getTitle());
//            TextViewHelper.setText(textDesc,item.getDesc());
//        }
//    }

    @Override
    protected int bindItemViewType(int position) {
        return 0;
    }

    @Override
    protected int bindItemId(int position) {
        return position;
    }
}