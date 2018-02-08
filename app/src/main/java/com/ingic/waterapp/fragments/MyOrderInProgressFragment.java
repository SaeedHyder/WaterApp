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
import com.ingic.waterapp.entities.MyOrdersChildEntity;
import com.ingic.waterapp.entities.MyOrdersChildListEntity;
import com.ingic.waterapp.entities.MyOrdersParentEntity;
import com.ingic.waterapp.entities.myOrder.InProgressOrderEnt;
import com.ingic.waterapp.entities.myOrder.OrderProduct;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.global.AppConstants;
import com.ingic.waterapp.global.WebServiceConstants;
import com.ingic.waterapp.helpers.DateHelper;
import com.ingic.waterapp.interfaces.OnChildViewHolderItemClick;
import com.ingic.waterapp.ui.adapters.MyOrderApdater;
import com.ingic.waterapp.ui.views.TitleBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrderInProgressFragment extends BaseFragment implements OnChildViewHolderItemClick {
    @BindView(R.id.rv_expandable_inProgress)
    RecyclerView recyclerViewExpendable;
    Unbinder unbinder;
    private MyOrderApdater mAdapter;
    private String whichFragment;
    private List<InProgressOrderEnt> myOrderList;
    private int mChildPosition;

    public static final java.lang.String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

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

        if (whichFragment.equalsIgnoreCase(AppConstants.IN_PROGRESS_ORDER))
            serviceHelper.enqueueCall(webService.myOrderInProgress(prefHelper.getUser().getToken()),
                    WebServiceConstants.getMyOrders);
        else if (whichFragment.equalsIgnoreCase(AppConstants.DELIVERED_ORDER))
            serviceHelper.enqueueCall(webService.myOrderDelivered(prefHelper.getUser().getToken()),
                    WebServiceConstants.getMyOrders);
        initRecyclerView();

        return view;
    }

    private void initRecyclerView() {
//        MyOrdersChildListEntity childObj = new MyOrdersChildListEntity("Aquafina", "20", "5");
//        MyOrdersChildListEntity childObj1 = new MyOrdersChildListEntity("Masafi Co.", "25", "1");
//        MyOrdersChildListEntity childObj2 = new MyOrdersChildListEntity("Nestle Waters", "10", "3");
//        ArrayList<MyOrdersChildListEntity> childList = new ArrayList<>();
//        childList.add(childObj);
//        childList.add(childObj1);
//        childList.add(childObj2);
//        MyOrdersChildEntity entity1 =
//                new MyOrdersChildEntity(whichFragment,
//                        "27 Jan, 2017", "Morning", childList);
//        MyOrdersParentEntity parentEntity1 = new MyOrdersParentEntity("5698", "AED 50", Arrays.asList(entity1));
//        MyOrdersParentEntity parentEntity2 = new MyOrdersParentEntity("9988", "AED 60", Arrays.asList(entity1));
//        MyOrdersParentEntity parentEntity3 = new MyOrdersParentEntity("8989", "AED 80", Arrays.asList(entity1));
//
//        List<MyOrdersParentEntity> parentEntityList = Arrays.asList(parentEntity1, parentEntity2, parentEntity3);

//        mAdapter = new MyOrderApdater(getDockActivity(), parentEntityList);
        mAdapter = new MyOrderApdater(getDockActivity(), this);

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

    private List<MyOrdersParentEntity> getdata() {
        List<MyOrdersParentEntity> parentEntityList = new ArrayList<>();
        for (int i = 0; i < myOrderList.size(); i++) {
            ArrayList<MyOrdersChildListEntity> childList = new ArrayList<>();
            String companyName = myOrderList.get(i).getCompanyDetail().getFullName();
            List<OrderProduct> child = myOrderList.get(i).getOrderProduct();
            for (int j = 0; j < child.size(); j++) {
                MyOrdersChildListEntity childObj =
                        new MyOrdersChildListEntity(child.get(i).getProductDetail().getProductImage(),
                                companyName, child.get(j).getAmount(), child.get(j).getQuantity()); /*todo : include ltr too and image*/
                childList.add(childObj);
            }
            //For Date
            Date date = DateHelper.stringToDate(myOrderList.get(i).getCreatedAt(), DATE_TIME_FORMAT);
            Date dateFormatGMT = DateHelper.getDateInGMT(date);
            String getFormattedDate = DateHelper.getFormattedDate(dateFormatGMT);
//            String getFormattedTime = DateHelper.getFormattedTime(dateFormatGMT);
            //================================//
            MyOrdersChildEntity entity1 =
                    new MyOrdersChildEntity(whichFragment, myOrderList.get(i).getId(),
                            getFormattedDate, myOrderList.get(i).getTimeSlot(), childList);
            MyOrdersParentEntity parentEntity1 =
                    new MyOrdersParentEntity(String.valueOf(myOrderList.get(i).getId()),
                            "AED " + String.valueOf(myOrderList.get(i).getTotal()), Arrays.asList(entity1));

            parentEntityList.add(parentEntity1);

        }
        return parentEntityList;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mAdapter != null)
            mAdapter.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (mAdapter != null)
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

    @Override
    public void ResponseSuccess(Object result, String tag, String message) {
        switch (tag) {
            case WebServiceConstants.getMyOrders:
                myOrderList = (List<InProgressOrderEnt>) result;
                mAdapter.setParentList(getdata(), false);
                break;

            case WebServiceConstants.cancelOrder:
//                adapter= new
                mAdapter.notifyParentRemoved(mChildPosition);

                break;

        }
    }

    @Override
    public void onReorderClick(View view, int position, int orderId) {
        mChildPosition = 0;
        mChildPosition = position;
        serviceHelper.enqueueCall(webService.reorder(orderId, prefHelper.getUser().getToken()),
                WebServiceConstants.reorder);
    }

    @Override
    public void onCancelOrderClick(View view, int position, int orderId) {
        mChildPosition = 0;
        mChildPosition = position;
        serviceHelper.enqueueCall(webService.cancelOrder(orderId, prefHelper.getUser().getToken()),
                WebServiceConstants.cancelOrder);
    }
}
