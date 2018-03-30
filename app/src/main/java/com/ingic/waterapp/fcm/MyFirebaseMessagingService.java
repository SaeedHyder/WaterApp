/*
 * Created by Ingic on 12/13/17 7:13 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 10/19/17 6:51 PM
 */

package com.ingic.waterapp.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ingic.waterapp.R;
import com.ingic.waterapp.activities.MainActivity;
import com.ingic.waterapp.global.AppConstants;
import com.ingic.waterapp.helpers.BasePreferenceHelper;

import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    //    OnBadgeCountChange onBadgeCountChange;
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        BasePreferenceHelper prefHelper = new BasePreferenceHelper(this);
        if (prefHelper.getUser() == null || !prefHelper.isLogin()) {
            Log.d(TAG, "prefHelper data null: ");
            return;
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData() != null && remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> msgData = remoteMessage.getData();
            String msg = msgData.get("message");
            String title = msgData.get("title");
            String type = msgData.get("type");
            String company_name = msgData.get("company_name");
            String company_id = msgData.get("company_id");
            Log.e(TAG, "onMessageReceived: msg->" + msg + " title->" + title);
            sendDefaultNotification(title, msg, company_name, company_id, type);
/*
            //for blocking user
            if (type.equalsIgnoreCase(AppConstant.ADMIN_BLOCKED)) {
//            //broadcast hit: log out api, then clear preference
                Log.d(TAG, "onMessageReceived: Type : " + type);
                hitBlockUserBroadCast(type);
            }
            //for updating view count
            hitViewCountBroadCast(Util.getParsedInteger(badge) , Util.getParsedInteger(totalCount));*/
        } else if (remoteMessage.getNotification() != null) {   //Check if message contains a notification payload.
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            String msg = remoteMessage.getNotification().getBody();
            String title = remoteMessage.getNotification().getTitle();
            sendDefaultNotification(title, msg, "", "", "");
        }
    }


    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param title
     * @param msg         FCM message body received.
     * @param bottle_name
     * @param company_id
     * @param type
     */
    private void sendDefaultNotification(String title, String msg, String bottle_name, String company_id, String type) {
        if (TextUtils.isEmpty(title)) title = getResources().getString(R.string.app_name);
        PendingIntent pendingIntent = null;
        Intent intent = null;
        if (!type.isEmpty() && type.equalsIgnoreCase(AppConstants.RATING)) {
            intent = new Intent(this, MainActivity.class);
            intent.putExtra(AppConstants.RATING_BOTTLE, bottle_name);
            intent.putExtra(AppConstants.RATING_COMPANY_ID, company_id);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

        }
//        NotificationHelper.getInstance().showNotification(this, R.mipmap.ic_launcher, title, msg,
//                String.valueOf(System.currentTimeMillis()), intent);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,
//                this.getResources().getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentText(msg)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setDefaults(Notification.DEFAULT_ALL)  //to show alert notification
                .setPriority(Notification.PRIORITY_HIGH)  //on top of app
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Random rn = new Random();
        int range = 10000 - 1 + 1;
        int randomNum = rn.nextInt(range) + 1;

        notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
    }
/*
    //broadcasting
    private void updateCount(int count) {
        //app badge count

        onBadgeCountChange.onBadgeCountChange(count);
    }

    private void hitViewCountBroadCast(int badgeCount, int totalCount) {
        Intent intent = new Intent();
        intent.setAction(AppConstant.VIEW_COUNT_BROADCAST);
        intent.putExtra(AppConstant.VIEW_COUNT, badgeCount);
        intent.putExtra(AppConstant.TOTAL_VIEW_COUNT, totalCount);
        this.sendBroadcast(intent);
    }*/
}