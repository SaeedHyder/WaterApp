package com.ingic.waterapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ingic.waterapp.R;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.global.AppConstants;
import com.ingic.waterapp.global.WebServiceConstants;
import com.ingic.waterapp.helpers.TextViewHelper;
import com.ingic.waterapp.helpers.UIHelper;
import com.ingic.waterapp.ui.views.AnyEditTextView;
import com.ingic.waterapp.ui.views.AnyTextView;
import com.ingic.waterapp.ui.views.TitleBar;
import com.ingic.waterapp.ui.views.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends BaseFragment implements View.OnClickListener {

    Unbinder unbinder;
    @BindView(R.id.tv_contactUs_phone)
    AnyEditTextView tvPhone;
    @BindView(R.id.tv_contactUs_email)
    AnyEditTextView tvEmail;
    @BindView(R.id.et_contactUs_feedback)
    AnyEditTextView etFeedback;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    private String token;
    private String type;


    public ContactUsFragment() {
        // Required empty public constructor
    }

    public static ContactUsFragment newInstance() {
        return new ContactUsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        return view;
    }

    private void setListeners() {
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        // TODO Auto-generated method stub
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getResources().getString(R.string.contact_us));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        setListeners();
        setData();
    }

    private void setData() {

        if (prefHelper.getUser() != null && prefHelper.getLoginTYpe() == AppConstants.REGISTERED_USER) {
            tvPhone.setEnabled(false);
            tvEmail.setEnabled(false);
            TextViewHelper.setText(tvPhone, prefHelper.getUser().getAdminMobile());
            TextViewHelper.setText(tvEmail, prefHelper.getUser().getAdminEmail());
        }
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
            case R.id.btn_submit:
                if (Util.doubleClickCheck2Seconds()) {
                    if (isValidate()) {
                        if (prefHelper.getUser() != null) {
                            type = AppConstants.Normal;
                            token = prefHelper.getUser().getToken();

                            serviceHelper.enqueueCall(webService.feedback(type, etFeedback.getText().toString()
                                    , token), WebServiceConstants.feedback);
                        } else {
                            type = AppConstants.Guest;
                            token = prefHelper.getGuestTOKEN();

                            serviceHelper.enqueueCall(webService.feedback(type, etFeedback.getText().toString(),tvEmail.getText().toString(),
                                    tvPhone.getText().toString(), token), WebServiceConstants.feedback);
                        }

                    }
                }
               /* if (etFeedback.testValidity()) {

                }*/
                break;
            default:
                break;
        }
    }

    private boolean isValidate() {
        if (tvPhone.getText() == null || (tvPhone.getText().toString().isEmpty())) {
            if (tvPhone.requestFocus()) {
                setEditTextFocus(tvPhone);
            }
            tvPhone.setError(getString(R.string.error));
            return false;
        } else if (tvEmail.getText() == null || (tvEmail.getText().toString().isEmpty()) ||
                (!Patterns.EMAIL_ADDRESS.matcher(tvEmail.getText().toString()).matches())) {
            if (tvEmail.requestFocus()) {
                setEditTextFocus(tvEmail);
            }
            tvEmail.setError(getString(R.string.enter_email));
            return false;
        } else if (etFeedback.getText() == null || (etFeedback.getText().toString().isEmpty())) {
            if (etFeedback.requestFocus()) {
                setEditTextFocus(etFeedback);
            }
            etFeedback.setError(getString(R.string.error));
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void ResponseSuccess(Object result, String tag, String message) {
        switch (tag) {
            case WebServiceConstants.feedback:
                UIHelper.showShortToastInCenter(getDockActivity(), message);
                getDockActivity().popFragment();
                break;
        }
    }


}
