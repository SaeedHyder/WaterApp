package com.ingic.waterapp.retrofit;


import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OKHttpClientCreator {

    private static NotificationManager mNotifyManager;
    private static NotificationCompat.Builder mBuilder;

    /*public static OkHttpClient createCustomInterceptorClient(Context context) {

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new CustomInterceptor(progressListener))
                .build();


        return client;


    }*/

    public static OkHttpClient createCustomInterceptorClient(Context context) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                /*.addNetworkInterceptor(new CustomInterceptor(progressListener))*/
                .addInterceptor(loggingInterceptor)
                .build();
    }

    public static OkHttpClient createDefaultInterceptorClient(Context context) {

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        return originalResponse.newBuilder()
                                .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                                .build();
                    }
                })
                .build();


        return client;

    }

    final static ProgressResponseBody.ProgressListener progressListener = new ProgressResponseBody.ProgressListener() {
        @Override
        public void update(long bytesRead, long contentLength, boolean done) {
            int percent = (int) ((100 * bytesRead) / contentLength);
        }
    };

}
