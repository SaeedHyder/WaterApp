package com.ingic.waterapp.fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ingic.waterapp.R;
import com.ingic.waterapp.entities.CreateOrder;
import com.ingic.waterapp.entities.cart.DataHelper;
import com.ingic.waterapp.entities.cart.MyCartModel;
import com.ingic.waterapp.entities.cart.MyCartModelForJson;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.global.AppConstants;
import com.ingic.waterapp.global.WebServiceConstants;
import com.ingic.waterapp.helpers.TextViewHelper;
import com.ingic.waterapp.helpers.UIHelper;
import com.ingic.waterapp.ui.views.AnyTextView;
import com.ingic.waterapp.ui.views.TitleBar;
import com.ingic.waterapp.ui.views.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
//import com.google.android.gms.location.places.Places;
//import com.google.android.gms.location.places.ui.PlacePicker;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmationFragment extends BaseFragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    Unbinder unbinder;
    @BindView(R.id.tv_confirmation_address)
    AnyTextView tvAddress;
    @BindView(R.id.tv_confirmation_date)
    AnyTextView tvDate;
    @BindView(R.id.tv_confirmation_timeSlot)
    AnyTextView tvTimeSlot;
    @BindView(R.id.tv_confirmation_deliveryText)
    AnyTextView tvDeliveryText;
    @BindView(R.id.tv_confirmation_totalAmount)
    AnyTextView tvTotalAmount;
    @BindView(R.id.btn_confirmation_continue)
    Button btnContinue;

    //Place picker
    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 1;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private CreateOrder cartObj;
    List<MyCartModel> cartList = new ArrayList<>();
    private String timeSlot, date, address;
    private double lat, lng;

    public ConfirmationFragment() {
        // Required empty public constructor
    }

    public static ConfirmationFragment newInstance() {
        return new ConfirmationFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirmation, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        if (getArguments() != null) {
            cartObj = (CreateOrder) getArguments().getSerializable(AppConstants.CART_OBJ);
            cartList = Parcels.unwrap(getArguments().getParcelable(AppConstants.CART_SELECTED_LIST));
        }
        setListener();
        mGoogleApiClient = new GoogleApiClient.Builder(getDockActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getDockActivity(), this)
                .build();

    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity()); //To avoid auto manage when fragment pop
        mGoogleApiClient.disconnect();
    }

    private void setListener() {
        tvAddress.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        tvTimeSlot.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        // TODO Auto-generated method stub
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getResources().getString(R.string.confirmation));
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
            case R.id.tv_confirmation_address:
                if (Util.doubleClickCheck())
                    openPlacesPicker();
                break;
            case R.id.tv_confirmation_date:
                if (Util.doubleClickCheck())
                    openCalender();
                break;
            case R.id.tv_confirmation_timeSlot:
                if (Util.doubleClickCheck())
                    openTimePicker();
                break;
            case R.id.btn_confirmation_continue:
                if (Util.doubleClickCheck())
                    if (!(tvAddress.getText().toString().isEmpty()
                            || tvDate.getText().toString().isEmpty()
                            || tvTimeSlot.getText().toString().isEmpty())) {
                        if (cartList != null) {
                            Gson gson = new GsonBuilder().create();

                            String orderList = gson.toJson(getMyList(cartList));//gson.toJson(cartList);
                            Log.d("GSON", "onClick: orderListjson" + orderList);
                            serviceHelper.enqueueCall(webService.createOrder(cartObj.getCompany_id()
                                    , address, String.valueOf(lat), String.valueOf(lng),
                                    date, timeSlot, cartObj.getCost(),
                                    cartObj.getService_charge(), cartObj.getVat_tax()
                                    , cartObj.getTotal(), orderList.toString(), prefHelper.getUser().getToken())
                                    , WebServiceConstants.createOrder);
                        }
                    } else
                        UIHelper.showShortToastInCenter(getDockActivity(), getResources().getString(R.string.fill_imformation));
                break;
            default:
                break;
        }
    }

    private List<MyCartModelForJson> getMyList(List<MyCartModel> cartList) {
        List<MyCartModelForJson> mList = new ArrayList<>();
        for (MyCartModel obj : cartList) {
            mList.add(new MyCartModelForJson(String.valueOf(obj.getProductId()),
                    obj.getProductQuantity(), obj.getProductAmount()));
        }
        return mList;
    }

    private void openPlacesPicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(getDockActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void openCalender() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        final DatePickerDialog datePickerDialog = new DatePickerDialog(getDockActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        date = String.format("%02d", dayOfMonth) + "-"
                                + String.format("%02d", monthOfYear + 1) +
                                "-" + year;
                        tvDate.setText(String.format("%02d", dayOfMonth) + "-"
                                + String.format("%02d", monthOfYear + 1) +
                                "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void openTimePicker() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getDockActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                timeSlot = String.format("%02d", selectedHour) + String.format("%02d", selectedMinute);
                tvTimeSlot.setText(String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute));
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar.make(tvAddress, connectionResult.getErrorMessage() + "", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getDockActivity());
                String placeName = String.format("%s", place.getName());
                address = String.format("%s", place.getAddress());
                lat = place.getLatLng().latitude;
                lng = place.getLatLng().longitude;

                TextViewHelper.setText(tvAddress, address);
            }
        }
    }

    @Override
    public void ResponseSuccess(Object result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.createOrder:
                DataHelper.deleteItemsAsync(getIds(cartList), getDockActivity());
                getDockActivity().popBackStackTillEntry(1);
                break;
        }
    }

    private Collection<Long> getIds(List<MyCartModel> cartList) {
        Collection<Long> mList = new ArrayList<>();
        for (MyCartModel obj : cartList) {
            mList.add(obj.getId());
        }
        return mList;
    }
/*
    @Override
    protected Converter createConverter() {
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();

        return new GsonConverter(gson);
    }*/
}

