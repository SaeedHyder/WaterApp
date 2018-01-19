package com.ingic.waterapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ingic.waterapp.R;
import com.ingic.waterapp.entities.Product;
import com.ingic.waterapp.interfaces.OnViewHolderClick;
import com.ingic.waterapp.ui.adapters.abstracts.RecyclerViewListAdapter;
import com.squareup.picasso.Picasso;

public class ProductsAdapter extends RecyclerViewListAdapter<Product> {
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
    protected void bindView(Product item, RecyclerviewViewHolder viewHolder) {
        if (item != null) {

            final int position = viewHolder.getAdapterPosition();
            ImageView imgRibbin = (ImageView) viewHolder.getView(R.id.img_ribbin);
            ImageView img_bottle = (ImageView) viewHolder.getView(R.id.img_bottle);
            TextView textDiscountTag = (TextView) viewHolder.getView(R.id.tv_waterBottle_discountText);
            TextView textOldAmount = (TextView) viewHolder.getView(R.id.tv_waterBottle_oldAmount);
            TextView tv_waterBottle_amount = (TextView) viewHolder.getView(R.id.tv_waterBottle_amount);
            TextView tv_waterBottle_name = (TextView) viewHolder.getView(R.id.tv_waterBottle_name);
            TextView tv_waterBottle_quantity = (TextView) viewHolder.getView(R.id.tv_waterBottle_quantity);

            View crossLine = (View) viewHolder.getView(R.id.crossLine);

            if (item.getCouponName()!= null && item.getCouponName().length() > 0) {
                imgRibbin.setVisibility(View.VISIBLE);
                textDiscountTag.setVisibility(View.VISIBLE);
                textOldAmount.setVisibility(View.VISIBLE);
                crossLine.setVisibility(View.VISIBLE);
                textDiscountTag.setText(item.getCouponName());
            } else {
                imgRibbin.setVisibility(View.GONE);
                textDiscountTag.setVisibility(View.GONE);
                textOldAmount.setVisibility(View.GONE);
                crossLine.setVisibility(View.GONE);
            }

            if(item.getProductImage()!= null && item.getProductImage().length() > 0) {
                Picasso.with(context)
                        .load(item.getProductImage())
                        .into(img_bottle);
            }

            tv_waterBottle_amount.setText(item.getProductAmount());
            tv_waterBottle_name.setText(item.getProductName());

        }
    }

    @Override
    protected int bindItemViewType(int position) {
        return 0;
    }

    @Override
    protected int bindItemId(int position) {
        return position;
    }
}