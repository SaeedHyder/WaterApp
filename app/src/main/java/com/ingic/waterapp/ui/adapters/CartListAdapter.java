package com.ingic.waterapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingic.waterapp.R;
import com.ingic.waterapp.helpers.UIHelper;
import com.ingic.waterapp.interfaces.OnViewHolderClick;
import com.ingic.waterapp.ui.adapters.abstracts.RecyclerViewListAdapter;

public class CartListAdapter extends RecyclerViewListAdapter<String> {
    private Context context;

    public CartListAdapter(Context context, OnViewHolderClick listener) {
        super(context, listener);
        this.context = context;
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.item_cart, viewGroup, false);
    }

    @Override
    protected void bindView(final String item, RecyclerviewViewHolder viewHolder) {
        if (item != null) {
            final int position = viewHolder.getAdapterPosition();
            TextView text = (TextView) viewHolder.getView(R.id.tv_itemCart_BottleName);
            ImageView imgDlt = (ImageView) viewHolder.getView(R.id.img_itemCart_dlt);
            CheckBox checkBox = (CheckBox) viewHolder.getView(R.id.cb_itemCart);
            imgDlt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UIHelper.showShortToastInCenter(context, context.getResources().getString(R.string.will_be_implemented_in_beta));
                }
            });
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UIHelper.showShortToastInCenter(context, context.getResources().getString(R.string.will_be_implemented_in_beta));
                }
            });
//            TextViewHelper.setText(text, item);

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