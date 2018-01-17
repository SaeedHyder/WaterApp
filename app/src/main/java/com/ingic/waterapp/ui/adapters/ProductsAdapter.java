package com.ingic.waterapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingic.waterapp.R;
import com.ingic.waterapp.fragments.HomeProductsFragment;
import com.ingic.waterapp.interfaces.OnViewHolderClick;
import com.ingic.waterapp.ui.adapters.abstracts.RecyclerViewListAdapter;

public class ProductsAdapter extends RecyclerViewListAdapter<HomeProductsFragment.projects> {
    private Context context;

    public ProductsAdapter(Context context, OnViewHolderClick listener) {
        super(context, listener);
        this.context = context;
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.item_product, viewGroup, false);
    }

    @Override
    protected void bindView(HomeProductsFragment.projects item, RecyclerviewViewHolder viewHolder) {
        if (item != null) {
            final int position = viewHolder.getAdapterPosition();
            ImageView imgRibbin = (ImageView) viewHolder.getView(R.id.img_ribbin);
            TextView textDiscountTag = (TextView) viewHolder.getView(R.id.tv_waterBottle_discountText);
            TextView textOldAmount = (TextView) viewHolder.getView(R.id.tv_waterBottle_oldAmount);
            View crossLine = (View) viewHolder.getView(R.id.crossLine);

            if (position == 2 || position == 3 || position == 5) {
                imgRibbin.setVisibility(View.VISIBLE);
                textDiscountTag.setVisibility(View.VISIBLE);
                textOldAmount.setVisibility(View.VISIBLE);
                crossLine.setVisibility(View.VISIBLE);
            } else {
                imgRibbin.setVisibility(View.GONE);
                textDiscountTag.setVisibility(View.GONE);
                textOldAmount.setVisibility(View.GONE);
                crossLine.setVisibility(View.GONE);
            }


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