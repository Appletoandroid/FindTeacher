package com.appleto.findteacher.retrofit2;

import com.appleto.findteacher.model.CityByStateListResponse;
import com.appleto.findteacher.model.SessionDetailsResponse;
import com.appleto.findteacher.model.StateListResponse;
import com.appleto.findteacher.model.StudentListByTIdResponse;
import com.appleto.findteacher.model.StudentPaymentHistoryResponse;
import com.appleto.findteacher.model.StudentSessionListResponse;
import com.appleto.findteacher.model.TeacherListResponse;
import com.appleto.findteacher.model.VideoListResponse;
import com.google.gson.JsonObject;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


/**
 * Created by Simon on 11/02/2019s.
 */
public interface ApiInterface {

    @FormUrlEncoded
    @POST("userLogin")
    Single<JsonObject> userLogin(@Field("Username") String Username,
                                 @Field("Password") String Password);

    @GET("getStudentById")
    Single<ResponseBody> getStudentById(@Query("id") String id);

    @FormUrlEncoded
    @POST("addStudent")
    Single<ResponseBody> addStudent(@Field("Name") String Name,
                                    @Field("Email") String Email,
                                    @Field("Password") String Password,
                                    @Field("Mobile") String Mobile,
                                    @Field("Gender") String Gender,
                                    @Field("Age") String Age);

    @FormUrlEncoded
    @POST("updateStudentLocation")
    Single<JsonObject> updateStudentLocation(@Field("City") String City,
                                             @Field("State") String State,
                                             @Field("S_ID") String S_ID);

    @GET("getTeacherById")
    Single<ResponseBody> getTeacherById(@Query("id") String id);

    @GET("getTeacherByStateCity")
    Single<TeacherListResponse> getTeacherByStateCity(@Query("state") String state, @Query("city") String city);

    @FormUrlEncoded
    @POST("requestTeacher")
    Single<JsonObject> requestTeacher(@Field("S_ID") String S_ID, @Field("T_ID") String T_ID);


    @GET("getSessionsByStudentId")
    Single<StudentSessionListResponse> getSessionsByStudentId(@Query("S_ID") String id);

    @GET("getSessionById")
    Single<SessionDetailsResponse> getSessionById(@Query("id") String id);

    @GET("getVideos")
    Single<VideoListResponse> getVideos(@Query("id") String id);

    @GET("getPaymentsByStudentId")
    Single<StudentPaymentHistoryResponse> getPaymentsByStudentId(@Query("S_ID") String S_ID);

    @GET("getStates")
    Single<StateListResponse> getStates();

    @GET("getCityByState")
    Single<CityByStateListResponse> getCityByState(@Query("State_Name") String State_Name);

    @GET("getStudentByTeacherIdAccepted")
    Single<StudentListByTIdResponse> getStudentByTeacherIdAccepted(@Query("T_ID") String T_ID);

    @GET("getStudentByTeacherIdPending")
    Single<StudentListByTIdResponse> getStudentByTeacherIdPending(@Query("T_ID") String T_ID);

    @FormUrlEncoded
    @POST("sessionCompleteInitiate")
    Single<JsonObject> sessionCompleteInitiate(@Field("ST_ID") String ST_ID);

    @FormUrlEncoded
    @POST("sessionCompleteVerify")
    Single<JsonObject> sessionCompleteVerify(@Field("ST_ID") String ST_ID,
                                             @Field("OTP") String OTP);

    @FormUrlEncoded
    @POST("paymentInitiate")
    Single<JsonObject> paymentInitiate(@Field("S_ID") String S_ID,
                                       @Field("Amount") String Amount);

    @FormUrlEncoded
    @POST("paymentVerify")
    Single<JsonObject> paymentVerify(@Field("P_ID") String P_ID,
                                     @Field("OTP") String OTP);

    @FormUrlEncoded
    @POST("updateStudentRequest")
    Single<JsonObject> updateStudentRequest(@Field("S_ID") String S_ID,
                                            @Field("T_ID") String T_ID,
                                            @Field("Status") String Status);

    @Multipart
    @POST("changeProfilePic")
    Single<JsonObject> changeProfile(@Part("U_ID") RequestBody U_ID,
                                     @Part MultipartBody.Part ProfilePic);

    @FormUrlEncoded
    @POST("forgetPassword")
    Single<JsonObject> forgetPassword(@Field("Email") String Email);

    @FormUrlEncoded
    @POST("changePassword")
    Single<JsonObject> changePassword(@Field("U_ID") String U_ID,
                                      @Field("Old_Password") String Old_Password,
                                      @Field("New_Password") String New_Password);

    @FormUrlEncoded
    @POST("updateStudent")
    Single<JsonObject> updateStudent(@Field("Name") String Name,
                                      @Field("Email") String Email,
                                      @Field("Mobile_No") String Mobile_No,
                                      @Field("Age") String Age,
                                      @Field("Gender") String Gender,
                                      @Field("City") String City,
                                      @Field("State") String State,
                                      @Field("S_ID") String S_ID,
                                      @Field("Email_Old") String Email_Old);

    @FormUrlEncoded
    @POST("updateTeacher")
    Single<JsonObject> updateTeacher(@Field("Name") String Name,
                                     @Field("Email") String Email,
                                     @Field("Mobile_No") String Mobile_No,
                                     @Field("Age") String Age,
                                     @Field("Gender") String Gender,
                                     @Field("City") String City,
                                     @Field("State") String State,
                                     @Field("T_ID") String T_ID,
                                     @Field("Email_Old") String Email_Old,
                                     @Field("Teacher_Fees") String Teacher_Fees,
                                     @Field("Experience") String Experience);




    /*@GET("get_skill_for_register")
    Single<SkillsListResponse> get_skill_for_register();

    @Multipart
    @POST("update_profile")
    Single<JsonObject> update_profile(@Part("user_id") RequestBody user_id,
                                      @Part("name") RequestBody name,
                                      @Part("email") RequestBody email,
                                      @Part("mobile_number") RequestBody mobile_number,
                                      @Part("description") RequestBody description,
                                      @Part("skill_id") RequestBody skill_id,
                                      @Part MultipartBody.Part profile_image,
                                      @Part("is_email") RequestBody is_email,
                                      @Part("is_mobile") RequestBody is_mobile,
                                      @Part("public_contact_number") RequestBody public_contact_number,
                                      @Part("additional_terms") RequestBody additional_terms,
                                      @Part("rate_of_service") RequestBody rate_of_service,
                                      @Part MultipartBody.Part banner_image,
                                      @Part("is_chat") RequestBody is_chat);*/

}
