package com.ingic.waterapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingic.waterapp.R;
import com.ingic.waterapp.entities.NotificationCountEnt;
import com.ingic.waterapp.helpers.DateHelper;
import com.ingic.waterapp.helpers.TextViewHelper;
import com.ingic.waterapp.interfaces.OnViewHolderClick;
import com.ingic.waterapp.ui.adapters.abstracts.RecyclerViewListAdapter;

public class NotificationsListAdapter extends RecyclerViewListAdapter<NotificationCountEnt> {
    private Context context;

    public NotificationsListAdapter(Context context, OnViewHolderClick listener) {
        super(context, listener);
        this.context = context;
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.item_notification, viewGroup, false);
    }

    @Override
    protected void bindView(final NotificationCountEnt item, RecyclerviewViewHolder viewHolder) {
        if (item != null) {
            final int position = viewHolder.getAdapterPosition();
            TextView text = (TextView) viewHolder.getView(R.id.tv_item_notification);
            TextView date = (TextView) viewHolder.getView(R.id.tv_item_date);
            TextViewHelper.setText(text, item.getMessage());
            TextViewHelper.setText(date, DateHelper.getChatMessageTime(item.getCreatedAt()));
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