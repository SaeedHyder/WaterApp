package com.ingic.waterapp.realm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingic.waterapp.R;
import com.ingic.waterapp.entities.cart.DataHelper;
import com.ingic.waterapp.entities.cart.MyCartModel;
import com.ingic.waterapp.helpers.DialogHelper;
import com.ingic.waterapp.helpers.ImageLoaderHelper;
import com.ingic.waterapp.helpers.TextViewHelper;
import com.ingic.waterapp.ui.views.Util;

import java.util.List;

import io.realm.OrderedRealmCollection;

public class MyRecyclerViewAdapter extends RealmRecyclerViewAdapter<MyCartModel, MyRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    List<MyCartModel> data;
    int size;
//    private OnCheckedChanged mListener;

    public interface OnItemCheckListener {
        void onItemCheck(MyCartModel item);

        void onItemUncheck(MyCartModel item);
    }

    @NonNull
    private OnItemCheckListener onItemClick;

    public MyRecyclerViewAdapter(OrderedRealmCollection<MyCartModel> data, Context context,
                                 @NonNull OnItemCheckListener onItemCheckListener) {
        super(data, true);
        this.context = context;
        this.data = data;
        this.size = data.size();
        this.onItemClick = onItemCheckListener;

        // Only set this if the model class has a primary key that is also a integer or long.
        // In that case, {@code getItemId(int)} must also be overridden to return the key.
        // See https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html#hasStableIds()
        // See https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html#getItemId(int)
        setHasStableIds(true);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (getItem(position) == null) return;
        final MyCartModel obj = getItem(position);
        holder.data = obj;
        float amount = obj.getProductAmount() * obj.getProductQuantity();
        ImageLoaderHelper.loadImageWithPicasso(context, obj.getProductImage(), holder.imgBottle);
        TextViewHelper.setText(holder.tvBottleName, obj.getProductName());
        TextViewHelper.setText(holder.tvBottleQuantity, "QTY : " + obj.getProductQuantity());
//        TextViewHelper.setText(holder.tvBottleLtr, obj.getProductLtr());
        TextViewHelper.setText(holder.tvBottleAmount, "AED " + String.format ("%,.2f", amount));
        TextViewHelper.setText(holder.tvBottleUnitAmount, "Unit Price : " + String.format ("%,.2f",obj.getProductAmount()) + " AED");

        obj.removeAllChangeListeners();
        holder.imgDlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.doubleClickCheck()) {

                    final DialogHelper dialogHelper=new DialogHelper(context);
                    dialogHelper.initDeleteItem(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DataHelper.deleteItemAsync(obj.getId());
                            size--;
                            notifyItemRemoved(position);


//                    removeAt(position);
                            notifyDataSetChanged();
                            onItemClick.onItemUncheck(obj);

                            dialogHelper.hideDialog();
                        }
                    });
                    dialogHelper.showDialog();


                }
               /* if (holder.checkBox.isChecked()) {
                    onItemClick.onItemUncheck(obj);
                }*/
            }
        });


       /* holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked()) {
                    onItemClick.onItemCheck(obj);
                } else {
                    onItemClick.onItemUncheck(obj);
                }
            }
        });*/

    }

    private void removeAt(int position) {
//        size = data.size();
//        size--;
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, size);
    }

    @Override
    public int getItemCount() {
        return size;
    }

    @Override
    public long getItemId(int index) {
        return getItem(index).getId();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public MyCartModel data;
        ImageView imgBottle, imgDlt;
        TextView tvBottleName, tvBottleQuantity, tvBottleAmount, /*tvBottleLtr ,*/
                tvBottleUnitAmount;
        CheckBox checkBox;

        MyViewHolder(View view) {
            super(view);
            imgBottle = (ImageView) view.findViewById(R.id.img_itemCart_bottle);
            tvBottleName = (TextView) view.findViewById(R.id.tv_itemCart_BottleName);
            tvBottleQuantity = (TextView) view.findViewById(R.id.tv_itemCart_quantity);
//            tvBottleLtr = (TextView) view.findViewById(R.id.tv_itemCart_UnitPrice);
            tvBottleAmount = (TextView) view.findViewById(R.id.tv_itemCart_TotalPrice);
            tvBottleUnitAmount = (TextView) view.findViewById(R.id.tv_itemCart_UnitPrice);
            imgDlt = (ImageView) view.findViewById(R.id.img_itemCart_dlt);
            checkBox = (CheckBox) view.findViewById(R.id.cb_itemCart);
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            itemView.setOnClickListener(onClickListener);
        }
    }
}