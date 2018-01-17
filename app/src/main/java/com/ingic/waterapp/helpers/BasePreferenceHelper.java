package com.ingic.waterapp.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public class BasePreferenceHelper extends PreferenceHelper {

    private static final String KEY_LOGIN_TYPE = "keyLoginType";
    private Context context;

    protected static final String KEY_LOGIN_STATUS = "islogin";

    private static final String FILENAME = "preferences";

    protected static final String Firebase_TOKEN = "Firebasetoken";

    protected static final String NotificationCount = "NotificationCount";


    public BasePreferenceHelper(Context c) {
        this.context = c;
    }

    public SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(FILENAME, Activity.MODE_PRIVATE);
    }

    public void setLoginStatus( boolean isLogin ) {
        putBooleanPreference( context, FILENAME, KEY_LOGIN_STATUS, isLogin );
    }

    public void setLoginType( int type ) {
        putIntegerPreference( context, FILENAME, KEY_LOGIN_TYPE, type );
    }

    public int getLoginTYpe() {
        return getIntegerPreference(context, FILENAME, KEY_LOGIN_TYPE);
    }

    public boolean isLogin() {
        return getBooleanPreference(context, FILENAME, KEY_LOGIN_STATUS);
    }


    public String getFirebase_TOKEN() {
        return getStringPreference(context, FILENAME, Firebase_TOKEN);
    }

    public void setFirebase_TOKEN(String _token) {
        putStringPreference(context, FILENAME, Firebase_TOKEN, _token);
    }
    public int getNotificationCount() {
        return getIntegerPreference(context, FILENAME, NotificationCount);
    }

    public void setNotificationCount(int count) {
        putIntegerPreference(context, FILENAME, NotificationCount, count);
    }


}
