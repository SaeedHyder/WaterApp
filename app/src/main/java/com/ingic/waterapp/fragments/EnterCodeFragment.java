package com.ingic.waterapp.fragments;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.ingic.waterapp.R;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.global.WebServiceConstants;
import com.ingic.waterapp.ui.views.AnyTextView;
import com.ingic.waterapp.ui.views.TitleBar;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class EnterCodeFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.et_enterPin_pinEntryEditText)
    PinEntryEditText etPinEditText;
    @BindView(R.id.tv_enterPin_didnotGetCodeText)
    AnyTextView tvCountDown;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    private CountDownTimer countDown;


    public EnterCodeFragment() {
        // Required empty public constructor
    }

    public static EnterCodeFragment newInstance() {
        return new EnterCodeFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enter_code, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etPinEditText.length() < 4) {
                    etPinEditText.setError(getString(R.string.please_enter_pin));
                } else {

                    serviceHelper.enqueueCall(webService.verifyCode(etPinEditText.getText().toString(), prefHelper.getUser().getToken()),
                            WebServiceConstants.forgotPassword);
                }
            }
        });

        countDown = new CountDownTimer(300000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvCountDown.setText("Didn't get a code yet? Wait for " + String.format("%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)) + ":" +
                        String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))) + " seconds");
            }

            @Override
            public void onFinish() {
                tvCountDown.setText("00:00");
//                if (Util.isNotNullEmpty(prefHelper.getUser().getEmail())) {
//                    resendPin(prefHelper.getUser().getEmail());
//                    countDown.start();
//                } else
//                    UIHelper.showShortToastInCenter(getDockActivity(), "Email is empty*");
            }
        }.start();
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {

            case WebServiceConstants.forgotPassword:
                getDockActivity().popBackStackTillEntry(1);
                getDockActivity().replaceDockableFragment(SetNewPasswordFragment.newInstance(), SetNewPasswordFragment.class.getSimpleName());

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


    @Override
    public void onDestroy() {
        countDown.cancel();
        unbinder.unbind();
        super.onDestroy();
    }
}

