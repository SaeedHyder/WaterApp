package com.ingic.waterapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ingic.waterapp.R;
import com.ingic.waterapp.entities.CompanyDetails;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.global.AppConstants;
import com.ingic.waterapp.retrofit.GsonFactory;
import com.ingic.waterapp.ui.views.AnyTextView;
import com.ingic.waterapp.ui.views.CustomRatingBar;
import com.ingic.waterapp.ui.views.TitleBar;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeProductReviewFragment extends BaseFragment {

    Unbinder unbinder;
    CompanyDetails companyDetails;
    @BindView(R.id.img_bottle)
    ImageView imgBottle;
    @BindView(R.id.tv_rating_title)
    AnyTextView tvRatingTitle;
    @BindView(R.id.tv_rating_serviceRatingText)
    AnyTextView tvRatingServiceRatingText;
    @BindView(R.id.rb_serviceRating)
    CustomRatingBar rbServiceRating;
    @BindView(R.id.tv_rating_companyRatingText)
    AnyTextView tvRatingCompanyRatingText;
    @BindView(R.id.rb_companyRating)
    CustomRatingBar rbCompanyRating;
    Unbinder unbinder1;

    public HomeProductReviewFragment() {
        // Required empty public constructor
    }

    public static HomeProductReviewFragment newInstance() {
        return new HomeProductReviewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            companyDetails = GsonFactory.getConfiguredGson().fromJson(getArguments().getString(AppConstants.CompanyDetails), CompanyDetails.class);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_product_review, container, false);
        unbinder1 = ButterKnife.bind(this, view);

        if (companyDetails != null && companyDetails.getProduct().size() > 0) {

            if (companyDetails.getProduct().get(0) != null && companyDetails.getProduct().get(0).getProductPicture().length() > 0) {
                Picasso.with(getDockActivity())
                        .load(companyDetails.getProduct().get(0).getProductPicture())
                        .into(imgBottle);
            }

            tvRatingTitle.setText(companyDetails.getName());

            if (companyDetails.getReview().getServiceRate() != null && companyDetails.getReview().getServiceRate().length() > 0)
                rbServiceRating.setScore(Float.parseFloat(companyDetails.getReview().getServiceRate()));

            if (companyDetails.getReview().getCompanyRate() != null && companyDetails.getReview().getCompanyRate().length() > 0)
                rbCompanyRating.setScore(Float.parseFloat(companyDetails.getReview().getCompanyRate()));

        }

        return view;
    }


    @Override
    public void setTitleBar(TitleBar titleBar) {
        // TODO Auto-generated method stub
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDockActivity().getDrawerLayout() != null)
            getDockActivity().lockDrawer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder1.unbind();
    }
}
