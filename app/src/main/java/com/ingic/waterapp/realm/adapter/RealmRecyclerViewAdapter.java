package com.ingic.waterapp.realm.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollection;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmResults;
/**
 * The RealmBaseRecyclerAdapter class is an abstract utility class for binding RecyclerView UI elements to Realm data.
 * <p>
 * This adapter will automatically handle any updates to its data and call {@code notifyDataSetChanged()},
 * {@code notifyItemInserted()}, {@code notifyItemRemoved()} or {@code notifyItemRangeChanged(} as appropriate.
 * <p>
 * The RealmAdapter will stop receiving updates if the Realm instance providing the {@link OrderedRealmCollection} is
 * closed.
 * <p>
 * If the adapter contains Realm model classes with a primary key that is either an {@code int} or a {@code long}, call
 * {@code setHasStableIds(true)} in the constructor and override {@link #getItemId(int)} as described by the Javadoc in that method.
 *
 * @param <T> type of {@link RealmModel} stored in the adapter.
 * @param <S> type of RecyclerView.ViewHolder used in the adapter.
 * @see RecyclerView.Adapter#setHasStableIds(boolean)
 * @see RecyclerView.Adapter#getItemId(int)
 */

public abstract class RealmRecyclerViewAdapter<T extends RealmModel, S extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<S> {

    private final boolean hasAutoUpdates;
    private final boolean updateOnModification;
    private final OrderedRealmCollectionChangeListener listener;
    @Nullable
    private OrderedRealmCollection<T> adapterData;

    private OrderedRealmCollectionChangeListener createListener() {
        return new OrderedRealmCollectionChangeListener() {
            @Override
            public void onChange(Object collection, OrderedCollectionChangeSet changeSet) {
                // null Changes means the async query returns the first time.
                if (changeSet == null) {
                    notifyDataSetChanged();
                    return;
                }
                // For deletions, the adapter has to be notified in reverse order.
                OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
                for (int i = deletions.length - 1; i >= 0; i--) {
                    OrderedCollectionChangeSet.Range range = deletions[i];
                    notifyItemRangeRemoved(range.startIndex, range.length);
                }

                OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
                for (OrderedCollectionChangeSet.Range range : insertions) {
                    notifyItemRangeInserted(range.startIndex, range.length);
                }

                if (!updateOnModification) {
                    return;
                }

                OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
                for (OrderedCollectionChangeSet.Range range : modifications) {
                    notifyItemRangeChanged(range.startIndex, range.length);
                }
            }
        };
    }

    /**
     * This is equivalent to {@code RealmRecyclerViewAdapter(data, autoUpdate, true)}.
     *
     * @see #RealmRecyclerViewAdapter(OrderedRealmCollection, boolean, boolean)
     */
    public RealmRecyclerViewAdapter(@Nullable OrderedRealmCollection<T> data, boolean autoUpdate) {
        this(data, autoUpdate, true);
    }

    /**
     * @param data collection data to be used by this adapter.
     * @param autoUpdate when it is {@code false}, the adapter won't be automatically updated when collection data
     *                   changes.
     * @param updateOnModification when it is {@code true}, this adapter will be updated when deletions, insertions or
     *                             modifications happen to the collection data. When it is {@code false}, only
     *                             deletions and insertions will trigger the updates. This param will be ignored if
     *                             {@code autoUpdate} is {@code false}.
     */
    public RealmRecyclerViewAdapter(@Nullable OrderedRealmCollection<T> data, boolean autoUpdate,
                                    boolean updateOnModification) {
        if (data != null && !data.isManaged())
            throw new IllegalStateException("Only use this adapter with managed RealmCollection, " +
                    "for un-managed lists you can just use the BaseRecyclerViewAdapter");
        this.adapterData = data;
        this.hasAutoUpdates = autoUpdate;
        this.listener = hasAutoUpdates ? createListener() : null;
        this.updateOnModification = updateOnModification;
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (hasAutoUpdates && isDataValid()) {
            //noinspection ConstantConditions
            addListener(adapterData);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(final RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (hasAutoUpdates && isDataValid()) {
            //noinspection ConstantConditions
            removeListener(adapterData);
        }
    }

    @Override
    public int getItemCount() {
        //noinspection ConstantConditions
        return isDataValid() ? adapterData.size() : 0;
    }

    /**
     * Returns the item associated with the specified position.
     * Can return {@code null} if provided Realm instance by {@link OrderedRealmCollection} is closed.
     *
     * @param index index of the item.
     * @return the item at the specified position, {@code null} if adapter data is not valid.
     */
    @SuppressWarnings("WeakerAccess")
    @Nullable
    public T getItem(int index) {
        //noinspection ConstantConditions
        return isDataValid() ? adapterData.get(index) : null;
    }

    /**
     * Returns data associated with this adapter.
     *
     * @return adapter data.
     */
    @Nullable
    public OrderedRealmCollection<T> getData() {
        return adapterData;
    }

    /**
     * Updates the data associated to the Adapter. Useful when the query has been changed.
     * If the query does not change you might consider using the automaticUpdate feature.
     *
     * @param data the new {@link OrderedRealmCollection} to display.
     */
    @SuppressWarnings("WeakerAccess")
    public void updateData(@Nullable OrderedRealmCollection<T> data) {
        if (hasAutoUpdates) {
            if (isDataValid()) {
                //noinspection ConstantConditions
                removeListener(adapterData);
            }
            if (data != null) {
                addListener(data);
            }
        }

        this.adapterData = data;
        notifyDataSetChanged();
    }

    private void addListener(@NonNull OrderedRealmCollection<T> data) {
        if (data instanceof RealmResults) {
            RealmResults<T> results = (RealmResults<T>) data;
            //noinspection unchecked
            results.addChangeListener(listener);
        } else if (data instanceof RealmList) {
            RealmList<T> list = (RealmList<T>) data;
            //noinspection unchecked
            list.addChangeListener(listener);
        } else {
            throw new IllegalArgumentException("RealmCollection not supported: " + data.getClass());
        }
    }

    private void removeListener(@NonNull OrderedRealmCollection<T> data) {
        if (data instanceof RealmResults) {
            RealmResults<T> results = (RealmResults<T>) data;
            //noinspection unchecked
            results.removeChangeListener(listener);
        } else if (data instanceof RealmList) {
            RealmList<T> list = (RealmList<T>) data;
            //noinspection unchecked
            list.removeChangeListener(listener);
        } else {
            throw new IllegalArgumentException("RealmCollection not supported: " + data.getClass());
        }
    }

    private boolean isDataValid() {
        return adapterData != null && adapterData.isValid();
    }
}