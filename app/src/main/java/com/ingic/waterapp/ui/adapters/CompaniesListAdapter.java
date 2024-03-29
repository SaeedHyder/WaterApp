package com.ingic.waterapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingic.waterapp.R;
import com.ingic.waterapp.entities.CompanyEnt;
import com.ingic.waterapp.helpers.ImageLoaderHelper;
import com.ingic.waterapp.helpers.TextViewHelper;
import com.ingic.waterapp.interfaces.OnViewHolderClick;
import com.ingic.waterapp.ui.adapters.abstracts.RecyclerViewListAdapter;

public class CompaniesListAdapter extends RecyclerViewListAdapter<CompanyEnt> {
    private Context context;

    public CompaniesListAdapter(Context context, OnViewHolderClick listener) {
        super(context, listener);
        this.context = context;
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.item_companies, viewGroup, false);
    }

    @Override
    protected void bindView(final CompanyEnt item, RecyclerviewViewHolder viewHolder) {
        if (item != null) {
            final int position = viewHolder.getAdapterPosition();
            TextView text = (TextView) viewHolder.getView(R.id.tv_item_companies);
            TextViewHelper.setText(text, item.getFullName());

            ImageView imageView = (ImageView) viewHolder.getView(R.id.img_item_companies);
            ImageLoaderHelper.loadImageWithPicasso(context, item.getProfileImage(), imageView);

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