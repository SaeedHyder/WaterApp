package com.ingic.waterapp.retrofit;

import com.ingic.waterapp.entities.CompanyDetails;
import com.ingic.waterapp.entities.CompanyEnt;
import com.ingic.waterapp.entities.GuestEnt;
import com.ingic.waterapp.entities.ResponseWrapper;
import com.ingic.waterapp.entities.UserEnt;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
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


    @FormUrlEncoded
    @POST("user/updateProfile")
    Call<ResponseWrapper<UserEnt>> updateProfile(
            @Field("full_name") String full_name,
            @Field("email") String email,
            @Field("mobile_no") String mobile_no,
            @Field("location") String location,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("profile_picture") String profile_picture,
            @Field("company_id") String company_id,
            @Field("push_notification") String push_notification,
            @Header("token") String token
    );

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

    @GET("user/logout")
    Call<ResponseWrapper> logout(@Header("token") String token);

}