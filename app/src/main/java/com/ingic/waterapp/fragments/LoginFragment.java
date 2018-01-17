package com.ingic.waterapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ingic.waterapp.R;
import com.ingic.waterapp.activities.MainActivity;
import com.ingic.waterapp.annotation.RestAPI;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.global.AppConstants;
import com.ingic.waterapp.ui.views.AnyEditTextView;
import com.ingic.waterapp.ui.views.AnyTextView;
import com.ingic.waterapp.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class LoginFragment extends BaseFragment implements View.OnClickListener {

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (isValidate()) {
                    login(etEmail.getText().toString(),
                            etPassword.getText().toString());
                    launchMainActivity(AppConstants.REGISTERED_USER);

                }
                break;
            case R.id.tv_login_guest:
                launchMainActivity(AppConstants.GUEST_USER);
            case R.id.btn_login_fb:
                launchMainActivity(AppConstants.REGISTERED_USER);
            case R.id.btn_login_google:
                launchMainActivity(AppConstants.REGISTERED_USER);
                break;
            case R.id.tv_login_forgotPassword:
                getDockActivity().replaceDockableFragment(EmailForgotPasswordFragment.newInstance(), EmailForgotPasswordFragment.class.getSimpleName());
                break;
            case R.id.btn_login_signup:
                getDockActivity().replaceDockableFragment(SignUpFragment.newInstance(), SignUpFragment.class.getSimpleName());
                break;
            default:
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

    @RestAPI
    private void login(String email, String password) {
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDockActivity().getResideMenu() != null)
            getDockActivity().closeResideMenu();
    }
}
