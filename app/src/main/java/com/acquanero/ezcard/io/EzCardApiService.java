package com.acquanero.ezcard.io;

import com.acquanero.ezcard.model.SimpleResponse;
import com.acquanero.ezcard.model.UserData;
import com.acquanero.ezcard.model.UserIdToken;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

//interface que define las rutas a la API

public interface EzCardApiService {

    @FormUrlEncoded
    @POST("user/login/")
    Call<UserIdToken> postDataGetToken(
            @Header("xappid") String xappid,
            @Field("mail") String mail,
            @Field("password") String password

    );

    @GET("user/login/{id}")
    Call<SimpleResponse> logInWithToken(
            @Header("xappid") String xappid,
            @Header("token") String elToken,
            @Path("id") int id
    );

    @GET("user/{id}")
    public Call<UserData> getUserData(
            @Header("xappid") String xappid,
            @Header("token") String elToken,
            @Path("id") int id
    );

    @FormUrlEncoded
    @POST("user/register/")
    Call<UserIdToken> postToRegister(
            @Header("xappid") String xappid,
            @Field("name") String name,
            @Field("last_name") String last_name,
            @Field("password") String password,
            @Field("email") String mail,
            @Field("cellphone") String phone,
            @Field("pin") int pin
    );

    @FormUrlEncoded
    @POST("user/recoveraccount/")
    Call<SimpleResponse> postToRecover(
            @Header("xappid") String xappid,
            @Field("email") String mail
    );
}
