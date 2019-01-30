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
import com.ingic.waterapp.global.AppConstants;
import com.ingic.waterapp.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrdersFragment extends BaseFragment implements View.OnClickListener {

    Unbinder unbinder;
    @BindView(R.id.btn_myOrder_inProgress)
    Button btnInProgress;
    @BindView(R.id.btn_myOrder_delivered)
    Button btnDelivered;
    @BindView(R.id.myOrder_container)
    FrameLayout frameLayout;

    private static boolean isCancelled = false;

    public static MyOrdersFragment newInstance() {
        isCancelled = false;
        return new MyOrdersFragment();
    }

    public static MyOrdersFragment newInstance(boolean isCancelledkey) {
        isCancelled = isCancelledkey;
        return new MyOrdersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        unbinder = ButterKnife.bind(this, view);


        if (isCancelled) {
            btnInProgress.setSelected(false);
            btnDelivered.setSelected(true);

            MyOrderInProgressFragment fragment = new MyOrderInProgressFragment();
            Bundle bundle = new Bundle();
            bundle.putString(AppConstants.fragment, AppConstants.DELIVERED_ORDER);
            fragment.setArguments(bundle);
            loadFragment(fragment);

        } else {
            btnInProgress.setSelected(true);
            btnDelivered.setSelected(false);

            MyOrderInProgressFragment fragment = new MyOrderInProgressFragment();
            Bundle bundle = new Bundle();
            bundle.putString(AppConstants.fragment, AppConstants.IN_PROGRESS_ORDER);
            fragment.setArguments(bundle);
            loadFragment(fragment);

        }
        setListeners();
        return view;
    }

    private void setListeners() {
        btnInProgress.setOnClickListener(this);
        btnDelivered.setOnClickListener(this);
    }

    private void loadFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.myOrder_container, fragment)
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
        titleBar.showBackButton();
        titleBar.clearHeaderBackround();
        titleBar.setSubHeading(getResources().getString(R.string.my_order));
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_myOrder_inProgress:
                if (!btnInProgress.isSelected()) {
                    btnInProgress.setSelected(true);
                    btnDelivered.setSelected(false);
                    MyOrderInProgressFragment fragment = new MyOrderInProgressFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(AppConstants.fragment, AppConstants.IN_PROGRESS_ORDER);
                    fragment.setArguments(bundle);
                    loadFragment(fragment);

                }
                break;

            case R.id.btn_myOrder_delivered:

                if (!btnDelivered.isSelected()) {
                    btnInProgress.setSelected(false);
                    btnDelivered.setSelected(true);

                    MyOrderInProgressFragment fragment = new MyOrderInProgressFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(AppConstants.fragment, AppConstants.DELIVERED_ORDER);
                    fragment.setArguments(bundle);
                    loadFragment(fragment);

                }
                break;

            default:
                break;
        }
    }

}

