package com.ingic.waterapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.ingic.waterapp.R;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
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
        loadFragment(new HomeProductsFragment());
        return view;
    }

    private void setListeners() {
        btnProducts.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
        btnReview.setOnClickListener(this);

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
        titleBar.clearHeaderBackround();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_products:
                if (!btnProducts.isSelected()) {
                    btnProducts.setSelected(true);
                    btnAbout.setSelected(false);
                    btnReview.setSelected(false);
                    loadFragment(new HomeProductsFragment());

                }
                break;

            case R.id.btn_about:

                if (!btnAbout.isSelected()) {
                    btnProducts.setSelected(false);
                    btnAbout.setSelected(true);
                    btnReview.setSelected(false);
                    loadFragment(new HomeProductAboutFragment());

                }
                break;

            case R.id.btn_review:
                if (!btnReview.isSelected()) {
                    btnProducts.setSelected(false);
                    btnAbout.setSelected(false);
                    btnReview.setSelected(true);
                    loadFragment(new HomeProductReviewFragment());

                }
                break;

            default:
                break;
        }
    }
}

