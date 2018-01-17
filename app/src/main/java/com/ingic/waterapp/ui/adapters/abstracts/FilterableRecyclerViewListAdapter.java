package com.ingic.waterapp.ui.adapters.abstracts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.ingic.waterapp.interfaces.OnViewHolderClick;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FilterableRecyclerViewListAdapter<T> extends RecyclerView.Adapter<FilterableRecyclerViewListAdapter.RecyclerviewViewHolder>
        implements Filterable {
    ToStringFilter toStringFilter = new ToStringFilter();
    private List<T> originalList = Collections.emptyList();
    private List<T> arrayList = Collections.emptyList();
    private Context mContext;
    private OnViewHolderClick originalListener;

    public FilterableRecyclerViewListAdapter(Context context) {
        this(context, null, null);
    }

    public FilterableRecyclerViewListAdapter(Context context, OnViewHolderClick listener, Function<T, String> converter) {
        super();
        mContext = context;
        originalListener = listener;
        if (converter != null)
            toStringFilter = new ToStringFilter(converter);
    }

    protected abstract View createView(Context context, ViewGroup viewGroup, int viewType);

    protected abstract void bindView(T item, RecyclerviewViewHolder viewHolder);

    protected abstract int bindItemViewType(int position);

    protected abstract int bindItemId(int position);

    @Override
    public int getItemViewType(int position) {
        return bindItemViewType(position);
    }

    @Override
    public RecyclerviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new RecyclerviewViewHolder(createView(mContext, viewGroup, viewType), originalListener);
    }

    @Override
    public void onBindViewHolder(RecyclerviewViewHolder viewHolder, int position) {
        bindView(getItem(position), viewHolder);
    }

    @Override
    public long getItemId(int position) {
        return bindItemId(position);
    }

    @Override
    public int getItemCount() {
        if (arrayList == null) return 0;
        return arrayList.size();
    }

    public T getItem(int index) {
        return ((arrayList != null && index < arrayList.size()) ? arrayList.get(index) : null);
    }

    public Context getContext() {
        return mContext;
    }

    public void addAll(List<T> list) {
        if (list == null) return;
        originalList = list;
        arrayList = list;
        notifyDataSetChanged();
    }

    public void updateItem(int position, @NonNull T entity) {
        if (position > arrayList.size()) return;
        arrayList.set(position, entity);
        notifyItemChanged(position);
    }

    public void removeItem(int index) {
        if (index > arrayList.size()) return;
        arrayList.remove(index);
        notifyItemRemoved(index);
    }

    public void removeItem(@NonNull T entity) {
        arrayList.remove(entity);
        notifyDataSetChanged();
    }

    public List<T> getList() {
        return arrayList;
    }

    public void setClickListener(OnViewHolderClick listener) {
        originalListener = listener;
    }

    @Override
    public Filter getFilter() {
        return toStringFilter;
    }

    public static class RecyclerviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Map<Integer, View> mMapView;
        private OnViewHolderClick originalListener;

        public RecyclerviewViewHolder(View view, OnViewHolderClick listener) {
            super(view);
            mMapView = new HashMap<>();
            mMapView.put(0, view);
            originalListener = listener;

            if (originalListener != null)
                view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (originalListener != null)
                originalListener.onItemClick(view, getAdapterPosition());
        }

        public void initViewList(int[] idList) {
            for (int id : idList)
                initViewById(id);
        }

        public void initViewById(int id) {
            View view = (getView() != null ? getView().findViewById(id) : null);

            if (view != null)
                mMapView.put(id, view);
        }

        public View getView() {
            return getView(0);
        }

        public View getView(int id) {
            if (mMapView.containsKey(id))
                return mMapView.get(id);
            else
                initViewById(id);

            return mMapView.get(id);
        }
    }

    public class ToStringFilter extends Filter {

        Function<T, String> converter;
        private CharSequence lastConstrant;

        public ToStringFilter(Function<T, String> converter) {
            this.converter = converter;
        }

        public ToStringFilter() {
        }

        protected void notifyFilter() {
            filter(lastConstrant);
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            this.lastConstrant = constraint;
            FilterResults results = new FilterResults();

            if (Strings.isNullOrEmpty(constraint.toString())) {
                results.count = originalList.size();
                results.values = new ArrayList<T>(originalList);
                return results;
            }

            ArrayList<T> filterList = new ArrayList<T>();
            constraint = constraint.toString().toLowerCase();
            for (int i = 0; i < originalList.size(); i++) {
                if (converter != null) {
                    String apply = converter.apply(originalList.get(i));
                    if (apply.toLowerCase().contains(constraint)) {
                        filterList.add(originalList.get(i));
                    }

                } else if (originalList.get(i).toString().toLowerCase()
                        .contains(constraint.toString())) {
                    filterList.add(originalList.get(i));
                }

            }
            results.count = filterList.size();
            results.values = filterList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            arrayList = (List<T>) results.values;
            notifyDataSetChanged();
        }
    }
}
