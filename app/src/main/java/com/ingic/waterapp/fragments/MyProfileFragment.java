package com.ingic.waterapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.ingic.waterapp.R;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.ui.views.AnyEditTextView;
import com.ingic.waterapp.ui.views.AnyTextView;
import com.ingic.waterapp.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends BaseFragment implements View.OnClickListener {

    Unbinder unbinder;
    @BindView(R.id.et_profile_name)
    AnyEditTextView etName;
    @BindView(R.id.et_profile_email)
    AnyEditTextView etEmail;
    @BindView(R.id.et_profile_address)
    AnyEditTextView etAddress;
    @BindView(R.id.et_profile_phone)
    AnyEditTextView etPhoneNumber;

    @BindView(R.id.img_profile_camera)
    ImageView btnCamera;
    @BindView(R.id.img_profile_notification)
    ImageView btnNotification;
    @BindView(R.id.tv_profile_changePassword)
    AnyTextView tvChangePassword;
    @BindView(R.id.tv_profile_selectSupplier)
    AnyTextView tvSelectSupplier;
    @BindView(R.id.btn_profile_update)
    Button btnUpdate;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    public static MyProfileFragment newInstance() {
        return new MyProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        return view;
    }

    private void setListeners() {
        btnCamera.setOnClickListener(this);
        btnNotification.setOnClickListener(this);
        tvChangePassword.setOnClickListener(this);
        tvSelectSupplier.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        // TODO Auto-generated method stub
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(getResources().getString(R.string.my_profile));
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
            case R.id.img_profile_notification:
                if (btnNotification.isSelected()) {
                    btnNotification.setSelected(false);
                } else
                    btnNotification.setSelected(true);

                break;
            case R.id.tv_profile_changePassword:
                getDockActivity().replaceDockableFragment(ChangePasswordFragment.newInstance(), ChangePasswordFragment.class.getSimpleName());
                break;
            case R.id.tv_profile_selectSupplier:
                break;
            case R.id.img_profile_camera:
                notImplemented();
                break;
            case R.id.btn_profile_update:
                if (isValidate())
                    notImplemented();
                break;
            default:
                break;
        }
    }

    private boolean isValidate() {
        return etName.testValidity() && etEmail.testValidity() && etAddress.testValidity() && etPhoneNumber.testValidity();
    }


}
