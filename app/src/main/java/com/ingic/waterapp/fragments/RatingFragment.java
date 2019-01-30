package com.ingic.waterapp.fragments;


import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ingic.waterapp.R;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.global.AppConstants;
import com.ingic.waterapp.global.WebServiceConstants;
import com.ingic.waterapp.helpers.UIHelper;
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


    @BindView(R.id.rb_serviceRating)
    CustomRatingBar rbService;
    @BindView(R.id.rb_companyRating)
    CustomRatingBar rbCompany;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.img_bottle)
    ImageView imgBottle;
    @BindView(R.id.tv_rating_title)
    AnyTextView tvRatingTitle;
    @BindView(R.id.tv_rating_serviceRatingText)
    AnyTextView tvRatingServiceRatingText;
    @BindView(R.id.tv_rating_companyRatingText)
    AnyTextView tvRatingCompanyRatingText;
    @BindView(R.id.rl_bottle)
    LinearLayout rlBottle;
    Unbinder unbinder;


    private int companyId = 0;
    private String name, orderId;
    private boolean isNotification = false;

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
            companyId = Integer.parseInt(getArguments().getString(AppConstants.COMPANY_ID));
            name = getArguments().getString(AppConstants.BOTTLE_NAME);
            orderId = getArguments().getString(AppConstants.ORDER_ID);
            isNotification = getArguments().getBoolean(AppConstants.IS_NOTIFICATION);
        }

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void setListeners() {
        btnSubmit.setOnClickListener(this);

        rbService.setOnScoreChanged(new CustomRatingBar.IRatingBarCallbacks() {
            @Override
            public void scoreChanged(float score) {
                if (score < 1.0f)
                    rbService.setScore(1.0f);
            }
        });

        rbCompany.setOnScoreChanged(new CustomRatingBar.IRatingBarCallbacks() {
            @Override
            public void scoreChanged(float score) {
                if (score < 1.0f)
                    rbCompany.setScore(1.0f);
            }
        });
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        // TODO Auto-generated method stub
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        if (isNotification) {
            titleBar.showBackButton();
        }
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.ratings));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);

        if(!isNotification){
            NotificationManager notificationManager = (NotificationManager) getDockActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
        }

        tvRatingTitle.setText(name != null ? name : "");
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
                int serviceRating = (int) Math.round(rbService.getScore());
                int companyRating = (int) Math.round(rbCompany.getScore());
                serviceHelper.enqueueCall(webService.rating(companyId, serviceRating, companyRating, orderId, prefHelper.getUser().getToken()),
                        WebServiceConstants.rating);

                break;
            default:
                break;
        }
    }

    @Override
    public void ResponseSuccess(Object result, String tag, String message) {
        switch (tag) {
            case WebServiceConstants.rating:
                UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.rating_submitted));
                getDockActivity().popFragment();
                break;
            default:
                break;
        }
    }


}