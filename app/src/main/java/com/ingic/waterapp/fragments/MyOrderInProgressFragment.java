package com.ingic.waterapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.waterapp.R;
import com.ingic.waterapp.entities.MyProjectsChildEntity;
import com.ingic.waterapp.entities.MyProjectsParentEntity;
import com.ingic.waterapp.entities.MyprojectsChildListEntity;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.global.AppConstants;
import com.ingic.waterapp.ui.adapters.MyOrderApdater;
import com.ingic.waterapp.ui.views.TitleBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrderInProgressFragment extends BaseFragment {
    @BindView(R.id.rv_expandable_inProgress)
    RecyclerView recyclerViewExpendable;
    Unbinder unbinder;
    private MyOrderApdater mAdapter;
    private String whichFragment;

    public static MyOrderInProgressFragment newInstance() {
        return new MyOrderInProgressFragment();
    }

    public MyOrderInProgressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_order_in_progress, container, false);
        unbinder = ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        whichFragment = bundle.getString(AppConstants.fragment);
        initRecyclerView();
        return view;
    }

    private void initRecyclerView() {
        MyprojectsChildListEntity childObj = new MyprojectsChildListEntity("Aquafina", "20", "5");
        MyprojectsChildListEntity childObj1 = new MyprojectsChildListEntity("Masafi Co.", "25", "1");
        MyprojectsChildListEntity childObj2 = new MyprojectsChildListEntity("Nestle Waters", "10", "3");
        ArrayList<MyprojectsChildListEntity> childList = new ArrayList<>();
        childList.add(childObj);
        childList.add(childObj1);
        childList.add(childObj2);
        MyProjectsChildEntity entity1 =
                new MyProjectsChildEntity(whichFragment,
                        "27 Jan, 2017", "Morning", childList);
        MyProjectsParentEntity parentEntity1 = new MyProjectsParentEntity("5698", "AED 50", Arrays.asList(entity1));
        MyProjectsParentEntity parentEntity2 = new MyProjectsParentEntity("9988", "AED 60", Arrays.asList(entity1));
        MyProjectsParentEntity parentEntity3 = new MyProjectsParentEntity("8989", "AED 80", Arrays.asList(entity1));

        List<MyProjectsParentEntity> parentEntityList = Arrays.asList(parentEntity1, parentEntity2, parentEntity3);

        mAdapter = new MyOrderApdater(getDockActivity(), parentEntityList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getDockActivity());

        // RecyclerView has some built in animations to it, using the DefaultItemAnimator.
        // Specifically when you call notifyItemChanged() it does a fade animation for the changing
        // of the data in the ViewHolder. If you would like to disable this you can use the following:
        RecyclerView.ItemAnimator animator = recyclerViewExpendable.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }

//        recyclerViewExpendable.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        recyclerViewExpendable.setAdapter(mAdapter);
        recyclerViewExpendable.setLayoutManager(layoutManager);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mAdapter.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mAdapter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.my_order));
        titleBar.showBackButton();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
