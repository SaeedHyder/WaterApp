package com.ingic.waterapp.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.ingic.waterapp.BuildConfig;
import com.ingic.waterapp.R;
import com.ingic.waterapp.entities.CityEnt;
import com.ingic.waterapp.entities.CompanyEnt;
import com.ingic.waterapp.entities.UserEnt;
import com.ingic.waterapp.entities.cart.DataHelper;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.global.WebServiceConstants;
import com.ingic.waterapp.helpers.ImageLoaderHelper;
import com.ingic.waterapp.helpers.InternetHelper;
import com.ingic.waterapp.helpers.TextViewHelper;
import com.ingic.waterapp.helpers.UIHelper;
import com.ingic.waterapp.interfaces.ProfileUpdateListener;
import com.ingic.waterapp.ui.views.AnyEditTextView;
import com.ingic.waterapp.ui.views.AnyTextView;
import com.ingic.waterapp.ui.views.TitleBar;
import com.ingic.waterapp.ui.views.Util;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
@RuntimePermissions
public class MyProfileFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = MyProfileFragment.class.getSimpleName();
    public static final int TRUE = 1;
    public static final int FALSE = 0;
    //FOr Camera
    private static final int CAMERA_REQUEST = 1001;
    static ProfileUpdateListener mListener;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    Unbinder unbinder;
    @BindView(R.id.et_profile_name)
    AnyEditTextView etName;
    @BindView(R.id.et_profile_email)
    AnyTextView etEmail;
    @BindView(R.id.et_profile_address)
    AnyTextView etAddress;
    @BindView(R.id.et_profile_phone)
    AnyEditTextView etPhoneNumber;
    @BindView(R.id.et_profile_makaniNumber)
    AnyEditTextView etMakaniNumber;
    @BindView(R.id.img_profile)
    ImageView imgProfile;
    @BindView(R.id.img_profile_camera)
    ImageView btnCamera;
    @BindView(R.id.img_profile_notification)
    ImageView btnNotification;
    @BindView(R.id.tv_profile_changePassword)
    AnyTextView tvChangePassword;
    @BindView(R.id.tv_profile_selectSupplier)
    AnyTextView tvSelectSupplier;
    @BindView(R.id.tv_profile_selectCity)
    AnyTextView tvSelectCity;
    @BindView(R.id.btn_profile_update)
    Button btnUpdate;
    @BindView(R.id.mainFrameLayout)
    RelativeLayout mainFrameLayout;
    @BindView(R.id.seperator6)
    View seperator6;

    List<CompanyEnt> companyEnts;
    int companyId = -1;
    int cityId = -1;
    String cityName;
    private LatLng addressLatLng = null;
    //FOr cities
    //FOr cities
    private List<CityEnt> cityList; //todo change its type
    private String mCurrentPhotoPath;
    private File fileUrl;
    private Snackbar snackbar;
    private View rootView;


    private boolean isEdit = false;
    private ImageLoader imageLoader;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    public static MyProfileFragment newInstance(ProfileUpdateListener listener) {
        mListener = listener;
        return new MyProfileFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        imageLoader=ImageLoader.getInstance();
        rootView = inflater.inflate(R.layout.fragment_my_profile, container, false);
        return rootView;
    }

    private void setListeners() {
        btnCamera.setOnClickListener(this);
        btnNotification.setOnClickListener(this);
        tvChangePassword.setOnClickListener(this);
        tvSelectSupplier.setOnClickListener(this);
        tvSelectCity.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        etAddress.setOnClickListener(this);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        // TODO Auto-generated method stub
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.showEditButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableDisableViewGroup(mainFrameLayout, true);
                btnCamera.setVisibility(View.VISIBLE);
                btnUpdate.setVisibility(View.VISIBLE);

                isEdit=true;
                if (getTitleBar() != null) {
                    getTitleBar().hideEditButton();
                }
            }
        });
        titleBar.setSubHeading(getResources().getString(R.string.my_profile));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        serviceHelper.enqueueCall(webService.getCompany(),
                WebServiceConstants.getCompanies);


        serviceHelper.enqueueCall(webService.getCities(),
                WebServiceConstants.getCities);

        serviceHelper.enqueueCall(webService.getProfile(prefHelper.getUser().getToken())
                ,WebServiceConstants.getProfile);
        setListeners();


        if (!isEdit) {
            enableDisableViewGroup(mainFrameLayout, false);
            btnCamera.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.GONE);
        }

        getDockActivity().getSupportFragmentManager().addOnBackStackChangedListener(getListener());

    }

    private FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getDockActivity().getSupportFragmentManager();

                if (manager != null) {
                    BaseFragment currFrag = (BaseFragment) manager.findFragmentById(getDockActivity().getDockFrameLayoutId());
                    if (currFrag != null) {
                        if (currFrag instanceof MyProfileFragment) {
                            if(isEdit){
                                if (getTitleBar() != null) {
                                    getTitleBar().hideEditButton();
                                }
                            }
                        }
                    }
                }
            }
        };

        return result;
    }

    public static void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            view.setEnabled(enabled);
            if (view instanceof ViewGroup) {
                enableDisableViewGroup((ViewGroup) view, enabled);
            }
        }
    }

    private void setData() {

        if (prefHelper.getUser() != null) {

            if(prefHelper.isSocialLogin()){
                tvChangePassword.setVisibility(View.GONE);
                seperator6.setVisibility(View.GONE);
            }

            if (prefHelper.getUser().getEmail() != null && !prefHelper.getUser().getEmail().isEmpty())
                etEmail.setEnabled(false);
            else
                etEmail.setEnabled(true);

            TextViewHelper.setText(etName, prefHelper.getUser().getFullName());
            TextViewHelper.setText(etEmail, prefHelper.getUser().getEmail());
            TextViewHelper.setText(etAddress, prefHelper.getUser().getLocation());
            TextViewHelper.setText(etMakaniNumber, prefHelper.getUser().getMakaniNumber());
            TextViewHelper.setText(etPhoneNumber, prefHelper.getUser().getMobileNo());
            TextViewHelper.setText(tvSelectSupplier, prefHelper.getUser().getCompanyName());
            TextViewHelper.setText(tvSelectCity, prefHelper.getUser().getCityName());
            try {
                addressLatLng = new LatLng(Double.parseDouble(prefHelper.getUser().getLatitude()), Double.parseDouble(prefHelper.getUser().getLongitude()));
            } catch (Exception e) {
                e.printStackTrace();
                addressLatLng = null;
            }
            int push = prefHelper.getUser().getPushNotification();
            if (push == TRUE) {
                btnNotification.setSelected(true);
            } else
                btnNotification.setSelected(false);

            companyId = Integer.parseInt(prefHelper.getUser().getCompanyId());

            if (prefHelper.getUser().getProfileImage() != null && prefHelper.getUser().getProfileImage().length() > 0) {
                imageLoader.displayImage(prefHelper.getUser().getProfileImage(),imgProfile);
                /*Picasso.with(getDockActivity())
                        .load(prefHelper.getUser().getProfileImage())
                        .placeholder(R.drawable.placeholder)
                        .into(imgProfile);*/
            }
        } else if (prefHelper.getGuestTOKEN() != null) {
            imgProfile.setImageResource(R.drawable.user);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDockActivity().getDrawerLayout() != null) {
            getDockActivity().lockDrawer();
        }
    }

    private void openDialog() {

        if (companyEnts != null) {
            final String[] items = new String[companyEnts.size()];
            for (int i = 0; i < companyEnts.size(); i++) {
                items[i] = companyEnts.get(i).getFullName();
            }
            TimeSlotActionSheetDialog(items);
           /* AlertDialog.Builder builder = new AlertDialog.Builder(getDockActivity());
            builder.setTitle(R.string.select_supplier);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int pos) {
                    tvSelectSupplier.setText(items[pos]);
                    companyId = companyEnts.get(pos).getId();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();*/
        } else {
            serviceHelper.enqueueCall(webService.getCompany(),
                    WebServiceConstants.getCompanies);
        }
    }

    private void TimeSlotActionSheetDialog(final String[] stringItems) {
//        final String[] stringItems = {getResources().getString(R.string.morning),
//                getResources().getString(R.string.afternoon)};
        final ActionSheetDialog dialog = new ActionSheetDialog(getDockActivity(), stringItems, null);
        dialog.title(getResources().getString(R.string.select_supplier))
                .titleTextSize_SP(14.5f)
                .cancelText(getResources().getString(android.R.string.cancel))
                .show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                companyId = companyEnts.get(position).getId();
                TextViewHelper.setText(tvSelectSupplier, stringItems[position]);
                dialog.dismiss();
            }
        });
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
                getDockActivity().addDockableFragment(ChangePasswordFragment.newInstance(), ChangePasswordFragment.class.getSimpleName());
                break;
            case R.id.tv_profile_selectSupplier:
                openDialog();
                break;

            case R.id.tv_profile_selectCity:
                if (Util.doubleClickCheck2Seconds())
                    openCityDialog();
                break;
            case R.id.et_profile_address:
                if (Util.doubleClickCheck2Seconds())
                    openLocationSelector();
                break;
            case R.id.img_profile_camera:
                if (Util.doubleClickCheck())
                    MyProfileFragmentPermissionsDispatcher.getStoragePermissionWithPermissionCheck(MyProfileFragment.this);
