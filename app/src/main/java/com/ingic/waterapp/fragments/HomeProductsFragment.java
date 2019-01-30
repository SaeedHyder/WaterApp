package com.ingic.waterapp.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.waterapp.R;
import com.ingic.waterapp.entities.CompanyDetails;
import com.ingic.waterapp.entities.Product;
import com.ingic.waterapp.entities.cart.DataHelper;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.global.AppConstants;
import com.ingic.waterapp.helpers.GridSpacingItemDecoration;
import com.ingic.waterapp.interfaces.OnViewHolderClick;
import com.ingic.waterapp.retrofit.GsonFactory;
import com.ingic.waterapp.ui.adapters.ProductsAdapter;
import com.ingic.waterapp.ui.adapters.abstracts.RecyclerViewListAdapter;
import com.ingic.waterapp.ui.views.AnyTextView;
import com.ingic.waterapp.ui.views.TitleBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeProductsFragment extends BaseFragment implements OnViewHolderClick {

    Unbinder unbinder;
    @BindView(R.id.rv_homeProducts)
    RecyclerView rvProducts;
    RecyclerViewListAdapter adapter;
    CompanyDetails companyDetails;
    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;


    //Realm
//    private Realm realm;


    public HomeProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        realm = Realm.getDefaultInstance();
       /* if (getArguments() != null) {
            companyDetails = GsonFactory.getConfiguredGson().fromJson(getArguments().getString(AppConstants.CompanyDetails), CompanyDetails.class);
        }

        setQuantity();*/


    }

    private void setQuantity() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (companyDetails != null) {
                    List<Product> products = companyDetails.getProduct();
                    for (int i = 0; i < products.size(); i++) {
                        Product obj = products.get(i);
                        obj.setQuantity(String.valueOf(DataHelper.getProductQuantity(products.get(i).getId())));
                        products.set(i, obj);
                    }
                    companyDetails.setProduct(products);
                }
                if (companyDetails != null && companyDetails.getProduct() != null && companyDetails.getProduct().size() > 0) {
                    rvProducts.setVisibility(View.VISIBLE);
                    txtNoData.setVisibility(View.GONE);

                    initRecyclerView(companyDetails.getProduct());
                } else {
                    if (rvProducts != null) {
                        rvProducts.setVisibility(View.GONE);
                    }
                    txtNoData.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_products, container, false);

        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new ProductsAdapter(getDockActivity(), this);
        rvProducts.setLayoutManager(new GridLayoutManager(getDockActivity(), 2));
        int spanCount = 2;
        int spacing = 1;
        rvProducts.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, false));
        rvProducts.setAdapter(adapter);

      /*  if (companyDetails != null && companyDetails.getProduct().size() > 0) {
            initRecyclerView(companyDetails.getProduct());
        }*/

        if (getArguments() != null) {
            companyDetails = GsonFactory.getConfiguredGson().fromJson(getArguments().getString(AppConstants.CompanyDetails), CompanyDetails.class);
        }

        setQuantity();


    }

    private void initRecyclerView(List<Product> products) {

        /*List<projects> list = new ArrayList<>();

        list.add(new projects(R.drawable.bottle, "Water Bottle"));
        list.add(new projects(R.drawable.bottle, "Water Bottle"));
        list.add(new projects(R.drawable.bottle, "Water Bottle"));
        list.add(new projects(R.drawable.bottle, "Water Bottle"));
        list.add(new projects(R.drawable.bottle, "Water Bottle"));
        list.add(new projects(R.drawable.bottle, "Water Bottle"));*/

        adapter.addAll(products);

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

       /* if (Util.doubleClickCheck()) {
            if (prefHelper.getUser() != null) {
                Product productDetail = companyDetails.getProduct().get(position);
                int quantity = DataHelper.getProductQuantity(productDetail.getId());
                WaterBottleFragment fragment = new WaterBottleFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstants.PRODUCT_OBJ, productDetail);
//                int productId = companyDetails.getProduct().get(position).getId();
                bundle.putInt(AppConstants.PRODUCT_QUANTITY, quantity);
//                bundle.putDouble(AppConstants.PRODUCT_AMOUNT, Util.getParsedDouble(productAmount));
                fragment.setArguments(bundle);
                getDockActivity().replaceDockableFragment(fragment, WaterBottleFragment.class.getSimpleName());
            } else
                UIHelper.showShortToastInCenter(getDockActivity(), getResources().getString(R.string.please_login));
        }*/
    }

    public class projects {
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