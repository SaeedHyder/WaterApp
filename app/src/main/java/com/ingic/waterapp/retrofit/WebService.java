package com.ingic.waterapp.retrofit;

import com.ingic.waterapp.entities.CmsEnt;
import com.ingic.waterapp.entities.CompanyDetails;
import com.ingic.waterapp.entities.CompanyEnt;
import com.ingic.waterapp.entities.GuestEnt;
import com.ingic.waterapp.entities.NotificationCountEnt;
import com.ingic.waterapp.entities.ResponseWrapper;
import com.ingic.waterapp.entities.SettingsEnt;
import com.ingic.waterapp.entities.UserEnt;
import com.ingic.waterapp.entities.myOrder.InProgressOrderEnt;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface WebService {

    @FormUrlEncoded
    @POST("user/register")
    Call<ResponseWrapper<UserEnt>> signUp(
            @Field("full_name") String full_name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("password_confirmation") String password_confirmation,
            @Field("company_id") String company_id,
            @Field("device_token") String device_token,
            @Field("device_type") String device_type
    );

    @FormUrlEncoded
    @POST("user/login")
    Call<ResponseWrapper<UserEnt>> login(
            @Field("email") String email,
            @Field("password") String password,
            @Field("device_token") String device_token,
            @Field("device_type") String device_type
    );

    @Multipart
    @POST("user/updateProfile")
    Call<ResponseWrapper<UserEnt>> updateProfile(
            @Part MultipartBody.Part profile_picture,
            @Part("full_name") RequestBody full_name,
            @Part("email") RequestBody email,
            @Part("mobile_no") RequestBody mobile_no,
            @Part("location") RequestBody location,
            @Part("company_id") RequestBody company_id,
            @Part("push_notification") RequestBody push_notification,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("user/userFacebookLogin")
    Call<ResponseWrapper<UserEnt>> userFacebookLogin(
            @Field("social_media_id") String social_media_id,
            @Field("social_media_platform") String social_media_platform,
            @Field("full_name") String full_name,
            @Field("email") String email,
            @Field("device_token") String device_token,
            @Field("device_type") String device_type
    );

    @GET("water/getCompany")
    Call<ResponseWrapper<List<CompanyEnt>>> getCompany();


//    @FormUrlEncoded
//    @POST("user/updateProfile")
//    Call<ResponseWrapper<UserEnt>> updateProfile(
//            @Field("full_name") String full_name,
//            @Field("email") String email,
//            @Field("mobile_no") String mobile_no,
//            @Field("location") String location,
//            @Field("latitude") String latitude,
//            @Field("longitude") String longitude,
//            @Field("profile_picture") String profile_picture,
//            @Field("company_id") String company_id,
//            @Field("push_notification") String push_notification,
//            @Header("token") String token
//    );

    @FormUrlEncoded
    @POST("user/changepassword")
    Call<ResponseWrapper<UserEnt>> changepassword(
            @Field("old_password") String old_password,
            @Field("password") String password,
            @Field("password_confirmation") String password_confirmation,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("user/forgotpassword")
    Call<ResponseWrapper<UserEnt>> forgotpassword(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("user/verifyCode")
    Call<ResponseWrapper<UserEnt>> verifyCode(
            @Field("verification_code") String verification_code,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("user/ForgotChangePassword")
    Call<ResponseWrapper<UserEnt>> ForgotChangePassword(
            @Field("password") String password,
            @Field("password_confirmation") String password_confirmation,
            @Header("token") String token
    );

    @GET("water/getCompanyProductAboutReview")
    Call<ResponseWrapper<CompanyDetails>> getCompanyProductAboutReview(
            @Query("user_type") String user_type,
            @Query("type_select") String type_select,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("user/guestUserToken")
    Call<ResponseWrapper<GuestEnt>> guestUserToken(@Field("project_name") String project_name);

    @FormUrlEncoded
    @POST("water/cancelorder")
    Call<ResponseWrapper> cancelOrder(@Field("order_id") String order_id,
                                      @Header("token") String token);

    @GET("user/logout")
    Call<ResponseWrapper> logout(@Header("token") String token);

    @GET("water/getInProgressOrder")
    Call<ResponseWrapper<List<InProgressOrderEnt>>> myOrderInProgress(@Header("token") String token);

    @GET("water/getDeliveredOrder")
    Call<ResponseWrapper<List<InProgressOrderEnt>>> myOrderDelivered(@Header("token") String token);

    @GET("water/getSetting")
    Call<ResponseWrapper<SettingsEnt>> settings(@Header("token") String token);

    @GET("cms/getCms")
    Call<ResponseWrapper<CmsEnt>> getCms(@Query("type") String type);

    @GET("getnotifications")
    Call<ResponseWrapper<List<NotificationCountEnt>>> getNotifications(@Header("token") String token);

    @GET("getUnreadNotificationsCount")
    Call<ResponseWrapper<NotificationCountEnt>> getUnreadNotificationsCount(@Header("token") String token);


    @FormUrlEncoded
    @POST("water/createOrder")
    Call<ResponseWrapper> createOrder(
            @Field("company_id") int company_id,
            @Field("address") String address,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("date") String date,
            @Field("time_slot") String time_slot,
            @Field("cost") String cost,
            @Field("service_charge") String service_charge,
            @Field("vat_tax") String vat_tax,
            @Field("total") String total,
            @Field("order_product") String order_product,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("water/createReorder")
    Call<ResponseWrapper> reorder(
            @Field("order_id") int order_id,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("water/cancelorder")
    Call<ResponseWrapper> cancelOrder(
            @Field("order_id") int order_id,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("cms/contactus")
    Call<ResponseWrapper> feedback(
            @Field("user_type") String user_type,
            @Field("feedback") String feedback,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("user/updateDeviceToken")
    Call<ResponseWrapper> updateToken(
            @Field("device_token") String device_token,
            @Field("device_type") String device_type,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("water/companyReview")
    Call<ResponseWrapper> rating(
            @Field("company_id") int companyId,
            @Field("service_rate") int serviceRating,
            @Field("company_rate") int companyRating,
            @Header("token") String token
    );
}