//                notImplemented();
                break;

            case R.id.btn_profile_update:
                if (Util.doubleClickCheck2Seconds())

                    if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                        if (isValidate()) {
                            String fullName = etName.getText().toString();
                            String email = etEmail.getText().toString();
                            String phoneNo = etPhoneNumber.getText().toString();
                            int mCompanyId = (companyId == -1) ? Util.getParsedInteger(prefHelper.getUser().getCompanyId()) : companyId;
                            int mCityId = (cityId == -1) ? Util.getParsedInteger(prefHelper.getUser().getCityId()) : cityId;
                            String location = etAddress.getText().toString();
                            String makaniNumber = etMakaniNumber.getText().toString();
                            int push = btnNotification.isSelected() ? 1 : 0;

                            callService(fullName, email, phoneNo, location, addressLatLng, makaniNumber, mCompanyId, mCityId, push, prefHelper.getUser().getToken());
                        }
                    }
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

    private void callService(String _fullName, String _email, String _mobileNo,
                             String _location, LatLng _latLng, String _makaniNumber, int _companyId, int _cityId, int _pushNotification, String _token) {
        MultipartBody.Part body = null;
        if (fileUrl != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileUrl);
            body = MultipartBody.Part.createFormData("profile_picture", fileUrl.getName(), requestFile);
        }

        RequestBody full_name = RequestBody.create(MediaType.parse("text/plain"), _fullName);
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), _email);
        RequestBody mobile_no = RequestBody.create(MediaType.parse("text/plain"), _mobileNo);
        RequestBody location = RequestBody.create(MediaType.parse("text/plain"), _location);
        RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), _latLng.latitude + "");
        RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), _latLng.longitude + "");
        RequestBody makaniNumber = RequestBody.create(MediaType.parse("text/plain"), _makaniNumber);
        RequestBody company_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(_companyId));
        RequestBody city_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(_cityId));
        RequestBody push_notification = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(_pushNotification));
