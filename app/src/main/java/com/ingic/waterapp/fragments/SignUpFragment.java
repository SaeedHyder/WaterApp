package com.ingic.waterapp.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ingic.waterapp.R;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.global.AppConstants;
import com.ingic.waterapp.ui.views.AnyEditTextView;
import com.ingic.waterapp.ui.views.AnyTextView;
import com.ingic.waterapp.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends BaseFragment implements View.OnClickListener {

    Unbinder unbinder;
    @BindView(R.id.et_signup_name)
    AnyEditTextView etName;
    @BindView(R.id.et_signup_email)
    AnyEditTextView etEmail;
    @BindView(R.id.et_signup_password)
    AnyEditTextView etPassword;
    @BindView(R.id.et_signup_confirmPassword)
    AnyEditTextView etConfirmPassword;
    @BindView(R.id.et_signup_selectSupplier)
    AnyTextView tvSelectSupplier;
    @BindView(R.id.btn_signup)
    Button btnSignUp;

    public SignUpFragment() {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        return view;
    }

    private void setListeners() {
        tvSelectSupplier.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        // TODO Auto-generated method stub
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.clearHeaderBackround();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        setListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDockActivity().getDrawerLayout() != null)
            getDockActivity().lockDrawer();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signup:
                if (isValidate()) {
//                    login(etEmail.getText().toString(),
//                            etPassword.getText().toString());
                    launchHomeFragment(AppConstants.REGISTERED_USER);
                }
                break;
            case R.id.tv_profile_selectSupplier:
                openDialog();

                break;
            default:
                break;
        }
    }

    private void openDialog() {
        final CharSequence[] items = {
                "Aquafina", "Nestle Waters", "Romana Mineral Water"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getDockActivity());
        builder.setTitle("Select Supplier");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                tvSelectSupplier.setText(items[item]);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private boolean isValidate() {
        if (etName.testValidity() && etEmail.testValidity() && etPassword.testValidity()) {
            if (checkPassword()) {
                return true;
            }
        }
        return false;
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

//    private void launchMainActivity(int type) {
//        prefHelper.setLoginStatus(true);
//        prefHelper.setLoginType(type);
//        getDockActivity().startActivity(new Intent(getDockActivity(), MainActivity.class)
//                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//    }


    private boolean checkPassword() {
        if (etPassword.testValidity()
                || etConfirmPassword.testValidity()) {

            if (!passwordLength(etPassword.getText().toString())) {
//                UIHelper.showShortToastInCenter(getDockActivity(), getResources().getString(R.string.password_length));
                etPassword.setError(getResources().getString(R.string.password_length));
                return false;
            }
//        if (!passwordLength(ietConfirmPassword.getText().toString())){
//            ietConfirmPassword.setError(getString(R.string.password_length));
//            return false;
//        }
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.error_passwords_do_not_match));
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean passwordLength(String pwd) {
        return (pwd.length() > 5);
    }
}
