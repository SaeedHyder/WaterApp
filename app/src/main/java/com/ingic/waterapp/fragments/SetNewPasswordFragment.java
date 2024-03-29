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
import com.ingic.waterapp.helpers.UIHelper;
import com.ingic.waterapp.ui.views.AnyEditTextView;
import com.ingic.waterapp.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetNewPasswordFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.et_newPassword_newPassword)
    AnyEditTextView etNewPassword;
    @BindView(R.id.et_newPassword_confirmPassword)
    AnyEditTextView etConfirmNewPassword;
    @BindView(R.id.btn_done)
    Button btnDone;

    public SetNewPasswordFragment() {
        // Required empty public constructor
    }

    public static SetNewPasswordFragment newInstance() {
        return new SetNewPasswordFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_new_password, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPassword()) {
                    changePassword(etNewPassword.getText().toString(),
                            etConfirmNewPassword.getText().toString());
                }
            }
        });

    }

    private void changePassword(String newPwd, String confirmNewPwd) {

        serviceHelper.enqueueCall(webService.ForgotChangePassword(newPwd.toString(),confirmNewPwd,prefHelper.getUser().getToken()),
                WebServiceConstants.forgotPassword);

    }

    @Override
    public void ResponseSuccess(Object result, String tag, String message) {
        switch (tag){

            case WebServiceConstants.forgotPassword:
                UIHelper.showLongToastInCenter(getDockActivity(),getString(R.string.pasword_change_sucess));
                getDockActivity().popBackStackTillEntry(1);
                break;
        }
    }

    private boolean checkPassword() {
        if (etNewPassword.testValidity()
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

    @Override
    public void setTitleBar(TitleBar titleBar) {
        // TODO Auto-generated method stub
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getResources().getString(R.string.forgot_password));
//        titleBar.setLeftBtnDrawable(R.drawable.ic_black_back);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDockActivity().getDrawerLayout() != null)
            getDockActivity().lockDrawer();
    }

}