//        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), _token);

        serviceHelper.enqueueCall(webService.updateProfile(body, full_name, email,
                mobile_no, location, latitude, longitude, makaniNumber, company_id, city_id, push_notification, _token), WebServiceConstants.updateProfile);
    }

    @Override
    public void ResponseSuccess(Object result, String tag, String message) {
        switch (tag) {

            case WebServiceConstants.getProfile:
                UserEnt profile = (UserEnt) result;
                UserEnt previousProfile=prefHelper.getUser();
                previousProfile.setEmail(profile.getEmail());
                previousProfile.setFullName(profile.getFullName());
                previousProfile.setLocation(profile.getLocation());
                previousProfile.setMakaniNumber(profile.getMakaniNumber());
                previousProfile.setMobileNo(profile.getMobileNo());
                previousProfile.setCompanyName(profile.getCompanyName());
                previousProfile.setCityName(profile.getCityName());
                previousProfile.setLatitude(profile.getLatitude());
                previousProfile.setLongitude(profile.getLongitude());
                previousProfile.setPushNotification(profile.getPushNotification());
                previousProfile.setCompanyId(profile.getCompanyId());
                previousProfile.setProfileImage(profile.getProfileImage());

                prefHelper.putUser(previousProfile);
                if (mListener != null)
                    mListener.profileUpdate();
                setData();

                break;
            case WebServiceConstants.updateProfile:
                UserEnt login = (UserEnt) result;

                //Empty card
                if (Util.getParsedInteger(login.getCompanyId())
                        != Util.getParsedInteger(prefHelper.getUser().getCompanyId())) {
                    DataHelper.deleteRealmData();
                }

                prefHelper.putUser(login);
                if (mListener != null)
                    mListener.profileUpdate();

                UIHelper.showShortToastInCenter(getDockActivity(), message);
                isEdit = false;
                getDockActivity().popFragment();
                break;

            case WebServiceConstants.getCompanies:
                companyId = -1;
                companyEnts = (List<CompanyEnt>) result;
                if (prefHelper != null && prefHelper.getUser().getCompanyId() != null && !prefHelper.getUser().getCompanyId().isEmpty()) {
                    companyId = Integer.parseInt(prefHelper.getUser().getCompanyId());
                }

                break;

            case WebServiceConstants.getCities:
                cityList = (List<CityEnt>) result;
                break;
        }

    }

    private boolean isValidate() {
        if (etName.getText() == null || (etName.getText().toString().isEmpty())) {
            if (etName.requestFocus()) {
                setEditTextFocus(etName);
            }
            etName.setError(getString(R.string.error));
            return false;
        } /*else if (etEmail.getText() == null || (etEmail.getText().toString().isEmpty()) || (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches())) {
            if (etEmail.requestFocus()) {
                setEditTextFocus(etEmail);
            }
            etEmail.setError(getString(R.string.enter_email));
            return false;
        }*/ else if (etPhoneNumber.getText() == null || (etPhoneNumber.getText().toString().isEmpty())) {
            if (etPhoneNumber.requestFocus()) {
                setEditTextFocus(etPhoneNumber);
            }
            etPhoneNumber.setError(getString(R.string.error));
            return false;
        } else if (etAddress.getText() == null || (etAddress.getText().toString().isEmpty()) || addressLatLng == null) {
            UIHelper.showShortToastInCenter(getDockActivity(), getResources().getString(R.string.locationError));
            return false;
        } else if (tvSelectSupplier.getText() == null || (tvSelectSupplier.getText().toString().isEmpty())) {
            UIHelper.showLongToastInCenter(getDockActivity(), getString(R.string.please_select_company));
            return false;
        } else if (tvSelectCity.getText() == null || (tvSelectCity.getText().toString().isEmpty())) {
            UIHelper.showShortToastInCenter(getDockActivity(), getResources().getString(R.string.please_select_city));
            return false;
        } else {
            return true;
        }
    }

    private boolean checkPhoneLength() {
        return (etPhoneNumber.length() > 7 || etPhoneNumber.length() < 1);
    }

    //Camera Integration
    public void uploadFromCamera() {
        Intent pictureActionIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoURI;
        try {
            photoURI = FileProvider.getUriForFile(getDockActivity(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    createImageFile());
            if (photoURI != null) {
                pictureActionIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                MyProfileFragment.this.startActivityForResult(pictureActionIntent, CAMERA_REQUEST);
            }
        } catch (IOException e) {
            UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.failure_response));
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "WaterApp");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
//                changesMadeInProfileImage = true;
                Uri resultUri = result.getUri();
                fileUrl = new File(resultUri.getPath());
                ImageLoaderHelper.loadImage(resultUri.toString(), imgProfile);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e(TAG, "onActivityResult: " + error.getLocalizedMessage());
            }
        }
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Place place = PlaceAutocomplete.getPlace(getDockActivity(), data);
                if (place != null) {
                    etAddress.setError(null);
                    etAddress.setText(place.getAddress().toString());
                    addressLatLng = place.getLatLng();
                    Log.i(TAG, "Place: " + place.getName());
                }

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getDockActivity(), data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
//        fileUrl = CameraHelper.retrieveAndDisplayPicture(requestCode, resultCode, data, getDockActivity(),
//                imgUserProfile, mCurrentPhotoPath);
    }

    /**
     * Permission Dispatcher work
     */

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void getStoragePermission() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(getContext(), this);
//        CameraHelper.uploadPhotoDialog(MyProfileFragment.this, getDockActivity());
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showRationaleForStorage(final PermissionRequest request) {
        new AlertDialog.Builder(getDockActivity())
                .setMessage(R.string.permission_storage_rationale)
                .setPositiveButton(R.string.button_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.button_deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        request.cancel();
                    }
                })
                .show();
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showDeniedForStorage() {
        showSnackBar(getResources().getString(R.string.permission_storage_denied));
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showNeverAskForStorage() {
        showSnackBar(getResources().getString(R.string.permission_storage_neverask));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MyProfileFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private void showSnackBar(String msg) {
        snackbar = Snackbar.make(rootView, msg, Snackbar.LENGTH_LONG);
        snackbar.setAction("Settings", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startInstalledAppDetailsActivity(getDockActivity());
            }
        });
        snackbar.show();
    }

    private void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }



    private void openLocationSelector() {

        try {
           /* Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(getDockActivity());*/
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(getDockActivity()), PLACE_AUTOCOMPLETE_REQUEST_CODE);
            //this.startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (Exception e) {

            e.printStackTrace();
        }


    }

}
