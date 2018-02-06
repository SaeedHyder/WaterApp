package com.ingic.waterapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ingic.waterapp.R;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.global.WebServiceConstants;
import com.ingic.waterapp.ui.views.AnyEditTextView;
import com.ingic.waterapp.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends BaseFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.et_changePassword_currentPassword)
    AnyEditTextView etCurrentPassword;
    @BindView(R.id.et_changePassword_newPassword)
    AnyEditTextView etNewPassword;
    @BindView(R.id.et_changePassword_confirmPassword)
    AnyEditTextView etConfirmNewPassword;
    @BindView(R.id.btn_done)
    Button btnDone;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    private void setListeners() {
        btnDone.setOnClickListener(this);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        // TODO Auto-generated method stub
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.change_password));
        titleBar.showBackButton();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDockActivity().getDrawerLayout() != null)
            getDockActivity().lockDrawer();
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
            case R.id.btn_done:

                if (checkPassword()) {

//                    getDockActivity().popFragment();
                    serviceHelper.enqueueCall(webService.changepassword(
                            etCurrentPassword.getText().toString(),
                            etNewPassword.getText().toString(),
                            etConfirmNewPassword.getText().toString(),
                            prefHelper.getUser().getToken()),
                            WebServiceConstants.changePassword);
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {

            case WebServiceConstants.changePassword:
                getDockActivity().popFragment();
                break;
        }
    }

    private boolean checkPassword() {
        if (etCurrentPassword.testValidity() || etNewPassword.testValidity()
                || etConfirmNewPassword.testValidity()) {

            if (!passwordLength(etNewPassword.getText().toString())) {
//                UIHelper.showShortToastInCenter(getDockActivity(), getResources().getString(R.string.password_length));
                etNewPassword.setError(getResources().getString(R.string.password_length));
                return false;
            }
//        if (!passwordLength(ietConfirmPassword.getText().toString())){
//            ietConfirmPassword.setError(getString(R.string.password_length));
//            return false;
//        }

            if (!etNewPassword.getText().toString().equals(etConfirmNewPassword.getText().toString())) {
                etConfirmNewPassword.setError(getResources().getString(R.string.error_passwords_do_not_match));
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