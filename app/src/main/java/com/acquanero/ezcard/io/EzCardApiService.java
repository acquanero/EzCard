package com.acquanero.ezcard.io;

import com.acquanero.ezcard.model.UserData;
import com.acquanero.ezcard.model.UserIdToken;
import com.acquanero.ezcard.model.UserMailPass;
import com.google.gson.JsonObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
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
            @Field("mail") String mail,
            @Field("password") String password
    );

    @GET("user/login/{id}")
    public Call logInWithToken(
            @Path("id") int id,
            @Header("token") String elToken
    );

    @GET("user/{id}")
    public Call<UserData> getUserData(
            @Path("id") int id,
            @Header("token") String elToken
    );
}
