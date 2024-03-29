package com.ingic.waterapp.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ingic.waterapp.R;
import com.ingic.waterapp.activities.MainActivity;
import com.ingic.waterapp.entities.CityEnt;
import com.ingic.waterapp.entities.CompanyEnt;
import com.ingic.waterapp.entities.UserEnt;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.global.AppConstants;
import com.ingic.waterapp.global.WebServiceConstants;
import com.ingic.waterapp.helpers.TextViewHelper;
import com.ingic.waterapp.helpers.UIHelper;
import com.ingic.waterapp.interfaces.SideMenuUpdate;
import com.ingic.waterapp.ui.views.AnyEditTextView;
import com.ingic.waterapp.ui.views.AnyTextView;
import com.ingic.waterapp.ui.views.TitleBar;
import com.ingic.waterapp.ui.views.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends BaseFragment implements View.OnClickListener {

    Unbinder unbinder;
    @BindView(R.id.et_signup_name)
    AnyEditTextView etName;
    @BindView(R.id.et_signup_email)
    AnyEditTextView etEmail;
    @BindView(R.id.et_signup_password)
    AnyEditTextView etPassword;
    @BindView(R.id.et_signup_confirmPassword)
    AnyEditTextView etConfirmPassword;
    @BindView(R.id.et_signUp_phone)
    AnyEditTextView etPhoneNumber;
    @BindView(R.id.tv_signup_selectSupplier)
    AnyTextView tvSelectSupplier;
    @BindView(R.id.tv_signup_selectCity)
    AnyTextView tvSelectCity;
    @BindView(R.id.btn_signup)
    Button btnSignUp;

    List<CompanyEnt> companyEnts;
    int companyId = 0;
//    int companyId = -1;

    //FOr cities
    private List<CityEnt> cityList; //todo change its type
    int cityId = -1;
    String cityName;

    SideMenuUpdate sideMenuUpdate;
    private String refreshToken;

    public SignUpFragment() {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        refreshToken = FirebaseInstanceId.getInstance().getToken();

        return view;
    }

    private void setListeners() {
        tvSelectSupplier.setOnClickListener(this);
        tvSelectCity.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        // TODO Auto-generated method stub
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.clearHeaderBackround();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        setListeners();

        serviceHelper.enqueueCall(webService.getCompany(),
                WebServiceConstants.getCompanies);

        serviceHelper.enqueueCall(webService.getCities(),
                WebServiceConstants.getCities);

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
            case R.id.btn_signup:
                if (isValidate()) {

                    serviceHelper.enqueueCall(webService.signUp(etName.getText().toString(),
                            etEmail.getText().toString(),
                            etPassword.getText().toString(),
                            etConfirmPassword.getText().toString(),
                            etPhoneNumber.getText().toString(),
                            cityId,
                            companyId + "",
                            refreshToken,
                            AppConstants.Device_Type),
                            WebServiceConstants.signUp);

                }
                break;

            case R.id.tv_signup_selectCity:
                if (Util.doubleClickCheck2Seconds())
                    openCityDialog();
                break;

            case R.id.tv_signup_selectSupplier:
                openDialog();
                break;

            default:
                break;
        }
    }

    private void openCityDialog() {

        if (cityList != null) {
            final String[] items = new String[cityList.size()];
            for (int i = 0; i < cityList.size(); i++) {
                items[i] = cityList.get(i).getCityName();
            }
            SelectCityActionSheetDialog(items);
        } else {
            serviceHelper.enqueueCall(webService.getCities(),
                    WebServiceConstants.getCities);
        }
    }

    private void SelectCityActionSheetDialog(final String[] stringItems) {
//        final String[] stringItems = {getResources().getString(R.string.morning),
//                getResources().getString(R.string.afternoon)};
        final ActionSheetDialog dialog = new ActionSheetDialog(getDockActivity(), stringItems, null);
        dialog.title(getResources().getString(R.string.select_city))
                .titleTextSize_SP(14.5f)
                .cancelText(getResources().getString(android.R.string.cancel))
                .show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                cityId = cityList.get(position).getId();
                cityName = stringItems[position];
                TextViewHelper.setText(tvSelectCity, stringItems[position]);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void ResponseSuccess(Object result, String tag, String message) {
        switch (tag) {

            case WebServiceConstants.signUp:
                UserEnt userEnt = (UserEnt) result;
                prefHelper.putUser(userEnt);
//                launchHomeFragment(AppConstants.REGISTERED_USER);
                launchMainActivity(AppConstants.REGISTERED_USER);
                break;

            case WebServiceConstants.getCompanies:
                companyId = 0;
//                companyId = -1;
                companyEnts = (List<CompanyEnt>) result;
                break;

            case WebServiceConstants.getCities:
                cityList = (List<CityEnt>) result;
                break;
        }
    }


    private void openDialog() {

        if (companyEnts != null) {
            final CharSequence[] items = new CharSequence[companyEnts.size()];
            for (int i = 0; i < companyEnts.size(); i++) {
                items[i] = companyEnts.get(i).getFullName();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getDockActivity());
            builder.setTitle(R.string.select_supplier);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int pos) {
                    tvSelectSupplier.setText(items[pos]);
                    companyId = companyEnts.get(pos).getId();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            serviceHelper.enqueueCall(webService.getCompany(),
                    WebServiceConstants.getCompanies);
        }
    }

    private boolean isValidate() {
        if (etName.testValidity() && etEmail.testValidity() && etPassword.testValidity() && etPhoneNumber.testValidity()) {
            if (checkPassword()) {
                if (checkPhoneLength()) {
                    if (!tvSelectCity.getText().toString().isEmpty()) {
                        return true;
                    } else
                        UIHelper.showShortToastInCenter(getDockActivity(), getResources().getString(R.string.please_select_city));
                } else
                    etPhoneNumber.setError("Please Enter valid phone number");

            }

//                if (companyId != -1)
//                    return true;
//                else
//                    UIHelper.showLongToastInCenter(getDockActivity(), getString(R.string.please_select_company));

        }
        return false;
    }

    private boolean checkPhoneLength() {
        return (etPhoneNumber.length() > 7 || etPhoneNumber.length() < 1);
    }
    /*
    *
    private boolean isValidate() {
        if (etName.testValidity() && etEmail.testValidity() && etPassword.testValidity()) {
            if (checkPassword()) {
                if (companyId != -1)
                    return true;
                else
                    UIHelper.showLongToastInCenter(getDockActivity(), getString(R.string.please_select_company));
            }
        }
        return false;
    }
    * */

    private void launchHomeFragment(int type) {
        prefHelper.setLoginStatus(true);
        prefHelper.setLoginType(type);
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.LOGIN_TYPE, type);
        fragment.setArguments(bundle);
        getDockActivity().popBackStackTillEntry(0);
        getDockActivity().replaceDockableFragment(fragment, "HomeFragment");
    }

    private void launchMainActivity(int type) {
        prefHelper.setLoginStatus(true);
        prefHelper.setLoginType(type);
        getDockActivity().startActivity(new Intent(getDockActivity(), MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }


    private boolean checkPassword() {
        if (etPassword.testValidity()
                || etConfirmPassword.testValidity()) {

            if (!passwordLength(etPassword.getText().toString())) {
//                UIHelper.showShortToastInCenter(getDockActivity(), getResources().getString(R.string.password_length));
                etPassword.setError(getResources().getString(R.string.password_length));
                return false;
            }
//        if (!passwordLength(ietConfirmPassword.getText().toString())){
//            ietConfirmPassword.setError(getString(R.string.password_length));
//            return false;
//        }
            if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(getResources().getString(R.string.error_passwords_do_not_match));
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean passwordLength(String pwd) {
        return (pwd.length() > 5);
    }


}
