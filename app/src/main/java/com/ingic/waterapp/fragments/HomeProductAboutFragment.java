package com.ingic.waterapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.waterapp.R;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.ui.views.TitleBar;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeProductAboutFragment extends BaseFragment{

    Unbinder unbinder;

    public HomeProductAboutFragment() {
        // Required empty public constructor
    }

    public static HomeProductAboutFragment newInstance() {
        return new HomeProductAboutFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_product_about, container, false);
        return view;
    }


    @Override
    public void setTitleBar(TitleBar titleBar) {
        // TODO Auto-generated method stub
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDockActivity().getDrawerLayout() != null)
            getDockActivity().lockDrawer();
    }
}
