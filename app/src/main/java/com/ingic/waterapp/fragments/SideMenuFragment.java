package com.ingic.waterapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.ingic.waterapp.R;
import com.ingic.waterapp.activities.MainActivity;
import com.ingic.waterapp.entities.CompanyEnt;
import com.ingic.waterapp.entities.UserEnt;
import com.ingic.waterapp.entities.cart.DataHelper;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.global.WebServiceConstants;
import com.ingic.waterapp.helpers.DialogHelper;
import com.ingic.waterapp.helpers.SimpleDividerItemDecoration;
import com.ingic.waterapp.helpers.UIHelper;
import com.ingic.waterapp.interfaces.OnViewHolderClick;
import com.ingic.waterapp.interfaces.ProfileUpdateListener;
import com.ingic.waterapp.ui.adapters.SideMenuAdapter;
import com.ingic.waterapp.ui.adapters.abstracts.RecyclerViewListAdapter;
import com.ingic.waterapp.ui.views.AnyTextView;
import com.ingic.waterapp.ui.views.TitleBar;
import com.ingic.waterapp.ui.views.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SideMenuFragment extends BaseFragment implements OnViewHolderClick, ProfileUpdateListener {


    private Unbinder unbinder;
    @BindView(R.id.rl_sideMenu)
    RelativeLayout rlParent;
    @BindView(R.id.img_sideMenu_profileImage)
    ImageView imgProfile;
    @BindView(R.id.tv_sideMenu_profileName)
    AnyTextView tvProfileName;
    @BindView(R.id.rv_sideMenu)
    RecyclerView rvSideMenu;
    private RecyclerViewListAdapter adapter;
    private List<String> sideMenuList = new ArrayList<>();

    private String[] userMenuList = {"Home", "Order History", "Company List", "Change Vendor", "About", "Contact us", "Logout"};
    private String[] guestMenuList = {"Home", "Company List", "About", "Contact us", "Login"};
    private int loginType;

    List<CompanyEnt> companyEnts;
    int companyId = 0;
    String companyName;

    public SideMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new SideMenuAdapter(getDockActivity(), this);
    }

    public static SideMenuFragment newInstance() {
        return new SideMenuFragment();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sidemenu, container, false);
        unbinder = ButterKnife.bind(this, view);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getDockActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = (int) (displayMetrics.widthPixels * 0.72f);

        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.width = width;
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        rlParent.setLayoutParams(params);

        loginType = prefHelper.getLoginTYpe();

        rvSideMenu.setLayoutManager(new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false));
        rvSideMenu.addItemDecoration(new SimpleDividerItemDecoration(getDockActivity()));
        rvSideMenu.setAdapter(adapter);
        bindData();
