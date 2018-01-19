package com.ingic.waterapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.ingic.waterapp.R;
import com.ingic.waterapp.entities.CompanyDetails;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.global.AppConstants;
import com.ingic.waterapp.global.WebServiceConstants;
import com.ingic.waterapp.ui.views.AnyTextView;
import com.ingic.waterapp.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

//import com.google.android.gms.location.places.Place;


public class HomeFragment extends BaseFragment implements View.OnClickListener {

    Unbinder unbinder;
    @BindView(R.id.btn_products)
    Button btnProducts;
    @BindView(R.id.btn_about)
    Button btnAbout;
    @BindView(R.id.btn_review)
    Button btnReview;
    @BindView(R.id.home_container)
    FrameLayout frameLayout;

    boolean isViewAll = false;
    @BindView(R.id.tv_home_productName)
    AnyTextView tvHomeProductName;
    @BindView(R.id.tv_home_productCount)
    AnyTextView tvHomeProductCount;
    @BindView(R.id.tv_home_viewAll)
    AnyTextView tvHomeViewAll;

    String type_select = AppConstants.select_product;

    CompanyDetails companyDetails;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        btnProducts.setSelected(true);
        btnAbout.setSelected(false);
        btnReview.setSelected(false);

        setListeners();

        callService();

        return view;
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {

            case WebServiceConstants.getCompanyProductAboutReview:
                companyDetails = (CompanyDetails)result;
                setData();
                break;
        }
    }

    public void setData(){

        if(companyDetails != null && companyDetails.getName()!= null){
            tvHomeProductName.setText(companyDetails.getName());
            tvHomeProductCount.setText(companyDetails.getCount()+" Products");

            HomeProductsFragment homeProductsFragment = new HomeProductsFragment();
            Bundle orderBundle = new Bundle();
            orderBundle.putString(AppConstants.CompanyDetails, new Gson().toJson(companyDetails));
            homeProductsFragment.setArguments(orderBundle);
            loadFragment(homeProductsFragment);

        }else{
            callService();
        }

    }

    public void callService(){

        serviceHelper.enqueueCall(webService.getCompanyProductAboutReview(
                type_select,
                prefHelper.getUser().getToken()),
                WebServiceConstants.getCompanyProductAboutReview);

    }

    private void setListeners() {
        btnProducts.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
        btnReview.setOnClickListener(this);
        tvHomeViewAll.setOnClickListener(this);

    }

    private void loadFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.home_container, fragment)
                .commit();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showMenuButton();
        titleBar.showMenuButton();

        titleBar.clearHeaderBackround();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_products:
                if (!btnProducts.isSelected()) {
                    btnProducts.setSelected(true);
                    btnAbout.setSelected(false);
                    btnReview.setSelected(false);
                    setData();

                }
                break;

            case R.id.btn_about:

                if (!btnAbout.isSelected()) {
                    btnProducts.setSelected(false);
                    btnAbout.setSelected(true);
                    btnReview.setSelected(false);

                    HomeProductAboutFragment productAboutFragment = new HomeProductAboutFragment();
                    Bundle orderBundle = new Bundle();
                    orderBundle.putString(AppConstants.CompanyDetails, new Gson().toJson(companyDetails));
                    productAboutFragment.setArguments(orderBundle);

                    loadFragment(productAboutFragment);

                }
                break;

            case R.id.btn_review:
                if (!btnReview.isSelected()) {
                    btnProducts.setSelected(false);
                    btnAbout.setSelected(false);
                    btnReview.setSelected(true);

                    HomeProductReviewFragment productReviewFragment = new HomeProductReviewFragment();
                    Bundle orderBundle = new Bundle();
                    orderBundle.putString(AppConstants.CompanyDetails, new Gson().toJson(companyDetails));
                    productReviewFragment.setArguments(orderBundle);

                    loadFragment(productReviewFragment);

                }
                break;

            case R.id.tv_home_viewAll:

                if (isViewAll) {
                    //isViewAll = false;
                    type_select = AppConstants.select_product;
                } else {
                    //isViewAll = true;
                    type_select = AppConstants.all_product;
                }

                callService();

                break;

            default:
                break;
        }
    }
}

