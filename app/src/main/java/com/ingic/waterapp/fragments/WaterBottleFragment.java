package com.ingic.waterapp.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.ingic.waterapp.R;
import com.ingic.waterapp.entities.Product;
import com.ingic.waterapp.entities.SettingsEnt;
import com.ingic.waterapp.entities.cart.DataHelper;
import com.ingic.waterapp.entities.cart.MyCartModel;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.global.AppConstants;
import com.ingic.waterapp.global.WebServiceConstants;
import com.ingic.waterapp.helpers.TextViewHelper;
import com.ingic.waterapp.helpers.UIHelper;
import com.ingic.waterapp.ui.views.AnyTextView;
import com.ingic.waterapp.ui.views.TitleBar;
import com.ingic.waterapp.ui.views.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class WaterBottleFragment extends BaseFragment implements View.OnClickListener {

    Unbinder unbinder;
    @BindView(R.id.img_bottle_minus)
    ImageView btnMinus;
    @BindView(R.id.img_bottle_add)
    ImageView btnAdd;
    @BindView(R.id.tv_bottle_count)
    AnyTextView tvBottleCount;
    @BindView(R.id.tv_bottle_cost)
    AnyTextView tvCost;
    @BindView(R.id.tv_bottle_total)
    AnyTextView tvTotal;
    @BindView(R.id.tv_bottle_delivery_text)
    AnyTextView tvDeliveryText;
    @BindView(R.id.btn_bottle_addToCart)
    Button btnAddToCart;
    private int count = 0;
    private Product productObj;
    private int productId;
    private String productName;
    private float productAmount;
    private float totalAmount;

    //Realm
    private Realm realm;
    private int productQuantity = 0;

    public WaterBottleFragment() {
        // Required empty public constructor
    }

    public static WaterBottleFragment newInstance() {
        return new WaterBottleFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_water_bottle, container, false);
        if (getArguments() != null) {
            productObj = (Product) getArguments().getSerializable(AppConstants.PRODUCT_OBJ);
//            productQuantity = Util.getParsedInteger(productObj.getQuantity());
            productQuantity = getArguments().getInt(AppConstants.PRODUCT_QUANTITY);
            productId = productObj.getId();

            productAmount = Util.getDiscountedValue(Util.getParsedFloat(productObj.getProductAmount()), //After discount
                    Util.getParsedFloat(productObj.getPercentage()));
//            productAmount = Util.getParsedDouble(productObj.getProductAmount());
//            count = getArguments().getInt(AppConstants.PRODUCT_COUNT);
        }
        realm = Realm.getDefaultInstance();
        return view;
    }

    private void setListeners() {
        btnMinus.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnAddToCart.setOnClickListener(this);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        // TODO Auto-generated method stub
        super.setTitleBar(titleBar);

        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.water_bottle));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        serviceHelper.enqueueCall(webService.settings(prefHelper.getUser().getToken())
                , WebServiceConstants.getSetting);
        setData();
        setListeners();
    }

    private void setData() {
        count = productQuantity;

//        tvBottleCount.setText(String.format("%02d", productObj.getQuantity()));
        if (productQuantity != 0)
            tvBottleCount.setText(String.format("%02d", productQuantity));
        else
            tvBottleCount.setText("" + productQuantity);
        TextViewHelper.setText(tvCost, String.format ("%,.2f", productAmount) + " AED");
        /*If count = 0 then total = cost*/
        totalAmount = count > 1 ? (count * productAmount) : productAmount;
        TextViewHelper.setText(tvTotal, String.format ("%,.2f", totalAmount) + " AED");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDockActivity().getDrawerLayout() != null)
            getDockActivity().lockDrawer();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_bottle_add:
                updateCount(AppConstants.ADD);
                break;
            case R.id.img_bottle_minus:
                updateCount(AppConstants.MINUS);
                break;
            case R.id.btn_bottle_addToCart:
