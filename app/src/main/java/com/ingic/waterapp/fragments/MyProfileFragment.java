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
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.ingic.waterapp.BuildConfig;
import com.ingic.waterapp.R;
import com.ingic.waterapp.entities.CompanyEnt;
import com.ingic.waterapp.entities.UserEnt;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;
import com.ingic.waterapp.global.WebServiceConstants;
import com.ingic.waterapp.helpers.ImageLoaderHelper;
import com.ingic.waterapp.helpers.TextViewHelper;
import com.ingic.waterapp.helpers.UIHelper;
import com.ingic.waterapp.interfaces.ProfileUpdateListener;
import com.ingic.waterapp.ui.views.AnyEditTextView;
import com.ingic.waterapp.ui.views.AnyTextView;
import com.ingic.waterapp.ui.views.TitleBar;
import com.ingic.waterapp.ui.views.Util;
import com.squareup.picasso.Picasso;
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

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
@RuntimePermissions
public class MyProfileFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = MyProfileFragment.class.getSimpleName();
    public static final int TRUE = 1;
    public static final int FALSE = 0;


    Unbinder unbinder;
    @BindView(R.id.et_profile_name)
    AnyEditTextView etName;
    @BindView(R.id.et_profile_email)
    AnyEditTextView etEmail;
    @BindView(R.id.et_profile_address)
    AnyEditTextView etAddress;
    @BindView(R.id.et_profile_phone)
    AnyEditTextView etPhoneNumber;

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
    @BindView(R.id.btn_profile_update)
    Button btnUpdate;


    List<CompanyEnt> companyEnts;
    int companyId = -1;

    //FOr Camera
    private static final int CAMERA_REQUEST = 1001;
    private String mCurrentPhotoPath;
    private File fileUrl;
    private Snackbar snackbar;
    private View rootView;
    static ProfileUpdateListener mListener;

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
        rootView = inflater.inflate(R.layout.fragment_my_profile, container, false);
        return rootView;
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
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getResources().getString(R.string.my_profile));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        serviceHelper.enqueueCall(webService.getCompany(),
                WebServiceConstants.getCompanies);
        setListeners();
        setData();

    }

    private void setData() {
        if (prefHelper.getUser() != null) {

            TextViewHelper.setText(etName, prefHelper.getUser().getFullName());
            TextViewHelper.setText(etEmail, prefHelper.getUser().getEmail());
            TextViewHelper.setText(etAddress, prefHelper.getUser().getLocation());
            TextViewHelper.setText(etPhoneNumber, prefHelper.getUser().getMobileNo());
            TextViewHelper.setText(tvSelectSupplier, prefHelper.getUser().getCompanyName());
            int push = prefHelper.getUser().getPushNotification();
            if (push == TRUE) {
                btnNotification.setSelected(false);
            } else
                btnNotification.setSelected(true);


            if (prefHelper.getUser().getProfileImage() != null && prefHelper.getUser().getProfileImage().length() > 0) {
                Picasso.with(getDockActivity())
                        .load(prefHelper.getUser().getProfileImage())
                        .into(imgProfile);
            }
        } else if (prefHelper.getGuestTOKEN() != null) {
            imgProfile.setImageResource(R.drawable.user);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDockActivity().getDrawerLayout() != null)
            getDockActivity().lockDrawer();
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
                openDialog();
                break;

            case R.id.img_profile_camera:
                if (Util.doubleClickCheck())
                    MyProfileFragmentPermissionsDispatcher.getStoragePermissionWithPermissionCheck(MyProfileFragment.this);
//                notImplemented();
                break;

            case R.id.btn_profile_update:
                if (isValidate()) {
                    String fullName = etName.getText().toString();
                    String email = etEmail.getText().toString();
                    String phoneNo = etPhoneNumber.getText().toString();
                    int mCompanyId = (companyId == -1) ? Util.getParsedInteger(prefHelper.getUser().getCompanyId()) : companyId;
                    String location = etAddress.getText().toString();
                    int push = btnNotification.isSelected() ? 1 : 0;
                    callService(fullName, email, phoneNo, location, mCompanyId, push, prefHelper.getUser().getToken());
                }
                break;
            default:
                break;
        }
    }

    private void callService(String _fullName, String _email, String _mobileNo,
                             String _location, int _companyId, int _pushNotification, String _token) {
        MultipartBody.Part body = null;
        if (fileUrl != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileUrl);
            body = MultipartBody.Part.createFormData("profile_picture", fileUrl.getName(), requestFile);
        }

        RequestBody full_name = RequestBody.create(MediaType.parse("text/plain"), _fullName);
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), _email);
        RequestBody mobile_no = RequestBody.create(MediaType.parse("text/plain"), _mobileNo);
        RequestBody location = RequestBody.create(MediaType.parse("text/plain"), _location);
        RequestBody company_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(_companyId));
        RequestBody push_notification = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(_pushNotification));
//        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), _token);

        serviceHelper.enqueueCall(webService.updateProfile(body, full_name, email,
                mobile_no, location, company_id, push_notification, _token), WebServiceConstants.updateProfile);
    }

    @Override
    public void ResponseSuccess(Object result, String tag, String message) {
        switch (tag) {
            case WebServiceConstants.updateProfile:
                UserEnt login = (UserEnt) result;
                prefHelper.putUser(login);
                mListener.profileUpdate();
                break;
            case WebServiceConstants.getCompanies:
                companyId = -1;
                companyEnts = (List<CompanyEnt>) result;

                break;
        }

    }

    private boolean isValidate() {
        if (etName.testValidity() && etAddress.testValidity()) {
            if (checkPhoneLength()) {
                return true;
            } else
                etPhoneNumber.setError("Please Enter valid phone number");

        }// && checkPhoneLength());
        return false;
    }

    private boolean checkPhoneLength() {
        return (etPhoneNumber.length() > 9 || etPhoneNumber.length() < 1);
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
}
