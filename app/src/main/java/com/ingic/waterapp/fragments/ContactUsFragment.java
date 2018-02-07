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
import com.ingic.waterapp.helpers.TextViewHelper;
import com.ingic.waterapp.helpers.UIHelper;
import com.ingic.waterapp.ui.views.AnyEditTextView;
import com.ingic.waterapp.ui.views.AnyTextView;
import com.ingic.waterapp.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends BaseFragment implements View.OnClickListener {

    Unbinder unbinder;
    @BindView(R.id.tv_contactUs_phone)
    AnyTextView tvPhone;
    @BindView(R.id.tv_contactUs_email)
    AnyTextView tvEmail;
    @BindView(R.id.et_contactUs_feedback)
    AnyEditTextView etFeedback;
    @BindView(R.id.btn_submit)
    Button btnSubmit;


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
        if (prefHelper.getUser() != null) {
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
                if (etFeedback.testValidity()) {
                    serviceHelper.enqueueCall(webService.feedback(etFeedback.getText().toString()
                            , prefHelper.getUser().getToken()), WebServiceConstants.feedback);
                    notImplemented();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void ResponseSuccess(Object result, String tag, String message) {
        switch (tag) {
            case WebServiceConstants.feedback:
                UIHelper.showShortToastInCenter(getDockActivity(), tag);
                getDockActivity().popFragment();
                break;
        }
    }
}
