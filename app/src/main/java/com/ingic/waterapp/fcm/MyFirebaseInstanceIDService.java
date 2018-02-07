/*
 * Created by Ingic on 12/13/17 7:13 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 12/13/17 7:12 PM
 */

package com.ingic.waterapp.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.ingic.waterapp.global.AppConstants;
import com.ingic.waterapp.helpers.BasePreferenceHelper;
import com.ingic.waterapp.helpers.TokenUpdater;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    protected BasePreferenceHelper preferenceHelper;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "sendRegistrationToServer: **onTokenRefresh** " +refreshedToken);

        preferenceHelper = new BasePreferenceHelper(getApplicationContext());
        // sending gcm token to server
        if (preferenceHelper.getUser() != null)
            sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(final String token) {
        Log.e(TAG, "sendRegistrationToServer: " + token);
        /*todo : refresh token */
        TokenUpdater.getInstance().UpdateToken(getApplicationContext(),
                preferenceHelper.getUser().getToken(), AppConstants.Device_Type ,token );
    }
}