package com.ingic.waterapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.waterapp.R;
import com.ingic.waterapp.interfaces.OnViewHolderClick;
import com.ingic.waterapp.ui.adapters.abstracts.RecyclerViewListAdapter;
import com.ingic.waterapp.ui.views.AnyTextView;

/**
 * Created by syed.shah on 7/28/17.
 */

public class SideMenuAdapter extends RecyclerViewListAdapter<String> {
    public SideMenuAdapter(Context context, OnViewHolderClick listener) {
        super(context, listener);
    }

    @Override
    protected int bindItemViewType(int position) {
        return 0;
    }

    @Override
    protected int bindItemId(int position) {
        return position;
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.item_sidemenu, viewGroup, false);
    }

    @Override
    protected void bindView(String item, RecyclerviewViewHolder viewHolder) {
        AnyTextView tvtext = (AnyTextView) viewHolder.getView(R.id.tv_sideMenu_item);
        if (item != null) {
            tvtext.setText(item);
        }
    }

}