//                notImplemented();
                if (Util.doubleClickCheck()) {

                    count = Util.getParsedInteger(tvBottleCount.getText().toString());
                    if (count != 0) {
                        DataHelper.addToRealm(getDockActivity(), new MyCartModel(
                                productId, productObj.getProductName(), productObj.getProductImage(), productObj.getLiter(),
                                count,
                                productAmount));
                        getDockActivity().popFragment();
                    } else
                        UIHelper.showShortToastInCenter(getDockActivity(), getResources().getString(R.string.add_quantity));
                }
                break;
            default:
                break;
        }
    }

    private void updateCount(String action) {
        count = Util.getParsedInteger(tvBottleCount.getText().toString());
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

        /*Set data to fields*/
        tvBottleCount.setText(String.format("%02d", count));
                       /*If count = 0 then total = cost*/
        totalAmount = count > 1 ? (count * productAmount) : productAmount;
        TextViewHelper.setText(tvTotal, String.format ("%,.2f", totalAmount)  + " AED");


       /* count = Util.getParsedInteger(tvBottleCount.getText().toString());
        count++;
        tvBottleCount.setText(String.format("%02d", count)); //count
        *//*If count = 0 then total = cost*//*
        totalAmount = count > 1 ? (count * productAmount) : productAmount; //amount
        TextViewHelper.setText(tvTotal, String.valueOf(totalAmount) + "AED"); // Total Amount


        *//*-----*//*

        count = Util.getParsedInteger(tvBottleCount.getText().toString());
        if (count > 1) {
            count--;
            tvBottleCount.setText(String.format("%02d", count));
                       *//*If count = 0 then total = cost*//*
            totalAmount = count > 1 ? (count * productAmount) : productAmount;
            TextViewHelper.setText(tvTotal, String.valueOf(totalAmount) + "AED");
*/
    }


    /*private void addToRealm(int productId, String productName, String productImage,
                            String productLtr, String quantity, String totalAmount) {

        MyCartModel obj = new MyCartModel(productId, productName, productImage, productLtr, quantity, totalAmount);

        MyCartModel searchItem = realm.where(MyCartModel.class).equalTo(RealmConstants.PRODUCT_ID, productId).findFirst();
        if (searchItem == null) {
            obj.setId(DataHelper.getDbSize(realm) + System.currentTimeMillis());
        } else
            obj.setId(searchItem.getId());

        DataHelper.insertOrUpdate(realm, obj, getDockActivity());
         *//*===testing===*//*
        MyCartModel testResult = realm.where(MyCartModel.class)
                .equalTo(RealmConstants.PRODUCT_ID, productId).findFirst();


        UIHelper.showShortToastInCenter(getDockActivity(),
                "Db Id = " + testResult.getId() +
                        "\nName =" + testResult.getProductName() + "\nQuantity =" + testResult.getProductQuantity());


//        MyCartModel result = realm.where(MyCartModel.class)
//                .equalTo(RealmConstants.PRODUCT_ID, productId).findFirst();


//
//        realm.beginTransaction();
//
//        //if data is not exist
//        if (result == null) {
//            addData(productId, productName, productImage, productLtr,
//                    quantity,
//                    totalAmount);
//        } else {
//            updateData(productId, productName, productImage, productLtr,
//                    quantity,
//                    totalAmount, result);
//        }
//        *//*============*//*
//
//        realm.commitTransaction();
//
//          *//*===testing===*//*
//        MyCartModel testResult = realm.where(MyCartModel.class)
//                .equalTo(RealmConstants.PRODUCT_ID, productId).findFirst();
//
//
//        UIHelper.showShortToastInCenter(getDockActivity(),
//                "Db Id = "+testResult.getId()+
//                "\nName ="+testResult.getProductName()+"\nQuantity ="+testResult.getProductQuantity());


//        notifyDataSetChanged();
    }
*/
 /*   private void addData(int productId, String productName, String productImage,
                         String productLtr, String quantity, String totalAmount) {
        MyCartModel myCartModel = new MyCartModel();
        //myCartModel  .setId(RealmController.getInstance().getBooks().size() + 1);
        myCartModel.setId(RealmController.getInstance().getMyCartModels().size() + System.currentTimeMillis());
        myCartModel.setProductId(productId);
        myCartModel.setProductName(productName);
        myCartModel.setProductImage(productImage);
        myCartModel.setProductLtr(productLtr);
        myCartModel.setProductQuantity(quantity);
        myCartModel.setProductAmount(totalAmount);


        // Persist your data easily
//        realm.beginTransaction();
        realm.copyToRealm(myCartModel);
//        realm.close();


//        adapter.notifyDataSetChanged();

    }

    private void updateData(int productId, String productName, String productImage,
                            String productLtr, String quantity, String totalAmount, MyCartModel result) {

//        realm.beginTransaction();

        result.setProductQuantity(quantity);
        result.setProductAmount(totalAmount);

//        realm.commitTransaction();

//        adapter.notifyDataSetChanged();

    }*/

    @Override
    public void ResponseSuccess(Object result, String tag, String message) {
        switch (tag) {
            case WebServiceConstants.addToCart:
                UIHelper.showShortToastInCenter(getDockActivity(), getResources().getString(R.string.added_to_cart));
                getDockActivity().popFragment();
                break;
            case WebServiceConstants.getSetting:
                SettingsEnt settings = (SettingsEnt) result;
                TextViewHelper.setHtmlText(tvDeliveryText, settings.getCompanyTerm());
                break;
        }
    }

    @Override
    public void onDestroy() {
        realm.close();
        super.onDestroy();
    }


}
