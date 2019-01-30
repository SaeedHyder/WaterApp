package com.ingic.waterapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ingic.waterapp.R;
import com.ingic.waterapp.activities.MainActivity;
import com.ingic.waterapp.entities.FacebookLoginEnt;
import com.ingic.waterapp.entities.GuestEnt;
import com.ingic.waterapp.entities.UserEnt;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.global.AppConstants;
import com.ingic.waterapp.global.WebServiceConstants;
import com.ingic.waterapp.helpers.FacebookLoginHelper;
import com.ingic.waterapp.helpers.GoogleHelper;
import com.ingic.waterapp.helpers.InternetHelper;
import com.ingic.waterapp.interfaces.FacebookLoginListener;
import com.ingic.waterapp.ui.views.AnyEditTextView;
import com.ingic.waterapp.ui.views.AnyTextView;
import com.ingic.waterapp.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class LoginFragment extends BaseFragment implements View.OnClickListener, GoogleHelper.GoogleHelperInterfce, FacebookLoginListener {
    public static final String TAG = LoginFragment.class.getSimpleName();

    private static final int RC_SIGN_IN = 007;

    Unbinder unbinder;
    @BindView(R.id.tv_login_forgotPassword)
    AnyTextView btnForgotPassword;
    @BindView(R.id.et_login_email)
    AnyEditTextView etEmail;
    @BindView(R.id.et_login_password)
    AnyEditTextView etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_login_signup)
    Button btnSignUp;
    @BindView(R.id.btn_login_fb)
    Button btnfb;
    @BindView(R.id.btn_login_google)
    Button btnGoogle;
    @BindView(R.id.tv_login_guest)
    AnyTextView btnGuest;

    private FacebookLoginHelper facebookLoginHelper;
    private CallbackManager callbackManager;
    private GoogleHelper googleHelper;
    private String mSocialMediaPlatform = "";
    private String mSocialMediaID = "";
    private String refreshToken;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        refreshToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "onCreateView: Login Fragment refresh token : " + refreshToken);
        return view;
    }

    private void setListeners() {
        btnForgotPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnfb.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
        btnGuest.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        // TODO Auto-generated method stub
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        setListeners();

        setupGoogleSignup();
        setupFacebookLogin();
    }


    private void setupGoogleSignup() {
        googleHelper = GoogleHelper.getInstance();
        googleHelper.setGoogleHelperInterface(this);
        googleHelper.configGoogleApiClient(this);
    }

    private void setupFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        // btnfbLogin.setFragment(this);
        facebookLoginHelper = new FacebookLoginHelper(getDockActivity(), this, this);
        LoginManager.getInstance().registerCallback(callbackManager, facebookLoginHelper);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (isValidate()) {

                    //String token = FirebaseInstanceId.getInstance().getToken();
//                    String token = "sadad";

                    serviceHelper.enqueueCall(webService.login(
                            etEmail.getText().toString(),
                            etPassword.getText().toString(),
                            refreshToken,
                            AppConstants.Device_Type
                            ),
                            WebServiceConstants.signIn);

                }
                break;
            case R.id.tv_login_guest:

                serviceHelper.enqueueCall(webService.guestUserToken(
                        AppConstants.Water),
                        WebServiceConstants.guestUserToken);

                break;
            case R.id.btn_login_fb:
                LoginManager.getInstance().logOut(); //this is for when mobile have already facebook app
                LoginManager.getInstance().logInWithReadPermissions(LoginFragment.this, facebookLoginHelper.getPermissionNeeds());
                break;
            case R.id.btn_login_google:
                googleHelper.intentGoogleSign();
                break;
            case R.id.tv_login_forgotPassword:
                getDockActivity().replaceDockableFragment(EmailForgotPasswordFragment.newInstance(),
                        EmailForgotPasswordFragment.class.getSimpleName());
                break;
            case R.id.btn_login_signup:
                getDockActivity().replaceDockableFragment(SignUpFragment.newInstance(), SignUpFragment.class.getSimpleName());
                break;
            default:
                break;
        }
    }

    @Override
    public void ResponseSuccess(Object result, String tag, String message) {
        switch (tag) {

            case WebServiceConstants.signIn:
                UserEnt userEnt = (UserEnt) result;
                prefHelper.putUser(userEnt);
                launchMainActivity(AppConstants.REGISTERED_USER);
                break;
            case WebServiceConstants.socialSignIn:
                UserEnt userData = (UserEnt) result;
                prefHelper.setSocailLoginStatus(true);
                prefHelper.putUser(userData);
                launchMainActivity(AppConstants.REGISTERED_USER);
                break;

            case WebServiceConstants.guestUserToken:

                GuestEnt guestEnt = (GuestEnt) result;
                if (guestEnt != null) {
                    prefHelper.setGuestTOKEN(guestEnt.getToken());
                    launchMainActivity(AppConstants.GUEST_USER);
                }

                break;
        }
    }

    private boolean isValidate() {
        return etEmail.testValidity() && etPassword.testValidity();
    }

    private void launchHomeFragment(int type) {
        prefHelper.setLoginStatus(true);
        prefHelper.setLoginType(type);
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.LOGIN_TYPE, type);
        fragment.setArguments(bundle);
        getDockActivity().popBackStackTillEntry(0);
        getDockActivity().replaceDockableFragment(fragment, "HomeFragment");
    }

    private void launchMainActivity(int type) {
        prefHelper.setLoginStatus(true);
        prefHelper.setLoginType(type);
        getDockActivity().startActivity(new Intent(getDockActivity(), MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDockActivity().getResideMenu() != null)
            getDockActivity().closeResideMenu();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            googleHelper.handleGoogleResult(requestCode, resultCode, data);
        } else
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        googleHelper.ConnectGoogleAPi();
    }

    @Override
    public void onStop() {
        super.onStop();
        googleHelper.DisconnectGoogleApi();
    }

    private void clearViews() {
        etEmail.setText("");
        etPassword.setText("");
    }

    @Override
    public void onGoogleSignInResult(GoogleSignInAccount result) {
        clearViews();
        String Name = result.getDisplayName();
        String Email = result.getEmail();

        String Image = "";
        if (result.getPhotoUrl() != null) {
            Image = result.getPhotoUrl().toString();
        }

        mSocialMediaPlatform = WebServiceConstants.PLATFORM_GOOGLE;
        mSocialMediaID = result.getId();

        if (mSocialMediaID != null && mSocialMediaID.length() > 0) {
            if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity()))
                socialMediaSignIn(mSocialMediaID, mSocialMediaPlatform, Name, Email, Image);
        }

    }

    @Override
    public void onSuccessfulFacebookLogin(FacebookLoginEnt LoginEnt) {
        clearViews();
        String Name = LoginEnt.getFacebookFullName();
        String Email = LoginEnt.getFacebookEmail() == null ? "" : LoginEnt.getFacebookEmail();
        String Image = LoginEnt.getFacebookUProfilePicture();
        mSocialMediaPlatform = WebServiceConstants.PLATFORM_FACEBOOK;
        mSocialMediaID = LoginEnt.getFacebookUID();

        if (mSocialMediaID != null && mSocialMediaID.length() > 0) {
            if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity()))
                socialMediaSignIn(mSocialMediaID, mSocialMediaPlatform, Name, Email, Image);
        }
    }

    private void socialMediaSignIn(final String SocialMediaId, final String SocialMediaPlatform, final String Name, final String Email, final String Image) {
        refreshToken = FirebaseInstanceId.getInstance().getToken();



        serviceHelper.enqueueCall(webService.userFacebookLogin(
                mSocialMediaID,
                mSocialMediaPlatform,
                Name,
                Email,
                refreshToken,
                AppConstants.Device_Type),
                WebServiceConstants.socialSignIn);
    }


}
