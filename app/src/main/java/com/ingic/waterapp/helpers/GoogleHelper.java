package com.ingic.waterapp.helpers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import com.ingic.waterapp.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.ingic.waterapp.fragments.abstracts.BaseFragment;


public class GoogleHelper implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = GoogleHelper.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    public static GoogleHelper mGoogleHelper;
    private GoogleApiClient mGoogleApiClient;
    private GoogleHelperInterfce googleHelperInterface;
    private BaseFragment context;
    private GoogleSignInOptions gso;
    private boolean isGoogleSign;
    private ProgressDialog progressDialog;

    public static GoogleHelper getInstance() {

        if (mGoogleHelper == null) {
            mGoogleHelper = new GoogleHelper();
        }
        return mGoogleHelper;
    }

    public void DisconnectGoogleApi() {

        mGoogleApiClient.stopAutoManage(context.getActivity());
        mGoogleApiClient.disconnect();
    }

    public void ConnectGoogleAPi() {
        mGoogleApiClient.connect();
    }

    public void configGoogleApiClient(BaseFragment activity, GoogleHelperInterfce interfce) {
        this.context = activity;
        this.googleHelperInterface = interfce;
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();

        this.mGoogleApiClient = new GoogleApiClient.Builder(context.getContext())
                .enableAutoManage(context.getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public void configGoogleApiClient(BaseFragment activity) {
        this.context = activity;
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();

        this.mGoogleApiClient = new GoogleApiClient.Builder(context.getContext())
                .enableAutoManage(context.getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public void setGoogleHelperInterface(GoogleHelperInterfce interfce) {
        this.googleHelperInterface = interfce;

    }

    public Scope[] setScopes() {
        return this.gso.getScopeArray();
    }

    public void intentGoogleSign() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        context.startActivityForResult(signInIntent, RC_SIGN_IN);
        showProgressDialog();
    }

    public void googleSignOut() {
        // showProgressDialog();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        hideProgressDialog();
                        setGoogleSign(false);
                    }
                });
    }

    public void googleRevokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        hideProgressDialog();
                        setGoogleSign(false);
                    }
                });
    }

    public boolean isGoogleSign() {
        return isGoogleSign;
    }

    private void setGoogleSign(boolean isSignin) {
        this.isGoogleSign = isSignin;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        hideProgressDialog();
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }


    private void showProgressDialog() {
        progressDialog = ProgressDialog.show(context.getContext(), "", context.getString(R.string.please_wait), true);
    }

    private void hideProgressDialog() {
        if (this.progressDialog != null)
            this.progressDialog.dismiss();
    }


    public void checkGoogleSeesion() {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            setGoogleSign(true);
            googleHelperInterface.onGoogleSignInResult(result.getSignInAccount());

        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    setGoogleSign(true);
                    googleHelperInterface.onGoogleSignInResult(googleSignInResult.getSignInAccount());
                }
            });
        }
    }

    public void handleGoogleResult(int requestCode, int resultCode, Intent data) {
        hideProgressDialog();
        if (requestCode == RC_SIGN_IN) {
            hideProgressDialog();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d(TAG, "handleGoogleSignInResult:" + result.isSuccess());
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();
                hideProgressDialog();
                setGoogleSign(true);
                googleHelperInterface.onGoogleSignInResult(acct);

            } else {
                hideProgressDialog();
                UIHelper.showShortToastInCenter(context.getContext(), "Something went wrong try again");
                setGoogleSign(false);
            }
        }
    }

    public interface GoogleHelperInterfce {
        void onGoogleSignInResult(GoogleSignInAccount result);
    }
}


