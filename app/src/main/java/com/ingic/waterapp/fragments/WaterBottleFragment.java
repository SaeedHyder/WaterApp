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
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.helpers.UIHelper;
import com.ingic.waterapp.ui.views.AnyTextView;
import com.ingic.waterapp.ui.views.TitleBar;
import com.ingic.waterapp.ui.views.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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
    @BindView(R.id.btn_bottle_addToCart)
    Button btnAddToCart;
    private int count;

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
        titleBar.showBackButton();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.water_bottle));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        setListeners();
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
                count = Util.getParsedInteger(tvBottleCount.getText().toString());
                count++;
                tvBottleCount.setText(String.format("%02d", count));
                break;
            case R.id.img_bottle_minus:
                if (count > 1) {
                    count = Util.getParsedInteger(tvBottleCount.getText().toString());
                    count--;
                    tvBottleCount.setText(String.format("%02d", count));
                }
                break;
            case R.id.btn_bottle_addToCart:
                UIHelper.showShortToastInCenter(getDockActivity(), getResources().getString(R.string.added_to_cart));
                getDockActivity().popFragment();
                break;
            default:
                break;
        }
    }
}
