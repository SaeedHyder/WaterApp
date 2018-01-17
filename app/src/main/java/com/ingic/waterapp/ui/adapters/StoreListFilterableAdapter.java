package com.ingic.waterapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;

import com.google.common.base.Function;
import com.ingic.waterapp.interfaces.OnViewHolderClick;
import com.ingic.waterapp.ui.adapters.abstracts.FilterableRecyclerViewListAdapter;


//Only for use case of Generic @FilterableRecyclerViewListAdapter
public class StoreListFilterableAdapter
        extends FilterableRecyclerViewListAdapter<Object>
        implements Filterable
{
    private Context context;


    public StoreListFilterableAdapter(Context context, OnViewHolderClick listener,
                                      Function<Object, String> converter) {
        super(context, listener, converter);
        this.context = context;
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        return inflater.inflate(R.layout.item_offers_list, viewGroup, false);
        return null;
    }

    @Override
    protected void bindView(final Object item, final FilterableRecyclerViewListAdapter.RecyclerviewViewHolder viewHolder) {
        if (item != null) {

        }
    }

    @Override
    protected int bindItemViewType(int position) {
        return 0;
    }

    @Override
    protected int bindItemId(int position) {
        return 0;
    }
}
