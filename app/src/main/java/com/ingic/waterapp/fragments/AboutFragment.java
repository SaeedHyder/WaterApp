package com.ingic.waterapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ingic.waterapp.R;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.ui.views.TitleBar;

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
            case R.id.btn_about:
                if (!btnAbout.isSelected()) {
                    btnAbout.setSelected(true);
                    btnTermsCondition.setSelected(false);
                    btnPrivacyPolicy.setSelected(false);
                }
                break;

            case R.id.btn_about_terms:

                if (!btnTermsCondition.isSelected()) {
                    btnAbout.setSelected(false);
                    btnTermsCondition.setSelected(true);
                    btnPrivacyPolicy.setSelected(false);
                }
                break;

            case R.id.btn_about_privacyPolicy:
                if (!btnPrivacyPolicy.isSelected()) {
                    btnAbout.setSelected(false);
                    btnTermsCondition.setSelected(false);
                    btnPrivacyPolicy.setSelected(true);
                }
                break;

            default:
                break;
        }
    }
}

