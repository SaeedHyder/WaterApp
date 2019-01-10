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
import android.widget.RelativeLayout;

import com.ingic.waterapp.R;
import com.ingic.waterapp.entities.CreateOrder;
import com.ingic.waterapp.entities.SettingsEnt;
import com.ingic.waterapp.entities.cart.DataHelper;
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

    @BindView(R.id.rl_cart)
    RelativeLayout rlCart;
    @BindView(R.id.tv_cart_cost)
    AnyTextView tvCost;
    @BindView(R.id.tv_cart_serviceCharges)
    AnyTextView tvServiceCharges;
    @BindView(R.id.tv_cart_vatTax)
    AnyTextView tvTax;
    @BindView(R.id.tv_cart_total)
    AnyTextView tvTotal;
    @BindView(R.id.tv_cart_deliveryText)
    AnyTextView tvDeliveryText;
    @BindView(R.id.btn_cart_proceed)
    Button btnProceed;
    Unbinder unbinder;
    @BindView(R.id.tv_cart_emptyCart)
    AnyTextView tvCartEmptyCart;
    @BindView(R.id.seperator)
    View seperator;
    @BindView(R.id.seperator1)
    View seperator1;
    @BindView(R.id.txt_service_charges)
    AnyTextView txtServiceCharges;
    @BindView(R.id.seperator2)
    View seperator2;
    @BindView(R.id.txt_vat_tax)
    AnyTextView txtVatTax;
    @BindView(R.id.seperator3)
    View seperator3;
    @BindView(R.id.seperator4)
    View seperator4;
    @BindView(R.id.rl_cart_parent)
    RelativeLayout rlCartParent;
    private Realm realm;
    private MyRecyclerViewAdapter adapter;

    List<MyCartModel> selectedListData = new ArrayList<>();
    private SettingsEnt settings;
    float cost = 0, total = 0, vatTax = 0;

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
        selectedListData = DataHelper.getRealmData();
        if (selectedListData.size() > 0) {
            rlCart.setVisibility(View.VISIBLE);
        } else
            rlCart.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rlCartParent.setVisibility(View.GONE);
        initRecyclerView();
        serviceHelper.enqueueCall(webService.settings(prefHelper.getUser().getToken())
                , WebServiceConstants.getSetting);

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.doubleClickCheck()) {
//                    if (selectedListData != null && selectedListData.size() > 0) {
//                    String cost = tvCost.getText().toString();
//                    String total = tvTotal.getText().toString();
                    if (settings != null) {
                        CreateOrder order = new CreateOrder(settings.getCompany_id(), settings.getCompany_name(),
                                String.valueOf(cost),
                                settings.getServiceCharges(), String.valueOf(vatTax), String.valueOf(total));
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(AppConstants.CART_OBJ, order);
                        bundle.putString(AppConstants.COMPANY_TERMS, settings.getCompanyTerm());
//                        bundle.putParcelable(AppConstants.CART_SELECTED_LIST, Parcels.wrap(selectedListData));
                        ConfirmationFragment fragment = new ConfirmationFragment();
                        fragment.setArguments(bundle);

                        getDockActivity().replaceDockableFragment(fragment,
                                ConfirmationFragment.class.getSimpleName());
                    } else
                        UIHelper.showShortToastInCenter(getDockActivity(), getResources().getString(R.string.no_internet_connection));
                }
            }
        });
    }

   /* private void setData() {
        if (settings != null) {
            TextViewHelper.setText(tvTax, settings.getVatTax());
            TextViewHelper.setText(tvServiceCharges, settings.getServiceCharges());
            TextViewHelper.setText(tvCost, "0.0");
            TextViewHelper.setText(tvTotal, "0.0");
        }
    }*/


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

    private void setUpRecyclerView() {
        adapter = new MyRecyclerViewAdapter(realm.where(MyCartModel.class).findAll(),
                getDockActivity(), new MyRecyclerViewAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(MyCartModel item) {
//                selectedListData.add(item);
//                setData(selectedListData);
            }

            @Override
            public void onItemUncheck(MyCartModel item) {
                selectedListData = DataHelper.getRealmData();
                if (selectedListData.size() > 0) {
                    if (rlCart.getVisibility() == View.GONE)
                        rlCart.setVisibility(View.VISIBLE);
                    setData();
                } else
                    rlCart.setVisibility(View.GONE);
            }
        });
        rvCart.setLayoutManager(new LinearLayoutManager(getDockActivity()));
        rvCart.setAdapter(adapter);
        rvCart.setHasFixedSize(false);
        rvCart.addItemDecoration(new SimpleDividerItemDecoration(getDockActivity()));

