package com.ingic.waterapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ingic.waterapp.R;
import com.ingic.waterapp.entities.CreateOrder;
import com.ingic.waterapp.entities.SettingsEnt;
import com.ingic.waterapp.entities.cart.MyCartModel;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.global.AppConstants;
import com.ingic.waterapp.global.WebServiceConstants;
import com.ingic.waterapp.helpers.SimpleDividerItemDecoration;
import com.ingic.waterapp.helpers.TextViewHelper;
import com.ingic.waterapp.helpers.UIHelper;
import com.ingic.waterapp.realm.adapter.MyRecyclerViewAdapter;
import com.ingic.waterapp.ui.views.AnyTextView;
import com.ingic.waterapp.ui.views.TitleBar;
import com.ingic.waterapp.ui.views.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends BaseFragment {
    @BindView(R.id.rv_cart)
    RecyclerView rvCart;

    @BindView(R.id.tv_cart_cost)
    AnyTextView tvCost;
    @BindView(R.id.tv_cart_serviceCharges)
    AnyTextView tvServiceCharges;
    @BindView(R.id.tv_cart_vatTax)
    AnyTextView tvTax;
    @BindView(R.id.tv_cart_total)
    AnyTextView tvTotal;
    @BindView(R.id.btn_cart_proceed)
    Button btnProceed;
    Unbinder unbinder;
    private Realm realm;
    private MyRecyclerViewAdapter adapter;

    List<MyCartModel> selectedListData = new ArrayList<>();
    private SettingsEnt settings;

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        serviceHelper.enqueueCall(webService.settings(prefHelper.getUser().getToken())
                , WebServiceConstants.getSetting);

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.doubleClickCheck()) {
                    if (selectedListData != null && selectedListData.size() > 0) {
                        String cost = tvCost.getText().toString();
                        String total = tvTotal.getText().toString();
                        CreateOrder order = new CreateOrder(settings.getCompany_id(), settings.getCompany_name(), cost,
                                settings.getServiceCharges(), settings.getVatTax(), total);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(AppConstants.CART_OBJ, order);
                        bundle.putParcelable(AppConstants.CART_SELECTED_LIST, Parcels.wrap(selectedListData));
                        ConfirmationFragment fragment = new ConfirmationFragment();
                        fragment.setArguments(bundle);

                        getDockActivity().replaceDockableFragment(fragment,
                                ConfirmationFragment.class.getSimpleName());
                    } else
                        UIHelper.showShortToastInCenter(getDockActivity(), getResources().getString(R.string.error_empty_list));
                }
            }
        });
    }

    private void setData() {
        if (settings != null) {
            TextViewHelper.setText(tvTax, settings.getVatTax());
            TextViewHelper.setText(tvServiceCharges, settings.getServiceCharges());
            TextViewHelper.setText(tvCost, "0.0");
            TextViewHelper.setText(tvTotal, "0.0");
        }
    }


    private void initRecyclerView() {
        realm = Realm.getDefaultInstance();
        setUpRecyclerView();
        rvCart.getLayoutManager().setMeasurementCacheEnabled(false);
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
        titleBar.hideButtons();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.cart));
        titleBar.showBackButton();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void setUpRecyclerView() {
        adapter = new MyRecyclerViewAdapter(realm.where(MyCartModel.class).findAll(),
                getDockActivity(), new MyRecyclerViewAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(MyCartModel item) {
                selectedListData.add(item);
                setData(selectedListData);
            }

            @Override
            public void onItemUncheck(MyCartModel item) {
                selectedListData.remove(item);
                setData(selectedListData);
            }
        });
        rvCart.setLayoutManager(new LinearLayoutManager(getDockActivity()));
        rvCart.setAdapter(adapter);
        rvCart.setHasFixedSize(true);
        rvCart.addItemDecoration(new SimpleDividerItemDecoration(getDockActivity()));

//        TouchHelperCallback touchHelperCallback = new TouchHelperCallback();
//        ItemTouchHelper touchHelper = new ItemTouchHelper(touchHelperCallback);
//        touchHelper.attachToRecyclerView(recyclerView);
    }

    private void setData(List<MyCartModel> selectedListData) {
        if (selectedListData != null && selectedListData.size() > 0 && settings != null) {
            float cost = 0, total = 0;

            List<MyCartModel> mlist = selectedListData;
            for (MyCartModel model : mlist) {
                cost = cost + model.getProductAmount();
            }
            total = cost + Util.getParsedFloat(settings.getVatTax()) + Util.getParsedFloat(settings.getServiceCharges());

            TextViewHelper.setText(tvCost, String.valueOf(cost));
            TextViewHelper.setText(tvTotal, String.valueOf(total));
        } else
            setData();

    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.getSetting:
                settings = (SettingsEnt) result;
                setData();
                break;
            default:
                break;
        }
    }
}

