package com.ingic.waterapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.waterapp.R;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.helpers.GridSpacingItemDecoration;
import com.ingic.waterapp.interfaces.OnViewHolderClick;
import com.ingic.waterapp.ui.adapters.ProductsAdapter;
import com.ingic.waterapp.ui.adapters.abstracts.RecyclerViewListAdapter;
import com.ingic.waterapp.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeProductsFragment extends BaseFragment implements OnViewHolderClick {

    @BindView(R.id.rv_homeProducts)
    RecyclerView rvProducts;
    Unbinder unbinder;
    RecyclerViewListAdapter adapter;

    public HomeProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_products, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
    }

    private void initRecyclerView() {
        adapter = new ProductsAdapter(getDockActivity(), this);
        rvProducts.setLayoutManager(new GridLayoutManager(getDockActivity(), 2));
        int spanCount = 2;
        int spacing = 2;
        rvProducts.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, false));
        rvProducts.setAdapter(adapter);
        ArrayList<projects> list = new ArrayList<>();
        list.add(new projects(R.drawable.bottle , "Water Bottle"));
        list.add(new projects(R.drawable.bottle , "Water Bottle"));
        list.add(new projects(R.drawable.bottle , "Water Bottle"));
        list.add(new projects(R.drawable.bottle , "Water Bottle"));
        list.add(new projects(R.drawable.bottle , "Water Bottle"));
        list.add(new projects(R.drawable.bottle , "Water Bottle"));
        adapter.addAll(list);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDockActivity().getDrawerLayout() != null)
            getDockActivity().lockDrawer();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
//        titleBar.hideButtons();
//        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.projects));
//        titleBar.setTitleBarTextColor(getResources().getColor(R.color.white));
//        titleBar.setTitleBarBackgroundColor(getResources().getColor(R.color.theme_green));
//        titleBar.showBackButton();
    }

    @Override
    public void onItemClick(View view, int position) {
        getDockActivity().replaceDockableFragment(WaterBottleFragment.newInstance(), WaterBottleFragment.class.getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public class projects{
        int image;
        String name;

        public projects(int image, String name) {
            this.image = image;
            this.name = name;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}