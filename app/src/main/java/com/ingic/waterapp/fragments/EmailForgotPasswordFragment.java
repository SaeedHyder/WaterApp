package com.ingic.waterapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ingic.waterapp.R;
import com.ingic.waterapp.entities.UserEnt;
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
public class EmailForgotPasswordFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.et_forgotPassword_email)
    AnyEditTextView etEmail;
    @BindView(R.id.btn_submit)
    Button btnSubmit;


    public EmailForgotPasswordFragment() {
        // Required empty public constructor
    }

    public static EmailForgotPasswordFragment newInstance() {
        return new EmailForgotPasswordFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_email_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etEmail.testValidity()) {

                    serviceHelper.enqueueCall(webService.forgotpassword(etEmail.getText().toString()),
                            WebServiceConstants.forgotPassword);
                }
            }
        });

    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag){

            case WebServiceConstants.forgotPassword:

                UserEnt userEnt = (UserEnt)result;
                prefHelper.putUser(userEnt);
                getDockActivity().replaceDockableFragment(EnterCodeFragment.newInstance(), EnterCodeFragment.class.getSimpleName());

                break;
        }
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