//        TouchHelperCallback touchHelperCallback = new TouchHelperCallback();
//        ItemTouchHelper touchHelper = new ItemTouchHelper(touchHelperCallback);
//        touchHelper.attachToRecyclerView(recyclerView);
    }

    private void setData() {
        if (selectedListData != null && selectedListData.size() > 0 && settings != null) {

            total = 0;
            cost = 0;
            vatTax = 0;
            List<MyCartModel> mlist = selectedListData;
            for (MyCartModel model : mlist) {
                cost = cost + (model.getProductQuantity() * model.getProductAmount());
            }
            vatTax = cost * (Util.getParsedFloat(settings.getVatTax()) / 100);

            if (settings.getVatTaxStatus() == 1 && settings.getServiceChargesStatus() == 1) {
                txtVatTax.setVisibility(View.VISIBLE);
                tvTax.setVisibility(View.VISIBLE);
                seperator3.setVisibility(View.VISIBLE);
                txtServiceCharges.setVisibility(View.VISIBLE);
                tvServiceCharges.setVisibility(View.VISIBLE);
                seperator2.setVisibility(View.VISIBLE);

                total = cost + vatTax + Util.getParsedFloat(settings.getServiceCharges());
            } else if (settings.getVatTaxStatus() == 0 && settings.getServiceChargesStatus() == 0) {
                txtVatTax.setVisibility(View.GONE);
                tvTax.setVisibility(View.GONE);
                seperator3.setVisibility(View.GONE);
                txtServiceCharges.setVisibility(View.GONE);
                tvServiceCharges.setVisibility(View.GONE);
                seperator2.setVisibility(View.GONE);

                total = cost;
            } else if (settings.getVatTaxStatus() == 1 && settings.getServiceChargesStatus() == 0) {
                txtVatTax.setVisibility(View.VISIBLE);
                tvTax.setVisibility(View.VISIBLE);
                seperator3.setVisibility(View.VISIBLE);
                txtServiceCharges.setVisibility(View.GONE);
                tvServiceCharges.setVisibility(View.GONE);
                seperator2.setVisibility(View.GONE);

                total = cost + vatTax;
            } else if (settings.getVatTaxStatus() == 0 && settings.getServiceChargesStatus() == 1) {
                txtVatTax.setVisibility(View.GONE);
                tvTax.setVisibility(View.GONE);
                seperator3.setVisibility(View.GONE);
                txtServiceCharges.setVisibility(View.VISIBLE);
                tvServiceCharges.setVisibility(View.VISIBLE);
                seperator2.setVisibility(View.VISIBLE);

                total = cost + Util.getParsedFloat(settings.getServiceCharges());
            }


            TextViewHelper.setText(tvTax, "AED " + vatTax);
            TextViewHelper.setText(tvCost, "AED " + String.valueOf(cost));
            TextViewHelper.setText(tvTotal, "AED " + String.valueOf(total));
            TextViewHelper.setText(tvServiceCharges, "AED " + settings.getServiceCharges());
            TextViewHelper.setHtmlText(tvDeliveryText, settings.getCompanyTerm());
        }/* else
            setData();*/

    }

    @Override
    public void ResponseSuccess(Object result, String tag, String message) {
        switch (tag) {
            case WebServiceConstants.getSetting:
                rlCartParent.setVisibility(View.VISIBLE);
                settings = (SettingsEnt) result;
                txtVatTax.setText("Vat Tax (%" + settings.getVatTax() + ")");
                setData();
                break;
            default:
                break;
        }
    }


}