//        setTop();
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.doubleClickCheck()) {
                    if (prefHelper.getUser() != null) {
                        getDockActivity().closeResideMenu();
                        getDockActivity().replaceDockableFragment(MyProfileFragment.newInstance(SideMenuFragment.this),
                                MyProfileFragment.class.getSimpleName());
                    } else
                        UIHelper.showShortToastInCenter(getDockActivity(), getResources().getString(R.string.please_login));

                }
            }
        });
        return view;
    }


    private void bindData() {
//        if (loginType == 0) {
//            for (int i = 0; i < nonUserIcons.length; i++)
//                sideMenuList.add(new SideMenuModel(nonUserIcons[i], nonUserList[i]));
//        } else if (loginType == 1) {
//            for (int i = 0; i < userMenuIcons.length; i++)
//                sideMenuList.add(new SideMenuModel(userMenuIcons[i], userMenuList[i]));
//        }


//        if (prefHelper.getGuestTOKEN() == null) {
//            Collections.addAll(sideMenuList, userMenuList);
//        } else
//            Collections.addAll(sideMenuList, guestMenuList);
//        adapter.addAll(sideMenuList);


        if (prefHelper.getUser() != null) {

            tvProfileName.setText(prefHelper.getUser().getFullName());

            if (prefHelper.getUser().getProfileImage() != null && prefHelper.getUser().getProfileImage().length() > 0) {
                Picasso.with(getDockActivity())
                        .load(prefHelper.getUser().getProfileImage())
                        .into(imgProfile);
            }
            Collections.addAll(sideMenuList, userMenuList);
        } else if (prefHelper.getGuestTOKEN() != null) {
            imgProfile.setImageResource(R.drawable.user);
            tvProfileName.setText(getString(R.string.guest));
            Collections.addAll(sideMenuList, guestMenuList);
        }
        adapter.addAll(sideMenuList);


    }

    public void update() {
        if (sideMenuList != null) sideMenuList.clear();

        if (prefHelper.getUser() != null) {

            tvProfileName.setText(prefHelper.getUser().getFullName());

            if (prefHelper.getUser().getProfileImage() != null && prefHelper.getUser().getProfileImage().length() > 0) {
                Picasso.with(getDockActivity())
                        .load(prefHelper.getUser().getProfileImage())
                        .into(imgProfile);
            }
            Collections.addAll(sideMenuList, userMenuList);
        } else if (prefHelper.getGuestTOKEN() != null) {
            imgProfile.setImageResource(R.drawable.user);
            tvProfileName.setText(getString(R.string.guest));
            Collections.addAll(sideMenuList, guestMenuList);
        }
        adapter.addAll(sideMenuList);

    }

    @Override
    public void onItemClick(View view, int position) {
        //sideMenu click events for non registered user
        if (Util.doubleClickCheck()) {
            getDockActivity().closeResideMenu();
            if (prefHelper.getUser() != null) {

                switch (position) {
                    case 0:
                        getDockActivity().replaceDockableFragment(HomeFragment.newInstance(),
                                HomeFragment.class.getSimpleName());

                        break;
                    case 1:
                        getDockActivity().replaceDockableFragment(MyOrdersFragment.newInstance(), MyOrdersFragment.class.getSimpleName());
                        break;
                    case 2:
                        getDockActivity().replaceDockableFragment(CompaniesListFragment.newInstance(), CompaniesListFragment.class.getSimpleName());
                        break;
                    case 3:
                        serviceHelper.enqueueCall(webService.getCompany(),
                                WebServiceConstants.getCompanies);
                       // openDialog();
                        break;
                    case 4:
                        getDockActivity().replaceDockableFragment(AboutFragment.newInstance(), AboutFragment.class.getSimpleName());
                        break;
                    case 5:
                        getDockActivity().replaceDockableFragment(ContactUsFragment.newInstance(), ContactUsFragment.class.getSimpleName());
                        break;
                    case 6:

                    /*final Dialog dialog = DialogFactory.createCustomDialog(getDockActivity(), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();

                            serviceHelper.enqueueCall(webService.logout(prefHelper.getUser().getToken()),
                                    WebServiceConstants.logOut);


                        }
                    }, getResources().getString(R.string.logout), getResources().getString(R.string.message_logout));

                    dialog.show();*/

                        final DialogHelper logoutdialog = new DialogHelper(getDockActivity());
                        logoutdialog.initlogout(R.layout.logout_dialog,
                                getResources().getString(R.string.logout),
                                getResources().getString(R.string.message_logout),
                                getResources().getColor(R.color.login_email_grey),
                                getResources().getColor(R.color.blue_theme),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) { //left click

                                        logoutdialog.hideDialog();

                                        serviceHelper.enqueueCall(webService.logout(prefHelper.getUser().getToken()),
                                                WebServiceConstants.logOut);

                                    }
                                }, new View.OnClickListener() { //right click
                                    @Override
                                    public void onClick(View v) {
                                        getMainActivity().getResideMenu().closeMenu();
                                        logoutdialog.hideDialog();
                                    }
                                });
                        logoutdialog.setCancelable(false);
                        logoutdialog.showDialog();

                        break;


                    default:
                        break;
                }
            } else if (prefHelper.getGuestTOKEN() != null) {

                switch (position) {
                    case 0:
                        getDockActivity().replaceDockableFragment(HomeFragment.newInstance(),
                                HomeFragment.class.getSimpleName());

                        break;
                    case 1:
                        getDockActivity().replaceDockableFragment(CompaniesListFragment.newInstance(), CompaniesListFragment.class.getSimpleName());
                        break;
                    case 2:
                        getDockActivity().replaceDockableFragment(AboutFragment.newInstance(), AboutFragment.class.getSimpleName());
                        break;
                    case 3:
                        getDockActivity().replaceDockableFragment(ContactUsFragment.newInstance(), ContactUsFragment.class.getSimpleName());
                        break;
                    case 4:
                        prefHelper.setGuestTOKEN(null);
//                        sideMenuList.clear();
//                        Collections.addAll(sideMenuList, guestMenuList);
//                        adapter.addAll(sideMenuList);


//                        getDockActivity().replaceDockableFragment(LoginFragment.newInstance(), LoginFragment.class.getSimpleName());

                        prefHelper.setLoginStatus(false);
                        getDockActivity().startActivity(new Intent(getDockActivity(), MainActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        break;

                    default:
                        break;
                }
            }
        }

    }

    @Override
    public void ResponseSuccess(Object result, String tag, String message) {
        switch (tag) {

            case WebServiceConstants.logOut:
                getMainActivity().getResideMenu().closeMenu();
//                prefHelper.setLoginStatus(false);
                prefHelper.putUser(null);
//                getDockActivity().popBackStackTillEntry(0);
//                getDockActivity().replaceDockableFragment(LoginFragment.newInstance(), LoginFragment.class.getSimpleName());

                prefHelper.setLoginStatus(false);
                DataHelper.deleteRealmData();
                getDockActivity().startActivity(new Intent(getDockActivity(), MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;

            case WebServiceConstants.getCompanies:
                companyId = 0;
                companyEnts = (List<CompanyEnt>) result;
                openDialog();
                break;

            case WebServiceConstants.changeVendor:
                UserEnt login = (UserEnt) result;
                //Empty card
                if (Util.getParsedInteger(login.getCompanyId())
                        != Util.getParsedInteger(prefHelper.getUser().getCompanyId())) {
                    DataHelper.deleteRealmData();
                }
                prefHelper.putUser(login);
                UIHelper.showShortToastInCenter(getDockActivity(), message);
                getDockActivity().replaceDockableFragment(HomeFragment.newInstance(), HomeFragment.class.getSimpleName());

                break;
        }

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
        final ActionSheetDialog dialog = new ActionSheetDialog(getDockActivity(), stringItems, null);
        dialog.title(getResources().getString(R.string.select_supplier))
                .titleTextSize_SP(14.5f)
                .cancelText(getResources().getString(android.R.string.cancel))
                .show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                companyId = companyEnts.get(position).getId();
                companyName = stringItems[position];
//                TextViewHelper.setText(tvSelectSupplier, stringItems[position]);
                callService(companyId);
                dialog.dismiss();
            }

            private void callService(int companyId) {
                serviceHelper.enqueueCall(webService.changeVendor(companyId, prefHelper.getUser().getToken()),
                        WebServiceConstants.changeVendor);
            }
        });
    }

    @Override
    public void profileUpdate() {
        if (prefHelper != null && prefHelper.getUser() != null) {
            tvProfileName.setText(prefHelper.getUser().getFullName());
            if (prefHelper.getUser().getProfileImage() != null && prefHelper.getUser().getProfileImage().length() > 0) {
                Picasso.with(getDockActivity())
                        .load(prefHelper.getUser().getProfileImage())
                        .into(imgProfile);
            }
        }
    }


    public class SideMenuModel {
        private int icon;
        private String name;

        SideMenuModel(int icon, String name) {
            this.icon = icon;
            this.name = name;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
