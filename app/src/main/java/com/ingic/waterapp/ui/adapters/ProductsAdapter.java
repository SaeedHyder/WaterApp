package com.ingic.waterapp.ui.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingic.waterapp.R;
import com.ingic.waterapp.entities.Product;
import com.ingic.waterapp.entities.cart.DataHelper;
import com.ingic.waterapp.entities.cart.MyCartModel;
import com.ingic.waterapp.global.AppConstants;
import com.ingic.waterapp.interfaces.OnViewHolderClick;
import com.ingic.waterapp.ui.adapters.abstracts.RecyclerViewListAdapter;
import com.ingic.waterapp.ui.views.Util;
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
    protected void bindView(final Product item, RecyclerviewViewHolder viewHolder) {
        if (item != null) {

            final int position = viewHolder.getAdapterPosition();
            ImageView imgRibbin = (ImageView) viewHolder.getView(R.id.img_ribbin);
            ImageView btnAdd = (ImageView) viewHolder.getView(R.id.img_add);
            ImageView btnMinus = (ImageView) viewHolder.getView(R.id.img_minus);
            ImageView img_bottle = (ImageView) viewHolder.getView(R.id.img_bottle);
            TextView textDiscountTag = (TextView) viewHolder.getView(R.id.tv_waterBottle_discountText);
            final TextView tvOriginalAmount = (TextView) viewHolder.getView(R.id.tv_waterBottle_originalAmount);
            final TextView tvCurrentAmount = (TextView) viewHolder.getView(R.id.tv_waterBottle_currentAmount);
            TextView tv_waterBottle_name = (TextView) viewHolder.getView(R.id.tv_waterBottle_name);
            final TextView tvQuantity = (TextView) viewHolder.getView(R.id.tv_waterBottle_quantity);


            tv_waterBottle_name.setSelected(true);
            textDiscountTag.setSelected(true);
            if (item.getCouponName() != null && item.getCouponName().length() > 0) {
                imgRibbin.setVisibility(View.VISIBLE);
                textDiscountTag.setVisibility(View.VISIBLE);
                tvOriginalAmount.setVisibility(View.VISIBLE);
                textDiscountTag.setText(item.getCouponName());
                tvOriginalAmount.setText(item.getProductAmount());
                tvOriginalAmount.setPaintFlags(tvOriginalAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                imgRibbin.setVisibility(View.GONE);
                textDiscountTag.setVisibility(View.GONE);
                tvOriginalAmount.setVisibility(View.GONE);
            }

            if (item.getProductImage() != null && item.getProductImage().length() > 0) {
                Picasso.with(context)
                        .load(item.getProductImage())
                        .into(img_bottle);
            }
            float currentValue = Util.getDiscountedValue(Util.getParsedFloat(item.getProductAmount()), //After discount
                    Util.getParsedFloat(item.getPercentage()));
            tvCurrentAmount.setText(String.valueOf(currentValue)); //our save

           /* float amount = DataHelper.getProductAmount(item.getId());
            if (amount == 0) //if amount is 0 means realm have not this data obj so in this case we will set the data
                //coming from api
                tvCurrentAmount.setText(String.valueOf(currentValue)); //our save
            else
                tvCurrentAmount.setText(String.valueOf(amount)); //new data from api*/

            tv_waterBottle_name.setText(item.getProductName());
            tvQuantity.setText(item.getQuantity());

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Util.doubleClickCheck())
                        updateCount(AppConstants.ADD, item, tvOriginalAmount, tvCurrentAmount, tvQuantity);
//                    UIHelper.showShortToastInCenter(context, context.getResources().getString(R.string.will_be_implemented_in_beta));
                }
            });
            btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Util.doubleClickCheck())
                        updateCount(AppConstants.MINUS, item, tvOriginalAmount, tvCurrentAmount, tvQuantity);

//                    UIHelper.showShortToastInCenter(context, context.getResources().getString(R.string.will_be_implemented_in_beta));
                }
            });

        }
    }

//    @Override
//    protected void bindView(ServicesFragment.ServicesModel item, RecyclerviewViewHolder viewHolder) {
//        if (item != null) {
//            final int position = viewHolder.getAdapterPosition();
//            ImageView img = (ImageView) viewHolder.getView(R.id.img_itemNews);
//            TextView textTitle = (TextView) viewHolder.getView(R.id.tv_itemNews_title);
//            TextView textDesc = (TextView) viewHolder.getView(R.id.tv_itemNews_description);
//
//            textDesignation.setVisibility(View.VISIBLE);
//            img.setBackgroundResource(item.getPicture());
//            TextViewHelper.setText(textTitle,item.getName());
//            TextViewHelper.setText(textDesc,item.getDesc());
//        }
//    }

    // =======
    @Override
    protected int bindItemViewType(int position) {
        return 0;
    }

    @Override
    protected int bindItemId(int position) {
        return position;
    }

    private void updateCount(String action, Product item, TextView tvOriginalAmount, TextView tvCurrentAmount, TextView tvQuantity) {
        int count = Util.getParsedInteger(tvQuantity.getText().toString());
//        count = Util.getParsedInteger(tvQuantity.getText().toString());
        switch (action) {
            case AppConstants.ADD:
                count++;
                break;
            case AppConstants.MINUS:
                if (count > 1)
                    count--;
                break;
            default:
                break;
        }

           /*If count = 0 then total = cost*/
        float productAmount = Util.getParsedFloat(item.getProductAmount());
        float originalAmount = count > 1 ? (count * productAmount) : productAmount;
        float currentValue = Util.getDiscountedValue(productAmount, //After discount
                Util.getParsedFloat(item.getPercentage()));

//        currentValue = currentValue * count;
        /*Set data to fields*/
//        TextViewHelper.setText(tvOriginalAmount, String.valueOf(originalAmount));
//        TextViewHelper.setText(tvCurrentAmount, String.valueOf(currentValue));
//        tvQuantity.setText(String.format("%02d", count));
        tvQuantity.setText("" + count);


        count = Util.getParsedInteger(tvQuantity.getText().toString());
        DataHelper.addToRealm(context, new MyCartModel(
                item.getId(), item.getProductName(), item.getProductImage(), item.getLiter(),
                count,
                currentValue));

    }
}