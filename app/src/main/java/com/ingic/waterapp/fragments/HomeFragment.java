package com.ingic.waterapp.fragments;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.google.gson.Gson;
import com.ingic.waterapp.R;
import com.ingic.waterapp.entities.CompanyDetails;
import com.ingic.waterapp.entities.CompanyEnt;
import com.ingic.waterapp.entities.NotificationCountEnt;
import com.ingic.waterapp.entities.cart.DataHelper;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.global.AppConstants;
import com.ingic.waterapp.global.WebServiceConstants;
import com.ingic.waterapp.helpers.DialogHelper;
import com.ingic.waterapp.helpers.UIHelper;
import com.ingic.waterapp.ui.views.AnyTextView;
import com.ingic.waterapp.ui.views.TitleBar;
import com.ingic.waterapp.ui.views.Util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    boolean isViewAll = false;
    @BindView(R.id.tv_home_productName)
    AnyTextView tvHomeProductName;
    @BindView(R.id.tv_home_productCount)
    AnyTextView tvHomeProductCount;
    @BindView(R.id.tv_home_viewAll)
    AnyTextView tvHomeViewAll;

    String type_select = AppConstants.select_product;

    CompanyDetails companyDetails;
    @BindView(R.id.img_home_leftArrow)
    ImageView imgLeftArrow;
    @BindView(R.id.img_home_rightArrow)
    ImageView imgRightArrow;
    @BindView(R.id.ll_arrows)
    LinearLayout llArrows;
    @BindView(R.id.middle)
    RelativeLayout middle;
    private int notificationCount = 0, cartCount = 0;
    List<CompanyDetails> companyList;
    private int index = 0;

    List<CompanyEnt> companyEnts;
    int companyId = 0;
    String companyName;

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
        printKeyHash(getDockActivity());
        unbinder = ButterKnife.bind(this, view);

        btnProducts.setSelected(true);
        btnAbout.setSelected(false);
        btnReview.setSelected(false);
        loadFragment(new HomeProductsFragment());
        setListeners();

        callService();

        cartCount = DataHelper.getTotalQuantities();

        return view;
    }

    @Override
    public void ResponseSuccess(Object result, String tag, String message) {
        switch (tag) {
            case WebServiceConstants.getCompanyProductAboutReview:
                companyList = (List<CompanyDetails>) result;
                if (companyList != null && companyList.size() > 0 && companyList.get(0) != null) {
                    index = 0;
                    if (companyList.size() == 1) {
                        imgLeftArrow.setVisibility(View.GONE);
                        imgRightArrow.setVisibility(View.GONE);
                    }
                    companyDetails = companyList.get(0);
                    if (!btnProducts.isSelected()) {
                        btnProducts.setSelected(true);
                        btnAbout.setSelected(false);
                        btnReview.setSelected(false);
                    }
                    setData();
                }
                break;
            case WebServiceConstants.getNotificationCount:
                NotificationCountEnt obj = (NotificationCountEnt) result;
                notificationCount = obj.getCount();
                hitBroadCast(notificationCount);
                break;

            case WebServiceConstants.getCompanies:
                companyId = 0;
                companyEnts = (List<CompanyEnt>) result;
                break;
            case WebServiceConstants.changeVendor:
                UIHelper.showShortToastInCenter(getDockActivity(), message);
                if (prefHelper.getUser() != null) {
                    serviceHelper.enqueueCall(webService.getCompanyProductAboutReview(
                            AppConstants.Normal,
                            type_select,
                            prefHelper.getUser().getToken()),
                            WebServiceConstants.getCompanyProductAboutReview);

                }
                break;
//                openDialog();
        }

    }

    private void hitBroadCast(int count) {
        Intent intent = new Intent();
        intent.setAction(AppConstants.UNREAD_NOTICATION_COUNT_BROADCAST);
        intent.putExtra(AppConstants.UNREAD_NOTICATION_COUNT, count);
        getDockActivity().sendBroadcast(intent);
    }

    public void setData() {
        if (companyDetails != null && companyDetails.getName() != null) {
            tvHomeProductName.setText(companyDetails.getName());
            tvHomeProductCount.setText(companyDetails.getCount() + " Products");

            HomeProductsFragment homeProductsFragment = new HomeProductsFragment();
            Bundle orderBundle = new Bundle();
            orderBundle.putString(AppConstants.CompanyDetails, new Gson().toJson(companyDetails));
            homeProductsFragment.setArguments(orderBundle);
            loadFragment(homeProductsFragment);

        } else {
            callService();
        }

    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
//        super.onSaveInstanceState(outState);
//    }

    public void callService() {

        if (prefHelper.getUser() != null) {
            serviceHelper.enqueueCall(webService.getCompanyProductAboutReview(
                    AppConstants.Normal,
                    type_select,
                    prefHelper.getUser().getToken()),
                    WebServiceConstants.getCompanyProductAboutReview);

            /*For notification notificationCount*/
            serviceHelper.enqueueCall(webService.getUnreadNotificationsCount(prefHelper.getUser().getToken()),
                    WebServiceConstants.getNotificationCount);

            /*For vendor */
            serviceHelper.enqueueCall(webService.getCompany(),
                    WebServiceConstants.getCompanies);
        } else {

            serviceHelper.enqueueCall(webService.getCompanyProductAboutReview(
                    AppConstants.Guest,
                    type_select,
                    prefHelper.getGuestTOKEN()),
                    WebServiceConstants.getCompanyProductAboutReview);

        }


    }

    private void setListeners() {
        btnProducts.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
        btnReview.setOnClickListener(this);
        tvHomeViewAll.setOnClickListener(this);

    }

    private void loadFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.home_container, fragment)
                .commitAllowingStateLoss();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*check wheater profile and vendor is selected or not */
        if (prefHelper.getUser() != null)
            if ((prefHelper.getUser().getLocation() != null && prefHelper.getUser().getLocation().isEmpty()) ||
                    (prefHelper.getUser().getMobileNo() != null && prefHelper.getUser().getMobileNo().isEmpty())) {
                openProfileDialog();

            } else if (prefHelper.getUser().getCompanyId() != null && prefHelper.getUser().getCompanyId().isEmpty()) {
                openVenderDialog();
            }
    }


    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showMenuButton();
        titleBar.showMenuButton();
        titleBar.setCartCount(cartCount);

        titleBar.clearHeaderBackround();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_products:
                if (Util.doubleClickCheck()) {
                    if (!btnProducts.isSelected()) {
                        btnProducts.setSelected(true);
                        btnAbout.setSelected(false);
                        btnReview.setSelected(false);
                        setData();

                    }
                }
                break;

            case R.id.btn_about:
                if (Util.doubleClickCheck()) {
                    if (!btnAbout.isSelected()) {
                        btnProducts.setSelected(false);
                        btnAbout.setSelected(true);
                        btnReview.setSelected(false);

                        HomeProductAboutFragment productAboutFragment = new HomeProductAboutFragment();
                        Bundle orderBundle = new Bundle();
                        orderBundle.putString(AppConstants.CompanyDetails, new Gson().toJson(companyDetails));
                        productAboutFragment.setArguments(orderBundle);

                        loadFragment(productAboutFragment);

                    }
                }
                break;

            case R.id.btn_review:
                if (Util.doubleClickCheck()) {
                    if (!btnReview.isSelected()) {
                        btnProducts.setSelected(false);
                        btnAbout.setSelected(false);
                        btnReview.setSelected(true);

                        HomeProductReviewFragment productReviewFragment = new HomeProductReviewFragment();
                        Bundle orderBundle = new Bundle();
                        orderBundle.putString(AppConstants.CompanyDetails, new Gson().toJson(companyDetails));
                        productReviewFragment.setArguments(orderBundle);
                        loadFragment(productReviewFragment);

                    }
                }
                break;

            case R.id.tv_home_viewAll:
                if (Util.doubleClickCheck()) {
                    if (isViewAll) {
                        //isViewAll = false;
                        type_select = AppConstants.select_product;
                    } else {
                        //isViewAll = true;
                        type_select = AppConstants.all_product;
                    }

                    callService();
                }
                break;

            default:
                break;
        }
    }

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @OnClick({R.id.img_home_leftArrow, R.id.img_home_rightArrow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_home_leftArrow:
                if (index > 0) {
                    index--;
                    companyDetails = companyList.get(index);
                    if (!btnProducts.isSelected()) {
                        btnProducts.setSelected(true);
                        btnAbout.setSelected(false);
                        btnReview.setSelected(false);
                    }
                    setData();
                }
                break;
            case R.id.img_home_rightArrow:
                if (companyList.size() - 1 > index) {
                    index++;
                    companyDetails = companyList.get(index);
                    if (!btnProducts.isSelected()) {
                        btnProducts.setSelected(true);
                        btnAbout.setSelected(false);
                        btnReview.setSelected(false);
                    }
                    setData();
                }
                break;
        }
    }

    public void openProfileDialog() {

        final DialogHelper dialog = new DialogHelper(getDockActivity());
        dialog.initProfile(R.layout.dialog_alert,
                getResources().getString(R.string.message_profile),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { //right click //proceed
                        dialog.hideDialogProfile();
                        getDockActivity().replaceDockableFragment(MyProfileFragment.newInstance(SideMenuFragment.newInstance()),
                                MyProfileFragment.class.getSimpleName());

                    }
                }, new View.OnClickListener() {
                    //left click // close
                    @Override
                    public void onClick(View v) {
                        dialog.hideDialogProfile();
                    }
                });
        dialog.setCancelableProfile(false);
        dialog.showDialogProfile();


       /* final Dialog dialog = DialogFactory.createCustomDialog(getDockActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                finalDialog.dismiss();
                getDockActivity().replaceDockableFragment(MyProfileFragment.newInstance(SideMenuFragment.newInstance()),
                        MyProfileFragment.class.getSimpleName());

            }
        }, getResources().getString(R.string.message_profile));

        dialog.show();*/
    }

    public void openVenderDialog() {

        final DialogHelper dialog = new DialogHelper(getDockActivity());
        dialog.initProfile(R.layout.dialog_alert,
                getResources().getString(R.string.message_vendor),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { //right click //proceed
                        dialog.hideDialogProfile();
                        openDialog(); // Change vendor

                    }
                }, new View.OnClickListener() {
                    //left click // close
                    @Override
                    public void onClick(View v) {
                        dialog.hideDialogProfile();
                    }
                });
        dialog.setCancelableProfile(false);
        dialog.showDialogProfile();

       /* final Dialog dialog = DialogFactory.createCustomDialog(getDockActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(); // Change vendor
            }
        }, getResources().getString(R.string.message_vendor));

        dialog.show();*/

    }

    private void openDialog() {
        if (companyEnts != null) {
            final String[] items = new String[companyEnts.size()];
            for (int i = 0; i < companyEnts.size(); i++) {
                items[i] = companyEnts.get(i).getFullName();
            }
            selectVendorActionSheetDialog(items);
        } else {
            serviceHelper.enqueueCall(webService.getCompany(),
                    WebServiceConstants.getCompanies);
        }
    }

    private void selectVendorActionSheetDialog(final String[] stringItems) {
//        final String[] stringItems = {getResources().getString(R.string.morning),
//                getResources().getString(R.string.afternoon)};
        final ActionSheetDialog dialog1 = new ActionSheetDialog(getDockActivity(), stringItems, null);
        dialog1.title(getResources().getString(R.string.select_supplier))
                .titleTextSize_SP(14.5f)
                .cancelText(getResources().getString(android.R.string.cancel))
                .show();

        dialog1.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                companyId = companyEnts.get(position).getId();
                companyName = stringItems[position];
//                TextViewHelper.setText(tvSelectSupplier, stringItems[position]);
                callService(companyId);
                dialog1.dismiss();
            }

            private void callService(int companyId) {
                serviceHelper.enqueueCall(webService.changeVendor(companyId, prefHelper.getUser().getToken()),
                        WebServiceConstants.changeVendor);
            }
        });
    }
}

