package com.ingic.waterapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ingic.waterapp.R;
import com.ingic.waterapp.entities.CmsEnt;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.global.WebServiceConstants;
import com.ingic.waterapp.helpers.TextViewHelper;
import com.ingic.waterapp.ui.views.AnyTextView;
import com.ingic.waterapp.ui.views.TitleBar;
import com.ingic.waterapp.ui.views.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends BaseFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.btn_about)
    Button btnAbout;
    @BindView(R.id.btn_about_terms)
    Button btnTermsCondition;
    @BindView(R.id.btn_about_privacyPolicy)
    Button btnPrivacyPolicy;
    @BindView(R.id.tv_about_description)
    AnyTextView tvDesc;
    private CmsEnt response;

    public AboutFragment() {
        // Required empty public constructor
    }

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    private void setListeners() {
        btnAbout.setOnClickListener(this);
        btnTermsCondition.setOnClickListener(this);
        btnPrivacyPolicy.setOnClickListener(this);

        btnAbout.setSelected(true);
        btnTermsCondition.setSelected(false);
        btnPrivacyPolicy.setSelected(false);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        // TODO Auto-generated method stub
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.about));
        titleBar.showBackButton();
        titleBar.clearHeaderBackround();
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
        callService(WebServiceConstants.about);
        setListeners();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_about:
                if (Util.doubleClickCheck())
                    if (!btnAbout.isSelected()) {
                        btnAbout.setSelected(true);
                        btnTermsCondition.setSelected(false);
                        btnPrivacyPolicy.setSelected(false);

                        callService(WebServiceConstants.about);
//                        if (response != null)

                    }
                break;

            case R.id.btn_about_terms:
                if (Util.doubleClickCheck())
                    if (!btnTermsCondition.isSelected()) {
                        btnAbout.setSelected(false);
                        btnTermsCondition.setSelected(true);
                        btnPrivacyPolicy.setSelected(false);

                        callService(WebServiceConstants.term);
//                        if (response != null)
//                            TextViewHelper.setHtmlText(tvDesc, response.getBody());

                    }
                break;

            case R.id.btn_about_privacyPolicy:
                if (Util.doubleClickCheck())
                    if (!btnPrivacyPolicy.isSelected()) {
                        btnAbout.setSelected(false);
                        btnTermsCondition.setSelected(false);
                        btnPrivacyPolicy.setSelected(true);

                        callService(WebServiceConstants.privacy);
//                        if (response != null)
//                            TextViewHelper.setHtmlText(tvDesc, response.getBody());

                    }
                break;

            default:
                break;
        }
    }

    private void callService(String type) {

        serviceHelper.enqueueCall(webService.getCms(type),
                WebServiceConstants.getCms);
    }

    @Override
    public void ResponseSuccess(Object result, String tag, String message) {
        switch (tag) {
            case WebServiceConstants.getCms:
                response = (CmsEnt) result;
                if (response != null)
                    TextViewHelper.setHtmlText(tvDesc, response.getBody());

                break;
        }
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

}
/*
* TextViewHelper.setHtmlText(tvAboutApp, response.body().getResult().getText());
* */



