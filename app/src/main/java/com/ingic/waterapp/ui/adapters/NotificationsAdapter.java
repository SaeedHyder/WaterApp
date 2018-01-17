package com.ingic.waterapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.waterapp.interfaces.OnViewHolderClick;
import com.ingic.waterapp.ui.adapters.abstracts.RecyclerViewListAdapter;

//Only for use case of Generic @RecyclerViewListAdapter
public class NotificationsAdapter extends RecyclerViewListAdapter<Object> {
    private Context context;

    public NotificationsAdapter(Context context, OnViewHolderClick listener) {
        super(context, listener);
        this.context = context;
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        return inflater.inflate(R.layout.item_notifications, viewGroup, false);
        return null;
    }

    @Override
    protected void bindView(Object item, RecyclerviewViewHolder viewHolder) {
        if (item != null) {
            int position = viewHolder.getAdapterPosition();
//            ImageView imgIcon = (ImageView) viewHolder.getView(R.id.img_itemNotification);
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
