package com.ingic.waterapp.helpers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.View;



public class SnackBarUtil {

    private static Snackbar snackbar;

    public static void showCameraSettingSnackBar(final Activity appContext, View view, String msg) {
        snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Settings", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startInstalledAppDetailsActivity(appContext);
            }
        });
        snackbar.show();
    }

    private static void startInstalledAppDetailsActivity(final Activity context) {
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
