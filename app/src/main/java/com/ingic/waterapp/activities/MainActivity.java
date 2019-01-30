package com.ingic.waterapp.activities;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.ingic.waterapp.R;
import com.ingic.waterapp.fragments.HomeFragment;
import com.ingic.waterapp.fragments.LoginFragment;
import com.ingic.waterapp.fragments.MyOrdersFragment;
import com.ingic.waterapp.fragments.NotificationsFragment;
import com.ingic.waterapp.fragments.RatingFragment;
import com.ingic.waterapp.fragments.SideMenuFragment;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.global.AppConstants;
import com.ingic.waterapp.global.SideMenuChooser;
import com.ingic.waterapp.global.SideMenuDirection;
import com.ingic.waterapp.helpers.ScreenHelper;
import com.ingic.waterapp.helpers.UIHelper;
import com.ingic.waterapp.residemenu.ResideMenu;
import com.ingic.waterapp.ui.views.TitleBar;
import com.ingic.waterapp.ui.views.Util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ingic.waterapp.global.AppConstants.deletePush;
import static com.ingic.waterapp.global.AppConstants.inactivePush;


public class MainActivity extends DockActivity implements OnClickListener {
    private static String TAG = "MainActivity";
    public TitleBar titleBar;
    @BindView(R.id.sideMneuFragmentContainer)
    FrameLayout sideMneufragmentContainer;
    @BindView(R.id.header_main)
    TitleBar header_main;
    @BindView(R.id.mainFrameLayout)
    FrameLayout mainFrameLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.content_frame)
    RelativeLayout contentFrame;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private MainActivity mContext;
    private boolean loading;

    private float lastTranslate = 0.0f;

    private String sideMenuType;
    private String sideMenuDirection;
    //Unread notification count broadcast//
    BroadcastReceiver notificationCountBroadcastReceiver;
    BroadcastReceiver cartCountBroadcastReceiver;
    private String ratingBottleName, ratingCompanyId, type, orderId;
    protected BroadcastReceiver broadcastReceiver;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dock);
        ButterKnife.bind(this);
        titleBar = header_main;
        // setBehindContentView(R.layout.fragment_frame);
        mContext = this;
        Log.i("Screen Density", ScreenHelper.getDensity(this) + "");

        sideMenuType = SideMenuChooser.RESIDE_MENU.getValue();
        sideMenuDirection = SideMenuDirection.LEFT.getValue();

        settingSideMenu(sideMenuType, sideMenuDirection);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            ratingBottleName = bundle.getString(AppConstants.RATING_BOTTLE);
            ratingCompanyId = bundle.getString(AppConstants.RATING_COMPANY_ID);
            type = bundle.getString(AppConstants.TYPE);
            orderId = bundle.getString(AppConstants.ACTIONID);

        }

        onNotificationReceived();

        notificationCountBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int count = intent.getIntExtra(AppConstants.UNREAD_NOTICATION_COUNT, 0);
                titleBar.setNotificationCount(count);
            }
        };

        cartCountBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int count = intent.getIntExtra(AppConstants.CART_COUNT, 0);
                titleBar.setCartCount(count);
            }
        };

        titleBar.setMenuButtonListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (sideMenuType.equals(SideMenuChooser.DRAWER.getValue()) && getDrawerLayout() != null) {
                    if (sideMenuDirection.equals(SideMenuDirection.LEFT.getValue())) {
                        drawerLayout.openDrawer(Gravity.LEFT);
                    } else {
                        drawerLayout.openDrawer(Gravity.RIGHT);
                    }
                } else {
                    resideMenu.openMenu(sideMenuDirection);
                }

            }
        });

        titleBar.setBackButtonListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (loading) {
                    UIHelper.showLongToastInCenter(getApplicationContext(),
                            R.string.message_wait);
                } else {
                    popFragment();
                    UIHelper.hideSoftKeyboard(getApplicationContext(),
                            titleBar);
                }
            }
        });

        titleBar.setNotificationButtonListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prefHelper.getUser() != null) {
                    if (Util.doubleClickCheck())
                        openNotification();
                } else
                    UIHelper.showShortToastInCenter(MainActivity.this, getResources().getString(R.string.please_login));
            }
        });
        titleBar.setCartButtonListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prefHelper.getUser() != null) {
                    if (Util.doubleClickCheck())
                        openCart();
                } else
                    UIHelper.showShortToastInCenter(MainActivity.this, getResources().getString(R.string.please_login));
            }
        });

       // if (savedInstanceState == null)
            initFragment();

    }


    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(notificationCountBroadcastReceiver, new IntentFilter(AppConstants.UNREAD_NOTICATION_COUNT_BROADCAST));
        this.registerReceiver(cartCountBroadcastReceiver, new IntentFilter(AppConstants.CART_COUNT_BROADCAST));

        LocalBroadcastManager.getInstance(getDockActivity()).registerReceiver(broadcastReceiver,
                new IntentFilter(AppConstants.REGISTRATION_COMPLETE));

        LocalBroadcastManager.getInstance(getDockActivity()).registerReceiver(broadcastReceiver,
                new IntentFilter(AppConstants.PUSH_NOTIFICATION));

        if (!prefHelper.isLogin()) {
            replaceDockableFragment(LoginFragment.newInstance(), "LoginFragment");
        }
    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(notificationCountBroadcastReceiver);
        this.unregisterReceiver(cartCountBroadcastReceiver);
        super.onDestroy();
    }

    public View getDrawerView() {
        return getLayoutInflater().inflate(getSideMenuFrameLayoutId(), null);
    }

    private void settingSideMenu(String type, String direction) {

        if (type.equals(SideMenuChooser.DRAWER.getValue())) {


            DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams((int) getResources().getDimension(R.dimen._260sdp), (int) DrawerLayout.LayoutParams.MATCH_PARENT);


            if (direction.equals(SideMenuDirection.LEFT.getValue())) {
                params.gravity = Gravity.LEFT;
                sideMneufragmentContainer.setLayoutParams(params);
            } else {
                params.gravity = Gravity.RIGHT;
                sideMneufragmentContainer.setLayoutParams(params);
            }
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

            sideMenuFragment = SideMenuFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(getSideMenuFrameLayoutId(), sideMenuFragment).commit();

            drawerLayout.closeDrawers();
        } else {
            resideMenu = new ResideMenu(this);
            resideMenu.attachToActivity(this);
            resideMenu.setMenuListener(getMenuListener());
            resideMenu.setScaleValue(0.52f);

            setMenuItemDirection(direction);
        }
    }

    private void setMenuItemDirection(String direction) {

        if (direction.equals(SideMenuDirection.LEFT.getValue())) {

            SideMenuFragment leftSideMenuFragment = SideMenuFragment.newInstance();
            resideMenu.addMenuItem(leftSideMenuFragment, "LeftSideMenuFragment", direction);

        } else if (direction.equals(SideMenuDirection.RIGHT.getValue())) {

            SideMenuFragment rightSideMenuFragment = SideMenuFragment.newInstance();
            resideMenu.addMenuItem(rightSideMenuFragment, "RightSideMenuFragment", direction);

        }

    }

    private int getSideMenuFrameLayoutId() {
        return R.id.sideMneuFragmentContainer;

    }

    public void initFragment() {
        getSupportFragmentManager().addOnBackStackChangedListener(getListener());
        lockDrawer();
//        replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");
        if (prefHelper.isLogin()) {
            replaceDockableFragment(HomeFragment.newInstance(), "HomeFragment");

            if (type != null && !type.isEmpty() && type.equals(AppConstants.RATING)) {
                if (ratingBottleName != null && !ratingBottleName.isEmpty()) {
                    RatingFragment fragment = new RatingFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(AppConstants.BOTTLE_NAME, ratingBottleName);
                    bundle.putString(AppConstants.COMPANY_ID, ratingCompanyId);
                    bundle.putString(AppConstants.ORDER_ID, orderId);
                    bundle.putBoolean(AppConstants.IS_NOTIFICATION, false);
                    fragment.setArguments(bundle);

                    replaceDockableFragment(fragment, RatingFragment.class.getSimpleName());
                }
            } else if (type != null && !type.isEmpty() && type.equals(AppConstants.CANCELLED)) {
                replaceDockableFragment(MyOrdersFragment.newInstance(true), MyOrdersFragment.class.getSimpleName());
            } else if (type != null && !type.isEmpty() && type.equals(AppConstants.ADMIN)) {
                replaceDockableFragment(NotificationsFragment.newInstance(), NotificationsFragment.class.getSimpleName());
            } else if (type != null && !type.isEmpty() && !type.equals("")) {
                replaceDockableFragment(MyOrdersFragment.newInstance(), MyOrdersFragment.class.getSimpleName());
            }

        } else {
            replaceDockableFragment(LoginFragment.newInstance(), "LoginFragment");
        }
    }

    private void onNotificationReceived() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(AppConstants.PUSH_NOTIFICATION)) {
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        String Type = bundle.getString(AppConstants.TYPE);
                        String ratingBottleName = bundle.getString(AppConstants.RATING_BOTTLE);
                        String ratingCompanyId = bundle.getString(AppConstants.RATING_COMPANY_ID);
                        String orderId = bundle.getString(AppConstants.ACTIONID);

                        if (Type != null && Type.equals(deletePush)) {
                            UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.deleted_by_admin));
                            getDockActivity().popBackStackTillEntry(0);
                            prefHelper.setLoginStatus(false);

                            if (AccessToken.getCurrentAccessToken() != null) {
                                LoginManager.getInstance().logOut();
                            }
                            NotificationManager notificationManager = (NotificationManager) getDockActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancelAll();
                            getDockActivity().replaceDockableFragment(LoginFragment.newInstance(), "LoginFragment");
                        } else if (Type != null && Type.equals(inactivePush)) {
                            UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.blocked_by_admin));
                            getDockActivity().popBackStackTillEntry(0);
                            prefHelper.setLoginStatus(false);
                            if (AccessToken.getCurrentAccessToken() != null) {
                                LoginManager.getInstance().logOut();
                            }
                            NotificationManager notificationManager = (NotificationManager) getDockActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancelAll();
                            getDockActivity().replaceDockableFragment(LoginFragment.newInstance(), "LoginFragment");
                        } else if (Type != null && !Type.isEmpty() && Type.equals(AppConstants.RATING)) {
                            if (ratingBottleName != null && !ratingBottleName.isEmpty()) {
                                RatingFragment fragment = new RatingFragment();
                                Bundle bundleRating = new Bundle();
                                bundleRating.putString(AppConstants.BOTTLE_NAME, ratingBottleName);
                                bundleRating.putString(AppConstants.COMPANY_ID, ratingCompanyId);
                                bundleRating.putString(AppConstants.ORDER_ID, orderId);
                                bundleRating.putBoolean(AppConstants.IS_NOTIFICATION, false);

                                fragment.setArguments(bundleRating);
                                replaceDockableFragment(fragment, RatingFragment.class.getSimpleName());
                            }
                        }
                    }
                }
            }

        };
    }

    private FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();

                if (manager != null) {
                    BaseFragment currFrag = (BaseFragment) manager.findFragmentById(getDockFrameLayoutId());
                    if (currFrag != null) {
                        currFrag.fragmentResume();
                    }
                }
            }
        };

        return result;
    }

    @Override
    public void onLoadingStarted() {

        if (mainFrameLayout != null) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            mainFrameLayout.setVisibility(View.VISIBLE);
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
            loading = true;
        }
    }

    @Override
    public void onLoadingFinished() {
        mainFrameLayout.setVisibility(View.VISIBLE);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if (progressBar != null) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
        loading = false;

    }


    @Override
    public void onProgressUpdated(int percentLoaded) {

    }

    @Override
    public int getDockFrameLayoutId() {
        return R.id.mainFrameLayout;
    }

    @Override
    public void onMenuItemActionCalled(int actionId, String data) {

    }

    @Override
    public void setSubHeading(String subHeadText) {

    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }

    @Override
    public void hideHeaderButtons(boolean leftBtn, boolean rightBtn) {
    }

    @Override
    public void onBackPressed() {
        if (loading) {
            UIHelper.showLongToastInCenter(getApplicationContext(),
                    R.string.message_wait);
        } else
            super.onBackPressed();

    }

    @Override
    public void onClick(View view) {

    }

    private void notImplemented() {
        UIHelper.showLongToastInCenter(this, "Coming Soon");
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(getDockActivity()).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    public void lockDrawer() {
        try {
            if (drawerLayout != null) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
