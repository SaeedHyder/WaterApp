package com.ingic.waterapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.ingic.waterapp.R;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.ui.views.TitleBar;

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
    @BindView(R.id.btn_submit)
    Button btnSubmit;


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
                } else
                    getDockActivity().replaceDockableFragment(SetNewPasswordFragment.newInstance(), SetNewPasswordFragment.class.getSimpleName());
            }
        });
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

