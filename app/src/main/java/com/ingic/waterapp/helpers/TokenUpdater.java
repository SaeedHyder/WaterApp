package com.ingic.waterapp.helpers;

import android.content.Context;
import android.util.Log;

import com.ingic.waterapp.entities.ResponseWrapper;
import com.ingic.waterapp.global.WebServiceConstants;
import com.ingic.waterapp.retrofit.WebService;
import com.ingic.waterapp.retrofit.WebServiceFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created on 5/15/2017.
 */

public class TokenUpdater {
    private static final TokenUpdater tokenUpdater = new TokenUpdater();
    private WebService webservice;

    private TokenUpdater() {
    }

    public static TokenUpdater getInstance() {
        return tokenUpdater;
    }

    public void UpdateToken(Context context, final String userToken, String DeviceType, String Token) {
        if (Token.isEmpty()) {
            Log.e("Token Updater", "Token is Empty");
        }
        webservice = WebServiceFactory.getWebServiceInstanceWithCustomInterceptor(context,
                WebServiceConstants.BASE_URL);

        Call<ResponseWrapper> call = webservice.updateToken(Token, DeviceType,userToken );
        call.enqueue(new Callback<ResponseWrapper>() {
            @Override
            public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                if (response.body()!=null)
                Log.i("UPDATETOKEN", response.body().getResponse() + "" + userToken);
            }

            @Override
            public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                Log.e("UPDATETOKEN", t.toString());
            }
        });
    }

}
