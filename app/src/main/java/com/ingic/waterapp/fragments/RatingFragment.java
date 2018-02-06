package com.ingic.waterapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ingic.waterapp.R;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.global.AppConstants;
import com.ingic.waterapp.global.WebServiceConstants;
import com.ingic.waterapp.helpers.TextViewHelper;
import com.ingic.waterapp.ui.views.AnyTextView;
import com.ingic.waterapp.ui.views.CustomRatingBar;
import com.ingic.waterapp.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RatingFragment extends BaseFragment implements View.OnClickListener {

    Unbinder unbinder;
    @BindView(R.id.rb_serviceRating)
    CustomRatingBar rbService;
    @BindView(R.id.rb_companyRating)
    CustomRatingBar rbCompany;

    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.tv_rating_title)
    AnyTextView tvName;
    private int companyId = 0;
    private String name;

    public RatingFragment() {
        // Required empty public constructor
    }

    public static RatingFragment newInstance() {
        return new RatingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rating, container, false);
        if (getArguments() != null) {
            companyId = getArguments().getInt(AppConstants.COMPANY_ID);
            name = getArguments().getString(AppConstants.BOTTLE_NAME);
        }
        TextViewHelper.setText(tvName, name);
        return view;
    }

    private void setListeners() {
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        // TODO Auto-generated method stub
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.ratings));
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
            case R.id.btn_submit:
                int serviceRating = (int) Math.ceil(rbService.getScore());
                int companyRating = (int) Math.ceil(rbCompany.getScore());
                serviceHelper.enqueueCall(webService.rating(companyId, serviceRating, companyRating),
                        WebServiceConstants.rating);

                break;
            default:
                break;
        }
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.rating:
                getDockActivity().popFragment();
                break;
            default:
                break;
        }
    }
}